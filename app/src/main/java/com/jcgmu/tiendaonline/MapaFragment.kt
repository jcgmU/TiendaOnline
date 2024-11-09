package com.jcgmu.tiendaonline

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jcgmu.tiendaonline.databinding.FragmentMapaBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapaFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapaBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMapaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = AppDatabase.obtenerBaseDatos(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        cargarUbicacionDelUsuario()
    }

    private fun cargarUbicacionDelUsuario() {
        lifecycleScope.launch {
            // Obtener el ID del usuario actual desde SharedPreferences
            val sharedPref = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val usuarioId = sharedPref.getInt("usuario_id", -1)

            if (usuarioId != -1) {
                val usuario = withContext(Dispatchers.IO) {
                    db.usuarioDao().obtenerUsuarioPorId(usuarioId)
                }

                if (usuario != null && usuario.latitud != null && usuario.longitud != null) {
                    val latitud = usuario.latitud!!
                    val longitud = usuario.longitud!!
                    val posicion = LatLng(latitud, longitud)
                    map.addMarker(
                        MarkerOptions()
                            .position(posicion)
                            .title(usuario.nombre)
                            .snippet("Correo: ${usuario.correo}")
                    )
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 15f))
                } else {
                    Toast.makeText(requireContext(), "No se encontró la ubicación del usuario", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(requireContext(), "Usuario no identificado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
