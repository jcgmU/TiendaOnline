package com.jcgmu.tiendaonline

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.OutputStream

class EditarProductoActivity : AppCompatActivity() {

    private lateinit var nombreEditText: EditText
    private lateinit var descripcionEditText: EditText
    private lateinit var precioEditText: EditText
    private lateinit var imagenImageView: ImageView
    private lateinit var seleccionarImagenButton: Button
    private lateinit var guardarProductoButton: Button

    private var imagenUri: Uri? = null
    private var productoId: Int = 0

    private lateinit var db: AppDatabase
    private var producto: Producto? = null

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            imagenUri = guardarImagenEnGaleria(it)
            imagenImageView.setImageBitmap(it)
        }
    }

    private val pickPhotoLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imagenUri = it
            imagenImageView.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_producto)

        nombreEditText = findViewById(R.id.nombreProductoEditText)
        descripcionEditText = findViewById(R.id.descripcionProductoEditText)
        precioEditText = findViewById(R.id.precioProductoEditText)
        imagenImageView = findViewById(R.id.imagenProductoImageView)
        seleccionarImagenButton = findViewById(R.id.seleccionarImagenButton)
        guardarProductoButton = findViewById(R.id.guardarProductoButton)

        db = AppDatabase.obtenerBaseDatos(this)
        productoId = intent.getIntExtra("productoId", 0)

        if (productoId != 0) {
            cargarProducto(productoId)
        } else {
            Toast.makeText(this, "Error al cargar el producto", Toast.LENGTH_SHORT).show()
            finish()
        }

        seleccionarImagenButton.setOnClickListener { mostrarOpcionesImagen() }
        guardarProductoButton.setOnClickListener { actualizarProducto() }
    }

    private fun cargarProducto(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val productoDao = db.productoDao()
            producto = productoDao.obtenerProductoPorId(id)

            withContext(Dispatchers.Main) {
                producto?.let {
                    nombreEditText.setText(it.nombre)
                    descripcionEditText.setText(it.descripcion)
                    precioEditText.setText(it.precio.toString())
                    imagenUri = Uri.parse(it.imagenUri)
                    imagenImageView.setImageURI(imagenUri)
                } ?: run {
                    Toast.makeText(this@EditarProductoActivity, "Producto no encontrado", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
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
            put(MediaStore.Images.Media.DISPLAY_NAME, "titulo_imagen_${System.currentTimeMillis()}")
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

    private fun actualizarProducto() {
        val nombre = nombreEditText.text.toString()
        val descripcion = descripcionEditText.text.toString()
        val precioTexto = precioEditText.text.toString()

        if (nombre.isNotEmpty() && descripcion.isNotEmpty() && precioTexto.isNotEmpty() && imagenUri != null) {
            val precio = precioTexto.toDoubleOrNull()
            if (precio != null && producto != null) {
                val productoActualizado = Producto(
                    id = producto!!.id,
                    nombre = nombre,
                    descripcion = descripcion,
                    precio = precio,
                    imagenUri = imagenUri.toString()
                )
                CoroutineScope(Dispatchers.IO).launch {
                    db.productoDao().actualizarProducto(productoActualizado)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@EditarProductoActivity, "Producto actualizado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Precio inválido o producto no cargado", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
        }
    }
}
