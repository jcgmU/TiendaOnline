package com.jcgmu.tiendaonline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class ComprasAdapter : RecyclerView.Adapter<ComprasAdapter.CompraViewHolder>() {

    private var compras: List<CompraConProducto> = emptyList()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    class CompraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fechaTextView: TextView = itemView.findViewById(R.id.fechaCompraTextView)
        val productoTextView: TextView = itemView.findViewById(R.id.productoCompraTextView)
        val cantidadPrecioTextView: TextView = itemView.findViewById(R.id.cantidadPrecioCompraTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompraViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_compra, parent, false)
        return CompraViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompraViewHolder, position: Int) {
        val compra = compras[position]
        val fecha = java.util.Date(compra.fecha) // Convertir Long a Date
        holder.fechaTextView.text = "Fecha: ${dateFormat.format(fecha)}"
        holder.productoTextView.text = "Producto: ${compra.productoNombre}"
        holder.cantidadPrecioTextView.text = "Cantidad: ${compra.cantidad} | Total: \$${String.format("%.2f", compra.precioTotal)}"
    }

    override fun getItemCount(): Int = compras.size

    fun setCompras(compras: List<CompraConProducto>) {
        this.compras = compras
        notifyDataSetChanged()
    }
}
