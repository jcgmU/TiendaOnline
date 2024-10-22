package com.jcgmu.tiendaonline

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast

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

        // Inicializar elementos de la vista
        recyclerView = findViewById(R.id.recyclerViewProductos)
        verCarritoButton = findViewById(R.id.verCarritoButton)
        cerrarSesionButton = findViewById(R.id.cerrarSesionButton)

        recyclerView.layoutManager = LinearLayoutManager(this)
        productosList = obtenerListaDeProductos()

        // Configurar el adaptador
        productoAdapter = ProductoAdapter(this, productosList) { producto ->
            CarritoManager.agregarProducto(this, producto)
            Toast.makeText(this, "${producto.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = productoAdapter

        // Navegar al carrito
        verCarritoButton.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }

        // Configurar el botón de cerrar sesión
        cerrarSesionButton.setOnClickListener {
            mostrarDialogoCerrarSesion()
        }
    }

    private fun obtenerListaDeProductos(): List<Producto> {
        return listOf(
            Producto("Camiseta", "Camiseta 100% algodón", 20.0, R.drawable.producto1),
            Producto("Pantalón", "Pantalón de mezclilla", 35.0, R.drawable.producto2),
            Producto("Zapatos", "Zapatos de cuero", 50.0, R.drawable.producto3),
            Producto("Gorra", "Gorra deportiva", 15.0, R.drawable.producto4),
            Producto("Reloj", "Reloj de pulsera", 80.0, R.drawable.producto5)
        )
    }

    private fun mostrarDialogoCerrarSesion() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cerrar Sesión")
        builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
        builder.setPositiveButton("Sí") { _, _ ->
            // Redirigir a la pantalla de inicio de sesión
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            // Cerrar el cuadro de diálogo y no hacer nada
            dialog.dismiss()
        }
        builder.show()
    }
}
