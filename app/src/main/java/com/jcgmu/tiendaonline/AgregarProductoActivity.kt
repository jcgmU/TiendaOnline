package com.jcgmu.tiendaonline

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.jcgmu.tiendaonline.databinding.ActivityAgregarProductoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.OutputStream

class AgregarProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarProductoBinding
    private var imagenUri: Uri? = null

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            imagenUri = guardarImagenEnGaleria(it)
            binding.imagenProductoImageView.setImageBitmap(it)
        }
    }

    private val pickPhotoLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imagenUri = it
            binding.imagenProductoImageView.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.seleccionarImagenButton.setOnClickListener { mostrarOpcionesImagen() }
        binding.guardarProductoButton.setOnClickListener { guardarProducto() }
    }

    private fun mostrarOpcionesImagen() {
        val opciones = arrayOf("Tomar foto", "Seleccionar de galería")
        AlertDialog.Builder(this).apply {
            setTitle("Seleccionar imagen")
            setItems(opciones) { _, which ->
                when (which) {
                    0 -> takePictureLauncher.launch(null)
                    1 -> pickPhotoLauncher.launch("image/*")
                }
            }
            show()
        }
    }

    private fun guardarImagenEnGaleria(bitmap: Bitmap): Uri {
        val resolver = contentResolver
        val imageCollection = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "imagen_producto_${System.currentTimeMillis()}")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/TiendaOnline")
        }

        val imageUri = resolver.insert(imageCollection, imageDetails)
        imageUri?.let { uri ->
            resolver.openOutputStream(uri)?.use { outputStream: OutputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            }
        }
        return imageUri ?: Uri.EMPTY
    }

    private fun guardarProducto() {
        val nombre = binding.nombreProductoEditText.text.toString()
        val descripcion = binding.descripcionProductoEditText.text.toString()
        val precioTexto = binding.precioProductoEditText.text.toString()

        if (nombre.isNotEmpty() && descripcion.isNotEmpty() && precioTexto.isNotEmpty() && imagenUri != null) {
            val precio = precioTexto.toDoubleOrNull()
            if (precio != null) {
                val nuevoProducto = Producto(
                    nombre = nombre,
                    descripcion = descripcion,
                    precio = precio,
                    imagenUri = imagenUri.toString()
                )
                val db = AppDatabase.obtenerBaseDatos(this)
                CoroutineScope(Dispatchers.IO).launch {
                    db.productoDao().insertarProducto(nuevoProducto)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AgregarProductoActivity, "Producto agregado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Precio inválido", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
        }
    }
}
