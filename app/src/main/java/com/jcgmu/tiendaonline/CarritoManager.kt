package com.jcgmu.tiendaonline

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CarritoManager {

    private const val SHARED_PREFS_KEY = "carrito_prefs"
    private const val CARRITO_KEY = "carrito"

    private var productosEnCarrito: MutableList<Producto> = mutableListOf()

    fun agregarProducto(context: Context, producto: Producto) {
        val existente = productosEnCarrito.find { it.id == producto.id }
        if (existente != null) {
            existente.cantidad++
        } else {
            productosEnCarrito.add(producto)
        }
        guardarCarrito(context)
    }

    fun eliminarUnidadProducto(context: Context, productoId: Int) {
        val producto = productosEnCarrito.find { it.id == productoId }
        if (producto != null) {
            if (producto.cantidad > 1) {
                producto.cantidad--
            } else {
                productosEnCarrito.remove(producto)
            }
            guardarCarrito(context)
        }
    }

    fun obtenerProductos(): List<Producto> {
        return productosEnCarrito
    }

    fun calcularTotal(): Double {
        return productosEnCarrito.sumOf { it.precio * it.cantidad }
    }

    fun cargarCarrito(context: Context) {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPrefs.getString(CARRITO_KEY, null)
        if (json != null) {
            val type = object : TypeToken<MutableList<Producto>>() {}.type
            productosEnCarrito = gson.fromJson(json, type)
        }
    }

    private fun guardarCarrito(context: Context) {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(productosEnCarrito)
        editor.putString(CARRITO_KEY, json)
        editor.apply()
    }
}
