package com.jcgmu.tiendaonline

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*

class GestionProductosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productoAdapter: ProductoAdminAdapter
    private lateinit var agregarProductoButton: Button
    private lateinit var cerrarSesionButton: Button

    private var productosList: MutableList<Producto> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_productos)

        recyclerView = findViewById(R.id.recyclerViewProductosAdmin)
        agregarProductoButton = findViewById(R.id.agregarProductoButton)
        cerrarSesionButton = findViewById(R.id.cerrarSesionButton)

        recyclerView.layoutManager = LinearLayoutManager(this)

        productoAdapter = ProductoAdminAdapter(this, productosList) { producto, accion ->
            when (accion) {
                "editar" -> {
                    val intent = Intent(this, EditarProductoActivity::class.java)
                    intent.putExtra("productoId", producto.id)
                    startActivity(intent)
                }
                "eliminar" -> {
                    mostrarDialogoEliminarProducto(producto)
                }
            }
        }
        recyclerView.adapter = productoAdapter

        // Cargar los productos desde la base de datos
        cargarProductos()

        agregarProductoButton.setOnClickListener {
            val intent = Intent(this, AgregarProductoActivity::class.java)
            startActivity(intent)
        }

        cerrarSesionButton.setOnClickListener {
            mostrarDialogoCerrarSesion()
        }
    }

    override fun onResume() {
        super.onResume()
        cargarProductos()
    }

    private fun cargarProductos() {
        val db = AppDatabase.obtenerBaseDatos(this)
        CoroutineScope(Dispatchers.IO).launch {
            val productos = db.productoDao().obtenerProductos()
            withContext(Dispatchers.Main) {
                productosList.clear()
                productosList.addAll(productos)
                productoAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun mostrarDialogoEliminarProducto(producto: Producto) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Producto")
        builder.setMessage("¿Estás seguro de que deseas eliminar este producto?")
        builder.setPositiveButton("Sí") { _, _ ->
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.obtenerBaseDatos(this@GestionProductosActivity)
                db.productoDao().eliminarProducto(producto)
                withContext(Dispatchers.Main) {
                    productosList.remove(producto)
                    productoAdapter.notifyDataSetChanged()
                }
            }
        }
        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        builder.show()
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
