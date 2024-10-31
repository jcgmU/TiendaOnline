package com.jcgmu.tiendaonline

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AdminPanelActivity : AppCompatActivity() {

    private lateinit var agregarProductosButton: Button
    private lateinit var administrarUsuariosButton: Button
    private lateinit var tiendaOnlineButton: Button
    private lateinit var cerrarSesionButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

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
            val intent = Intent(this, ListadoProductosActivity::class.java)
            startActivity(intent)
        }

        cerrarSesionButton.setOnClickListener {
            // Regresar al login
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}
