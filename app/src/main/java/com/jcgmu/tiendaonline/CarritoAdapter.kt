package com.jcgmu.tiendaonline
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CarritoAdapter(
    private val context: Context,
    private val productos: List<Producto>,
    private val eliminarProductoCallback: (Producto) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val producto = productos[position]
        holder.bind(producto)
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    inner class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreTextView: TextView = itemView.findViewById(R.id.nombreProductoCarrito)
        private val precioTextView: TextView = itemView.findViewById(R.id.precioProductoCarrito)
        private val imagenImageView: ImageView = itemView.findViewById(R.id.imagenProductoCarrito)
        private val eliminarButton: Button = itemView.findViewById(R.id.eliminarCarritoButton)

        fun bind(producto: Producto) {
            nombreTextView.text = producto.nombre
            precioTextView.text = "$${String.format("%.2f", producto.precio)}"
            imagenImageView.setImageResource(producto.imagenResId)

            eliminarButton.setOnClickListener {
                eliminarProductoCallback(producto)
            }
        }
    }
}
