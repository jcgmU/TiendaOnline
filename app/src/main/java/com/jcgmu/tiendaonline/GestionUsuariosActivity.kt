package com.jcgmu.tiendaonline

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GestionUsuariosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adaptador: UsuarioAdminAdapter
    private var listaUsuarios: MutableList<Usuario> = mutableListOf()

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_usuarios) // Asegúrate de tener este layout

        recyclerView = findViewById(R.id.recyclerViewUsuarios) // Asegúrate de que el ID coincida
        recyclerView.layoutManager = LinearLayoutManager(this)
        adaptador = UsuarioAdminAdapter(listaUsuarios) { usuario ->
            actualizarLista(usuario)
        }
        recyclerView.adapter = adaptador

        db = AppDatabase.obtenerBaseDatos(this)

        cargarUsuarios()
    }

    private fun cargarUsuarios() {
        lifecycleScope.launch {
            val usuarios = withContext(Dispatchers.IO) {
                db.usuarioDao().obtenerTodosLosUsuarios() // Asegúrate de tener esta función en tu DAO
            }
            listaUsuarios.clear()
            listaUsuarios.addAll(usuarios)
            adaptador.notifyDataSetChanged()
        }
    }

    private fun actualizarLista(usuario: Usuario) {
        // Implementa la lógica para actualizar la lista de usuarios
        // Por ejemplo, eliminar al usuario de la base de datos y actualizar la lista
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.usuarioDao().eliminarUsuario(usuario.id) // Corrección aquí
            }
            Toast.makeText(this@GestionUsuariosActivity, "Usuario eliminado", Toast.LENGTH_SHORT).show()
            cargarUsuarios() // Recargar la lista después de eliminar
        }
    }
}
