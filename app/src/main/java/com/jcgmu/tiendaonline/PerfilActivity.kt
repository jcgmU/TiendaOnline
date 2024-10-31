package com.jcgmu.tiendaonline

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerfilActivity : AppCompatActivity() {

    private lateinit var nombreTextView: TextView
    private lateinit var correoTextView: TextView
    private lateinit var rolTextView: TextView

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TiendaOnline)
        setContentView(R.layout.activity_perfil)

        nombreTextView = findViewById(R.id.nombreTextView)
        correoTextView = findViewById(R.id.correoTextView)
        rolTextView = findViewById(R.id.rolTextView)

        db = AppDatabase.obtenerBaseDatos(this)

        cargarDatosDelUsuario()
    }

    private fun cargarDatosDelUsuario() {
        lifecycleScope.launch {
            // Obtener el ID del usuario actual desde SharedPreferences
            val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val usuarioId = sharedPref.getInt("usuario_id", -1)

            if (usuarioId != -1) {
                val usuario = withContext(Dispatchers.IO) {
                    db.usuarioDao().obtenerUsuarioPorId(usuarioId)
                }

                if (usuario != null) {
                    nombreTextView.text = "Nombre: ${usuario.nombre}"
                    correoTextView.text = "Correo: ${usuario.correo}"
                    rolTextView.text = "Rol: ${usuario.rol}"
                } else {
                    Toast.makeText(this@PerfilActivity, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@PerfilActivity, "Usuario no identificado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
