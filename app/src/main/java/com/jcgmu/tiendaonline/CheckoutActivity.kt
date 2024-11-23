package com.jcgmu.tiendaonline

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CheckoutActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var totalTextView: TextView
    private lateinit var pagarButton: Button

    private var productosCarrito: List<Producto> = emptyList()
    private var total: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        recyclerView = findViewById(R.id.recyclerViewCheckout)
        totalTextView = findViewById(R.id.totalTextView)
        pagarButton = findViewById(R.id.pagarButton)

        recyclerView.layoutManager = LinearLayoutManager(this)

        cargarProductosCarrito()

        pagarButton.setOnClickListener {
            if (productosCarrito.isNotEmpty() && total > 0.0) {
                val intent = Intent(this, PaymentPageActivity::class.java)
                intent.putExtra("total", total)
                startActivity(intent)
            } else {
                Toast.makeText(this, "El carrito está vacío o el total es inválido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarProductosCarrito() {
        CarritoManager.cargarCarrito(this)
        productosCarrito = CarritoManager.obtenerProductos()
        total = CarritoManager.calcularTotal()

        if (productosCarrito.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
        }

        val adapter = ProductoAdapter(this, productosCarrito) { producto ->
            Toast.makeText(this, "${producto.nombre} seleccionado", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter
        totalTextView.text = "Total: COP $total"
    }
}
