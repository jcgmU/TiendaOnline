package com.jcgmu.tiendaonline

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var verPerfilButton: Button
    private lateinit var verProductosButton: Button
    private lateinit var verUbicacionButton: Button

    // Registrar el contrato para solicitar permisos
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permiso concedido, puedes proceder con la carga de imágenes
            Toast.makeText(this, "Permiso de lectura de imágenes concedido", Toast.LENGTH_SHORT).show()
        } else {
            // Permiso denegado, informa al usuario que la funcionalidad no estará disponible
            Toast.makeText(this, "Permiso de lectura de imágenes denegado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TiendaOnline)
        setContentView(R.layout.activity_main)

        verPerfilButton = findViewById(R.id.verPerfilButton)
        verProductosButton = findViewById(R.id.verProductosButton)
        verUbicacionButton = findViewById(R.id.verUbicacionButton)

        // Solicitar permisos al iniciar la actividad
        solicitarPermisos()

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

    private fun solicitarPermisos() {
        val permisoImagen = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(
                this,
                permisoImagen
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permiso ya concedido, proceder
                Toast.makeText(this, "Permiso de lectura de imágenes ya concedido", Toast.LENGTH_SHORT).show()
            }
            shouldShowRequestPermissionRationale(permisoImagen) -> {
                // Mostrar una explicación al usuario de por qué se necesita el permiso
                Toast.makeText(this, "Se necesita permiso para acceder a las imágenes", Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(permisoImagen)
            }
            else -> {
                // Solicitar el permiso
                requestPermissionLauncher.launch(permisoImagen)
            }
        }
    }
}
