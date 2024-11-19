package com.jcgmu.tiendaonline

import com.jcgmu.tiendaonline.CarritoActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*


class ListadoProductosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productoAdapter: ProductoAdapter
    private lateinit var verCarritoButton: Button
    private lateinit var cerrarSesionButton: Button
    private lateinit var db: AppDatabase

    private var productosList: List<Producto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TiendaOnline)
        setContentView(R.layout.activity_listado_productos)

        recyclerView = findViewById(R.id.recyclerViewProductos)
        verCarritoButton = findViewById(R.id.verCarritoButton)
        cerrarSesionButton = findViewById(R.id.cerrarSesionButton)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializar el adaptador con una lista vacía
        productoAdapter = ProductoAdapter(this, productosList) { producto ->
            CarritoManager.agregarProducto(this, producto)
            Toast.makeText(this, "${producto.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = productoAdapter

        db = AppDatabase.obtenerBaseDatos(this)

        cargarProductos()

        verCarritoButton.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
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
        CoroutineScope(Dispatchers.IO).launch {
            val productos = db.productoDao().obtenerProductos()
            withContext(Dispatchers.Main) {
                productosList = productos
                productoAdapter.actualizarLista(productosList)
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

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}
