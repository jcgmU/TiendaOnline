package com.jcgmu.tiendaonline

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TiendaOnline)

        // Inicializar la base de datos
        db = AppDatabase.obtenerBaseDatos(this)

        // Inicializar SharedPreferences
        sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)

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
            // Guardar el ID del usuario en SharedPreferences en el hilo principal
            withContext(Dispatchers.Main) {
                sharedPref.edit().putInt("usuario_id", usuario.id).apply()

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
}
