package com.jcgmu.tiendaonline

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registrarLinkButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.login_button)
        registrarLinkButton = findViewById(R.id.registrarLinkButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Validar las credenciales desde SharedPreferences
                val sharedPref = getSharedPreferences("Usuarios", Context.MODE_PRIVATE)
                val storedPassword = sharedPref.getString("contrasena_$email", null)

                if (storedPassword != null && storedPassword == password) {
                    // Login exitoso, navegar a la lista de productos
                    val intent = Intent(this, ListadoProductosActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Mostrar mensaje de error
                    Toast.makeText(this, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Redirigir a la pantalla de registro
        registrarLinkButton.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}
