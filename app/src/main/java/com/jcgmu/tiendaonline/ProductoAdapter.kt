package com.jcgmu.tiendaonline
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductoAdapter(
    private val context: Context,
    private val productos: List<Producto>,
    private val agregarProductoCallback: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false)
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
        private val nombreTextView: TextView = itemView.findViewById(R.id.nombreProducto)
        private val descripcionTextView: TextView = itemView.findViewById(R.id.descripcionProducto)
        private val precioTextView: TextView = itemView.findViewById(R.id.precioProducto)
        private val imagenImageView: ImageView = itemView.findViewById(R.id.imagenProducto)
        private val agregarButton: Button = itemView.findViewById(R.id.agregarCarritoButton)

        fun bind(producto: Producto) {
            nombreTextView.text = producto.nombre
            descripcionTextView.text = producto.descripcion
            precioTextView.text = "$${String.format("%.2f", producto.precio)}"
            imagenImageView.setImageResource(producto.imagenResId)

            agregarButton.setOnClickListener {
                agregarProductoCallback(producto)
            }
        }
    }
}