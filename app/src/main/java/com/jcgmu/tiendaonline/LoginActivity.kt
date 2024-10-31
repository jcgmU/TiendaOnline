package com.jcgmu.tiendaonline

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {

    private lateinit var correoEditText: EditText
    private lateinit var contrasenaEditText: EditText
    private lateinit var iniciarSesionButton: Button
    private lateinit var registrarseButton: Button

    private lateinit var db: AppDatabase
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TiendaOnline)
        setContentView(R.layout.activity_login)

        correoEditText = findViewById(R.id.correoEditText)
        contrasenaEditText = findViewById(R.id.contrasenaEditText)
        iniciarSesionButton = findViewById(R.id.iniciarSesionButton)
        registrarseButton = findViewById(R.id.registrarseButton)

        db = AppDatabase.obtenerBaseDatos(this)
        sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)

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

    private suspend fun iniciarSesion(correo: String, contrasena: String) {
        val usuario = withContext(Dispatchers.IO) {
            db.usuarioDao().login(correo, contrasena)
        }
        if (usuario != null) {
            // Guardar el ID del usuario en SharedPreferences
            with(sharedPref.edit()) {
                putInt("usuario_id", usuario.id)
                apply()
            }

            if (usuario.rol == "admin") {
                val intent = Intent(this@LoginActivity, AdminPanelActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            Toast.makeText(this@LoginActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // No es necesario cancelar un CoroutineScope proporcionado por lifecycleScope
    }
}
