package com.jcgmu.tiendaonline

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class RegistroActivity : AppCompatActivity() {

    private lateinit var nombreEditText: EditText
    private lateinit var correoEditText: EditText
    private lateinit var contrasenaEditText: EditText
    private lateinit var registrarButton: Button

    private lateinit var db: AppDatabase
    private val uiScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        nombreEditText = findViewById(R.id.nombreEditText)
        correoEditText = findViewById(R.id.correoEditText)
        contrasenaEditText = findViewById(R.id.contrasenaEditText)
        registrarButton = findViewById(R.id.registrarButton)

        // Inicializar la base de datos
        db = AppDatabase.obtenerBaseDatos(this)

        registrarButton.setOnClickListener {
            val nombre = nombreEditText.text.toString()
            val correo = correoEditText.text.toString()
            val contrasena = contrasenaEditText.text.toString()

            if (nombre.isNotEmpty() && correo.isNotEmpty() && contrasena.isNotEmpty()) {
                val nuevoUsuario = Usuario(
                    nombre = nombre,
                    correo = correo,
                    contrasena = contrasena,
                    rol = "user" // Asignamos el rol de "user" por defecto
                )

                uiScope.launch {
                    insertarUsuario(nuevoUsuario)
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private suspend fun insertarUsuario(usuario: Usuario) {
        withContext(Dispatchers.IO) {
            db.usuarioDao().insertarUsuario(usuario)
        }
        withContext(Dispatchers.Main) {
            Toast.makeText(this@RegistroActivity, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
            // Redirigir a la pantalla de login
            val intent = Intent(this@RegistroActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        uiScope.cancel()
    }
}
