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
    private lateinit var pagarButton: Button
    private lateinit var cerrarSesionButton: Button
    private lateinit var productosEnCarrito: MutableList<Producto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TiendaOnline)
        setContentView(R.layout.activity_carrito)

        recyclerView = findViewById(R.id.recyclerViewCarrito)
        totalTextView = findViewById(R.id.totalTextView)
        pagarButton = findViewById(R.id.pagarButton)
        cerrarSesionButton = findViewById(R.id.cerrarSesionButton)

        recyclerView.layoutManager = LinearLayoutManager(this)
        productosEnCarrito = CarritoManager.obtenerProductos().toMutableList()

        carritoAdapter = CarritoAdapter(this, productosEnCarrito) { producto ->
            CarritoManager.eliminarProducto(this, producto)
            productosEnCarrito.remove(producto)
            carritoAdapter.notifyDataSetChanged()
            actualizarTotal()

            if (productosEnCarrito.isEmpty()) {
                val intent = Intent(this, ListadoProductosActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        }
        recyclerView.adapter = carritoAdapter

        actualizarTotal()

        // Configurar botón Pagar
        pagarButton.setOnClickListener {
            mostrarDialogoPago()
        }

        // Configurar el botón de cerrar sesión
        cerrarSesionButton.setOnClickListener {
            mostrarDialogoCerrarSesion()
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
            CarritoManager.limpiarCarrito(this)
            val intent = Intent(this, ListadoProductosActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
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
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}
