package com.jcgmu.tiendaonline

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var correoEditText: EditText
    private lateinit var contrasenaEditText: EditText
    private lateinit var iniciarSesionButton: Button
    private lateinit var registrarseButton: Button

    private lateinit var db: AppDatabase
    private lateinit var sharedPref: android.content.SharedPreferences

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TiendaOnline)

        // Inicializar la base de datos
        db = AppDatabase.obtenerBaseDatos(this)

        // Inicializar SharedPreferences
        sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)

        // Inicializar el cliente de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Verificar si ya existe un usuario logueado
        val usuarioId = sharedPref.getInt("usuario_id", -1)
        if (usuarioId != -1) {
            // Obtener el usuario para determinar a dónde redirigir
            lifecycleScope.launch {
                val usuario = withContext(Dispatchers.IO) {
                    db.usuarioDao().obtenerUsuarioPorId(usuarioId)
                }
                if (usuario != null) {
                    if (usuario.rol == "admin") {
                        val intent = Intent(this@LoginActivity, AdminPanelActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        } else {
            // Si no hay usuario logueado, continuar con la configuración de vistas
            setContentView(R.layout.activity_login)

            correoEditText = findViewById(R.id.correoEditText)
            contrasenaEditText = findViewById(R.id.contrasenaEditText)
            iniciarSesionButton = findViewById(R.id.iniciarSesionButton)
            registrarseButton = findViewById(R.id.registrarseButton)

            iniciarSesionButton.setOnClickListener {
                val correo = correoEditText.text.toString()
                val contrasena = contrasenaEditText.text.toString()

                if (correo.isNotEmpty() && contrasena.isNotEmpty()) {
                    lifecycleScope.launch {
                        iniciarSesion(correo, contrasena)
                    }
                } else {
                    Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            }

            registrarseButton.setOnClickListener {
                val intent = Intent(this, RegistroActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private suspend fun iniciarSesion(correo: String, contrasena: String) {
        val usuario = withContext(Dispatchers.IO) {
            db.usuarioDao().obtenerUsuarioPorCorreoYContrasena(correo, contrasena)
        }

        if (usuario != null) {
            withContext(Dispatchers.Main) {
                // Guardar el ID del usuario en SharedPreferences
                sharedPref.edit().putInt("usuario_id", usuario.id).apply()

                // Solicitar permisos de ubicación si no están otorgados
                if (ActivityCompat.checkSelfPermission(
                        this@LoginActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this@LoginActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@LoginActivity,
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                } else {
                    // Obtener ubicación y actualizar usuario
                    obtenerUbicacionYActualizarUsuario(usuario)
                }

                // Redirigir a la actividad correspondiente
                if (usuario.rol == "admin") {
                    val intent = Intent(this@LoginActivity, AdminPanelActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                finish()
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@LoginActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun obtenerUbicacionYActualizarUsuario(usuario: Usuario) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Los permisos no están otorgados
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                lifecycleScope.launch {
                    usuario.latitud = it.latitude
                    usuario.longitud = it.longitude

                    withContext(Dispatchers.IO) {
                        db.usuarioDao().actualizarUsuario(usuario)
                    }
                }
            }
        }
    }

    // Manejar el resultado de la solicitud de permisos
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permiso otorgado, podemos obtener la ubicación
                lifecycleScope.launch {
                    val usuarioId = sharedPref.getInt("usuario_id", -1)
                    if (usuarioId != -1) {
                        val usuario = withContext(Dispatchers.IO) {
                            db.usuarioDao().obtenerUsuarioPorId(usuarioId)
                        }
                        usuario?.let {
                            obtenerUbicacionYActualizarUsuario(it)
                        }
                    }
                }
            } else {
                // Permiso denegado, manejar adecuadamente
                Toast.makeText(
                    this,
                    "No se otorgaron permisos de ubicación",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
