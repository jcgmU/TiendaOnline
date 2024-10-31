package com.jcgmu.tiendaonline
import com.jcgmu.tiendaonline.Usuario


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsuarioAdminAdapter(
    private val context: Context,
    private var usuarios: List<Usuario>,
    private val accionCallback: (Usuario, String) -> Unit
) : RecyclerView.Adapter<UsuarioAdminAdapter.UsuarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_usuario_admin, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.bind(usuario)
    }

    override fun getItemCount(): Int {
        return usuarios.size
    }

    // Método agregado para actualizar la lista de usuarios
    fun actualizarLista(nuevaLista: List<Usuario>) {
        usuarios = nuevaLista
        notifyDataSetChanged()
    }

    inner class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreTextView: TextView = itemView.findViewById(R.id.nombreUsuarioAdmin)
        private val correoTextView: TextView = itemView.findViewById(R.id.correoUsuarioAdmin)
        private val rolSpinner: Spinner = itemView.findViewById(R.id.rolUsuarioSpinner)
        private val eliminarButton: Button = itemView.findViewById(R.id.eliminarUsuarioButton)

        fun bind(usuario: Usuario) {
            nombreTextView.text = usuario.nombre
            correoTextView.text = usuario.correo

            val roles = arrayOf("user", "admin")
            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, roles)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            rolSpinner.adapter = adapter
            rolSpinner.setSelection(roles.indexOf(usuario.rol))

            rolSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val nuevoRol = roles[position]
                    if (usuario.rol != nuevoRol) {
                        usuario.rol = nuevoRol
                        val db = AppDatabase.obtenerBaseDatos(context)
                        CoroutineScope(Dispatchers.IO).launch {
                            db.usuarioDao().actualizarUsuario(usuario)
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

            eliminarButton.setOnClickListener {
                accionCallback(usuario, "eliminar")
            }
        }
    }
}
