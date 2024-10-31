package com.jcgmu.tiendaonline

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private lateinit var verPerfilButton: Button
    private lateinit var verProductosButton: Button
    private lateinit var verUbicacionButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TiendaOnline)
        setContentView(R.layout.activity_main)

        verPerfilButton = findViewById(R.id.verPerfilButton)
        verProductosButton = findViewById(R.id.verProductosButton)
        verUbicacionButton = findViewById(R.id.verUbicacionButton)

        verPerfilButton.setOnClickListener {
            // Navegar a PerfilActivity
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }

        verProductosButton.setOnClickListener {
            // Navegar a ListadoProductosActivity
            val intent = Intent(this, ListadoProductosActivity::class.java)
            startActivity(intent)
        }

        verUbicacionButton.setOnClickListener {
            // Alternar la visibilidad del fragmento del mapa
            val mapaFragment = supportFragmentManager.findFragmentById(R.id.mapaFragment)
            mapaFragment?.let {
                val transaction = supportFragmentManager.beginTransaction()
                if (it.isVisible) {
                    transaction.hide(it).commit()
                    verUbicacionButton.text = "Mostrar Mi Ubicación"
                } else {
                    transaction.show(it).commit()
                    verUbicacionButton.text = "Ocultar Mi Ubicación"
                }
            } ?: run {
                // El fragmento no existe, manejar este caso si es necesario
                Toast.makeText(this, "El mapa no está disponible", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
