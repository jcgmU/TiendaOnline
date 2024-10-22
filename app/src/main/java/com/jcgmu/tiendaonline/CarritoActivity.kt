package com.jcgmu.tiendaonline

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CarritoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carritoAdapter: CarritoAdapter
    private lateinit var totalTextView: TextView
    private lateinit var productosEnCarrito: MutableList<Producto>
    private lateinit var pagarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TiendaOnline) // Usar el tema correcto
        setContentView(R.layout.activity_carrito)

        recyclerView = findViewById(R.id.recyclerViewCarrito)
        recyclerView.layoutManager = LinearLayoutManager(this)

        totalTextView = findViewById(R.id.totalTextView)
        pagarButton = findViewById(R.id.pagarButton)

        // Cargar productos del carrito
        productosEnCarrito = CarritoManager.obtenerProductos().toMutableList()

        // Configurar el adaptador
        carritoAdapter = CarritoAdapter(this, productosEnCarrito) { producto ->
            // Eliminar producto del carrito
            CarritoManager.eliminarProducto(this, producto)
            productosEnCarrito.remove(producto)
            carritoAdapter.notifyDataSetChanged()
            actualizarTotal()
        }

        recyclerView.adapter = carritoAdapter

        actualizarTotal()

        pagarButton.setOnClickListener {
            // Simular pago
            mostrarDialogoPago()
        }
    }

    private fun actualizarTotal() {
        val total = CarritoManager.calcularTotal()
        totalTextView.text = "Total: $${String.format("%.2f", total)}"
    }

    private fun mostrarDialogoPago() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pago realizado")
        builder.setMessage("Gracias por su compra.")
        builder.setPositiveButton("Aceptar") { _, _ ->
            // Limpiar carrito y regresar a la lista de productos
            CarritoManager.limpiarCarrito(this)
            val intent = Intent(this, ListadoProductosActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
        builder.show()
    }
}
