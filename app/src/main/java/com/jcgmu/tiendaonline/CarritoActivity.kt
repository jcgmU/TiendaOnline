package com.jcgmu.tiendaonline

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CarritoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var totalTextView: TextView
    private lateinit var checkoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        recyclerView = findViewById(R.id.recyclerViewCarrito)
        totalTextView = findViewById(R.id.totalTextView)
        checkoutButton = findViewById(R.id.checkoutButton)

        recyclerView.layoutManager = LinearLayoutManager(this)

        cargarProductos()

        checkoutButton.setOnClickListener {
            if (CarritoManager.obtenerProductos().isNotEmpty()) {
                val intent = Intent(this, CheckoutActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "El carrito está vacío.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarProductos() {
        CarritoManager.cargarCarrito(this)
        val productos = CarritoManager.obtenerProductos()

        if (productos.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío.", Toast.LENGTH_SHORT).show()
            return
        }

        val adapter = ProductoAdapter(this, productos) { producto ->
            Toast.makeText(this, "${producto.nombre} seleccionado", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        val total = CarritoManager.calcularTotal()
        totalTextView.text = "Total: COP $total"
    }
}
