package com.jcgmu.tiendaonline

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class AdminPanelActivity : AppCompatActivity() {

    private lateinit var agregarProductosButton: Button
    private lateinit var administrarUsuariosButton: Button
    private lateinit var tiendaOnlineButton: Button
    private lateinit var cerrarSesionButton: Button

    private lateinit var sharedPref: android.content.SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        // Inicializar SharedPreferences
        sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

        agregarProductosButton = findViewById(R.id.agregarProductosButton)
        administrarUsuariosButton = findViewById(R.id.administrarUsuariosButton)
        tiendaOnlineButton = findViewById(R.id.tiendaOnlineButton)
        cerrarSesionButton = findViewById(R.id.cerrarSesionButton)

        agregarProductosButton.setOnClickListener {
            val intent = Intent(this, GestionProductosActivity::class.java)
            startActivity(intent)
        }

        administrarUsuariosButton.setOnClickListener {
            val intent = Intent(this, GestionUsuariosActivity::class.java)
            startActivity(intent)
        }

        tiendaOnlineButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        cerrarSesionButton.setOnClickListener {
            mostrarDialogoCerrarSesion()
        }
    }

    private fun mostrarDialogoCerrarSesion() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cerrar Sesión")
        builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
        builder.setPositiveButton("Sí") { _, _ ->
            // Eliminar el usuario_id de SharedPreferences
            sharedPref.edit().remove("usuario_id").apply()

            // Redirigir al usuario a la pantalla de inicio de sesión
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}
