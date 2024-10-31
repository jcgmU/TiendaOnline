package com.jcgmu.tiendaonline
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*

class GestionUsuariosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var usuarioAdapter: UsuarioAdminAdapter
    private var usuariosList: List<Usuario> = listOf()

    private val uiScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_usuarios)

        recyclerView = findViewById(R.id.recyclerViewUsuarios)
        recyclerView.layoutManager = LinearLayoutManager(this)

        db = AppDatabase.obtenerBaseDatos(this)

        // Cargar usuarios en segundo plano
        uiScope.launch {
            cargarUsuarios()
        }
    }

    private suspend fun cargarUsuarios() {
        withContext(Dispatchers.IO) {
            usuariosList = db.usuarioDao().obtenerUsuarios()
        }
        withContext(Dispatchers.Main) {
            if (::usuarioAdapter.isInitialized) {
                usuarioAdapter.actualizarLista(usuariosList)
            } else {
                usuarioAdapter = UsuarioAdminAdapter(this@GestionUsuariosActivity, usuariosList) { usuario, accion ->
                    when (accion) {
                        "eliminar" -> eliminarUsuario(usuario)
                    }
                }
                recyclerView.adapter = usuarioAdapter
            }
        }
    }

    private fun eliminarUsuario(usuario: Usuario) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                db.usuarioDao().eliminarUsuarioPorId(usuario.id)
            }
            Toast.makeText(this@GestionUsuariosActivity, "Usuario eliminado", Toast.LENGTH_SHORT).show()
            cargarUsuarios() // Volver a cargar la lista de usuarios
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        uiScope.cancel() // Cancelar la coroutine al destruir la actividad
    }
}
