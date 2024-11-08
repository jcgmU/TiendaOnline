package com.jcgmu.tiendaonline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsuarioAdminAdapter(
    private val usuarios: List<Usuario>,
    private val onItemClick: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuarioAdminAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
        val correoTextView: TextView = itemView.findViewById(R.id.correoTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario_admin, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.nombreTextView.text = usuario.nombre
        holder.correoTextView.text = usuario.correo

        holder.itemView.setOnClickListener {
            onItemClick(usuario)
        }
    }

    override fun getItemCount(): Int = usuarios.size

    // Función para actualizar la lista de usuarios (opcional si usas listas mutables)
    fun setUsuarios(nuevosUsuarios: List<Usuario>) {
        (usuarios as MutableList).clear()
        usuarios.addAll(nuevosUsuarios)
        notifyDataSetChanged()
    }
}
