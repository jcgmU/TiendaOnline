package com.jcgmu.tiendaonline

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerfilActivity : AppCompatActivity() {

    private lateinit var nombreTextView: TextView
    private lateinit var correoTextView: TextView
    private lateinit var recyclerViewCompras: RecyclerView
    private lateinit var comprasAdapter: ComprasAdapter
    private lateinit var editarPerfilButton: Button
    private lateinit var cerrarSesionButton: Button

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TiendaOnline)
        setContentView(R.layout.activity_perfil)

        nombreTextView = findViewById(R.id.nombreTextView)
        correoTextView = findViewById(R.id.correoTextView)
        recyclerViewCompras = findViewById(R.id.recyclerViewCompras)
        editarPerfilButton = findViewById(R.id.editarPerfilButton)
        cerrarSesionButton = findViewById(R.id.cerrarSesionButton)

        // Configurar RecyclerView
        recyclerViewCompras.layoutManager = LinearLayoutManager(this)
        comprasAdapter = ComprasAdapter()
        recyclerViewCompras.adapter = comprasAdapter

        db = AppDatabase.obtenerBaseDatos(this)

        cargarDatosDelUsuario()
        cargarComprasDelUsuario()

        // Configurar botón Editar Perfil
        editarPerfilButton.setOnClickListener {
            val intent = Intent(this, EditarPerfilActivity::class.java)
            startActivity(intent)
        }

        // Configurar botón Cerrar Sesión
        cerrarSesionButton.setOnClickListener {
            mostrarDialogoCerrarSesion()
        }
    }

    override fun onResume() {
        super.onResume()
        // Volver a cargar los datos en caso de que hayan sido actualizados
        cargarDatosDelUsuario()
        cargarComprasDelUsuario()
    }

    private fun cargarDatosDelUsuario() {
        lifecycleScope.launch {
            val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val usuarioId = sharedPref.getInt("usuario_id", -1)

            if (usuarioId != -1) {
                val usuario = withContext(Dispatchers.IO) {
                    db.usuarioDao().obtenerUsuarioPorId(usuarioId)
                }

                withContext(Dispatchers.Main) {
                    if (usuario != null) {
                        Log.d("PerfilActivity", "Usuario: $usuario")
                        nombreTextView.text = "Nombre: ${usuario.nombre}"
                        correoTextView.text = "Correo: ${usuario.correo}"
                    } else {
                        Toast.makeText(this@PerfilActivity, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PerfilActivity, "Usuario no identificado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun cargarComprasDelUsuario() {
        lifecycleScope.launch {
            val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val usuarioId = sharedPref.getInt("usuario_id", -1)

            if (usuarioId != -1) {
                val compras = withContext(Dispatchers.IO) {
                    db.compraDao().obtenerComprasPorUsuario(usuarioId)
                }

                withContext(Dispatchers.Main) {
                    if (compras.isNotEmpty()) {
                        Log.d("PerfilActivity", "Compras: $compras")
                        val comprasConProducto = compras.map { compra ->
                            val producto = db.productoDao().obtenerProductoPorId(compra.productoId)
                            CompraConProducto(
                                id = compra.id,
                                usuarioId = compra.usuarioId,
                                productoNombre = producto?.nombre ?: "Producto Desconocido",
                                cantidad = compra.cantidad,
                                precioTotal = compra.precioTotal,
                                fecha = compra.fecha
                            )
                        }
                        comprasAdapter.setCompras(comprasConProducto)
                    } else {
                        Toast.makeText(this@PerfilActivity, "No hay compras realizadas", Toast.LENGTH_SHORT).show()
                        comprasAdapter.setCompras(emptyList())
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PerfilActivity, "Usuario no identificado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun mostrarDialogoCerrarSesion() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cerrar Sesión")
        builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
        builder.setPositiveButton("Sí") { _, _ ->
            // Eliminar el usuario_id de SharedPreferences
            val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            sharedPref.edit().remove("usuario_id").apply()

            // Redirigir al usuario a la pantalla de inicio de sesión
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}
