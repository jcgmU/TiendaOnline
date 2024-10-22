package com.jcgmu.tiendaonline

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast

class ListadoProductosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productoAdapter: ProductoAdapter
    private lateinit var productosList: List<Producto>
    private lateinit var verCarritoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TiendaOnline) // Usar el tema correcto
        setContentView(R.layout.activity_listado_productos)

        // Cargar el carrito desde SharedPreferences
        CarritoManager.cargarCarrito(this)

        recyclerView = findViewById(R.id.recyclerViewProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        verCarritoButton = findViewById(R.id.verCarritoButton)

        // Inicializar la lista de productos
        productosList = obtenerListaDeProductos()

        // Configurar el adaptador
        productoAdapter = ProductoAdapter(this, productosList) { producto ->
            // Agregar producto al carrito
            CarritoManager.agregarProducto(this, producto)
            // Mostrar mensaje al usuario
            Toast.makeText(this, "${producto.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = productoAdapter

        verCarritoButton.setOnClickListener {
            // Navegar a la actividad del carrito
            startActivity(Intent(this, CarritoActivity::class.java))
        }
    }

    private fun obtenerListaDeProductos(): List<Producto> {
        // Aquí puedes agregar los productos que desees
        return listOf(
            Producto("Camiseta", "Camiseta 100% algodón", 20.0, R.drawable.producto1),
            Producto("Pantalón", "Pantalón de mezclilla", 35.0, R.drawable.producto2),
            Producto("Zapatos", "Zapatos de cuero", 50.0, R.drawable.producto3),
            Producto("Gorra", "Gorra deportiva", 15.0, R.drawable.producto4),
            Producto("Reloj", "Reloj de pulsera", 80.0, R.drawable.producto5)
        )
    }
}
