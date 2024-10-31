package com.jcgmu.tiendaonline

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jcgmu.tiendaonline.databinding.ActivityUsuariosMapaBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsuariosMapaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityUsuariosMapaBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsuariosMapaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.obtenerBaseDatos(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        cargarUsuariosEnMapa()
    }

    private fun cargarUsuariosEnMapa() {
        lifecycleScope.launch {
            val usuarios = withContext(Dispatchers.IO) {
                db.usuarioDao().obtenerUsuariosConUbicacion()
            }

            if (usuarios.isNotEmpty()) {
                for (usuario in usuarios) {
                    val latitud = usuario.latitud
                    val longitud = usuario.longitud
                    if (latitud != null && longitud != null) {
                        val posicion = LatLng(latitud, longitud)
                        map.addMarker(
                            MarkerOptions()
                                .position(posicion)
                                .title(usuario.nombre)
                                .snippet("Correo: ${usuario.correo}")
                        )
                    }
                }
                // Opcional: Centrar el mapa en la primera ubicación
                val primerUsuario = usuarios.first()
                if (primerUsuario.latitud != null && primerUsuario.longitud != null) {
                    val primeraPosicion = LatLng(primerUsuario.latitud, primerUsuario.longitud)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(primeraPosicion, 10f))
                }
            } else {
                Toast.makeText(this@UsuariosMapaActivity, "No hay usuarios con ubicación registrada", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
