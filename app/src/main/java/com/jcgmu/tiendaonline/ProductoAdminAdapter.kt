// ProductoAdminAdapter.kt
package com.jcgmu.tiendaonline

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductoAdminAdapter(
    private val context: Context,
    private val productos: List<Producto>,
    private val accionCallback: (Producto, String) -> Unit
) : RecyclerView.Adapter<ProductoAdminAdapter.ProductoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_producto_admin, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.bind(producto)
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreTextView: TextView = itemView.findViewById(R.id.nombreProductoAdmin)
        private val descripcionTextView: TextView = itemView.findViewById(R.id.descripcionProductoAdmin)
        private val precioTextView: TextView = itemView.findViewById(R.id.precioProductoAdmin)
        private val imagenImageView: ImageView = itemView.findViewById(R.id.imagenProductoAdmin)
        private val editarButton: Button = itemView.findViewById(R.id.editarProductoButton)
        private val eliminarButton: Button = itemView.findViewById(R.id.eliminarProductoButton)

        fun bind(producto: Producto) {
            nombreTextView.text = producto.nombre
            descripcionTextView.text = producto.descripcion
            precioTextView.text = "COP ${String.format("%.2f", producto.precio)}"
            imagenImageView.setImageURI(Uri.parse(producto.imagenUri))

            editarButton.setOnClickListener {
                accionCallback(producto, "editar")
            }

            eliminarButton.setOnClickListener {
                accionCallback(producto, "eliminar")
            }
        }
    }
}