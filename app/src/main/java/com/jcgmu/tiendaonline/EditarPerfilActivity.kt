// EditarPerfilActivity.kt
package com.jcgmu.tiendaonline

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var editNombreEditText: EditText
    private lateinit var editCorreoEditText: EditText
    private lateinit var guardarPerfilButton: Button

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TiendaOnline)
        setContentView(R.layout.activity_editar_perfil)

        editNombreEditText = findViewById(R.id.editNombreEditText)
        editCorreoEditText = findViewById(R.id.editCorreoEditText)
        guardarPerfilButton = findViewById(R.id.guardarPerfilButton)

        db = AppDatabase.obtenerBaseDatos(this)

        cargarDatosDelUsuario()

        guardarPerfilButton.setOnClickListener {
            val nuevoNombre = editNombreEditText.text.toString().trim()
            val nuevoCorreo = editCorreoEditText.text.toString().trim()

            if (nuevoNombre.isEmpty() || nuevoCorreo.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                actualizarPerfil(nuevoNombre, nuevoCorreo)
            }
        }
    }

    private fun cargarDatosDelUsuario() {
        lifecycleScope.launch {
            val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val usuarioId = sharedPref.getInt("usuario_id", -1)

            if (usuarioId != -1) {
                val usuario = withContext(Dispatchers.IO) {
                    db.usuarioDao().obtenerUsuarioPorId(usuarioId)
                }

                if (usuario != null) {
                    editNombreEditText.setText(usuario.nombre)
                    editCorreoEditText.setText(usuario.correo)
                } else {
                    Toast.makeText(this@EditarPerfilActivity, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this@EditarPerfilActivity, "Usuario no identificado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun actualizarPerfil(nuevoNombre: String, nuevoCorreo: String) {
        lifecycleScope.launch {
            val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val usuarioId = sharedPref.getInt("usuario_id", -1)

            if (usuarioId != -1) {
                val usuario = withContext(Dispatchers.IO) {
                    db.usuarioDao().obtenerUsuarioPorId(usuarioId)
                }

                if (usuario != null) {
                    usuario.nombre = nuevoNombre
                    usuario.correo = nuevoCorreo

                    withContext(Dispatchers.IO) {
                        db.usuarioDao().actualizarUsuario(usuario)
                    }

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@EditarPerfilActivity, "Perfil actualizado exitosamente", Toast.LENGTH_SHORT).show()
                        finish() // Volver a la actividad de perfil
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@EditarPerfilActivity, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EditarPerfilActivity, "Usuario no identificado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
