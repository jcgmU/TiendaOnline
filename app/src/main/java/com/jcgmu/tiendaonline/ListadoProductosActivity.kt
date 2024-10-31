package com.jcgmu.tiendaonline

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListadoProductosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productoAdapter: ProductoAdapter
    private lateinit var productosList: List<Producto>
    private lateinit var verCarritoButton: Button
    private lateinit var cerrarSesionButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TiendaOnline)
        setContentView(R.layout.activity_listado_productos)

        recyclerView = findViewById(R.id.recyclerViewProductos)
        verCarritoButton = findViewById(R.id.verCarritoButton)
        cerrarSesionButton = findViewById(R.id.cerrarSesionButton)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val db = AppDatabase.obtenerBaseDatos(this)
        CoroutineScope(Dispatchers.IO).launch {
            productosList = db.productoDao().obtenerProductos()
            withContext(Dispatchers.Main) {
                productoAdapter = ProductoAdapter(this@ListadoProductosActivity, productosList) { producto ->
                    CarritoManager.agregarProducto(this@ListadoProductosActivity, producto)
                    Toast.makeText(this@ListadoProductosActivity, "${producto.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
                }
                recyclerView.adapter = productoAdapter
            }
        }

        verCarritoButton.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }

        cerrarSesionButton.setOnClickListener {
            mostrarDialogoCerrarSesion()
        }
    }

    override fun onResume() {
        super.onResume()
        val db = AppDatabase.obtenerBaseDatos(this)
        CoroutineScope(Dispatchers.IO).launch {
            productosList = db.productoDao().obtenerProductos()
            withContext(Dispatchers.Main) {
                productoAdapter.actualizarLista(productosList)
            }
        }
    }

    private fun mostrarDialogoCerrarSesion() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cerrar Sesión")
        builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
        builder.setPositiveButton("Sí") { _, _ ->
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}
