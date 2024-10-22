package com.jcgmu.tiendaonline

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegistroActivity : AppCompatActivity() {

    private lateinit var nombreEditText: EditText
    private lateinit var correoEditText: EditText
    private lateinit var contrasenaEditText: EditText
    private lateinit var registrarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        nombreEditText = findViewById(R.id.nombreEditText)
        correoEditText = findViewById(R.id.correoEditText)
        contrasenaEditText = findViewById(R.id.contrasenaEditText)
        registrarButton = findViewById(R.id.registrarButton)

        registrarButton.setOnClickListener {
            val nombre = nombreEditText.text.toString()
            val correo = correoEditText.text.toString()
            val contrasena = contrasenaEditText.text.toString()

            if (nombre.isNotEmpty() && correo.isNotEmpty() && contrasena.isNotEmpty()) {
                // Guardar los datos del usuario en SharedPreferences
                val sharedPref = getSharedPreferences("Usuarios", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("nombre_$correo", nombre)
                editor.putString("contrasena_$correo", contrasena)
                editor.apply()

                Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()

                // Redirigir a la pantalla de login
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
