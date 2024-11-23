package com.jcgmu.tiendaonline

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CarritoManager {

    private const val SHARED_PREFS_KEY = "carrito_prefs"
    private const val CARRITO_KEY = "carrito"

    private var productosEnCarrito: MutableList<Producto> = mutableListOf()

    // Agrega un producto al carrito
    fun agregarProducto(context: Context, producto: Producto) {
        val existente = productosEnCarrito.find { it.id == producto.id }
        if (existente != null) {
            existente.cantidad += producto.cantidad // Incrementa la cantidad
        } else {
            productosEnCarrito.add(producto)
        }
        guardarCarrito(context)
    }

    // Elimina un producto del carrito
    fun eliminarProducto(context: Context, producto: Producto) {
        productosEnCarrito.removeIf { it.id == producto.id }
        guardarCarrito(context)
    }

    // Obtiene la lista de productos del carrito
    fun obtenerProductos(): List<Producto> {
        return productosEnCarrito
    }

    // Calcula el total a pagar
    fun calcularTotal(): Double {
        return productosEnCarrito.sumOf { it.precio * it.cantidad }
    }

    // Limpia todos los productos del carrito
    fun limpiarCarrito(context: Context) {
        productosEnCarrito.clear()
        guardarCarrito(context)
    }

    // Carga el carrito desde las SharedPreferences
    fun cargarCarrito(context: Context) {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPrefs.getString(CARRITO_KEY, null)
        if (json != null) {
            val type = object : TypeToken<MutableList<Producto>>() {}.type
            productosEnCarrito = gson.fromJson(json, type)
        }
    }

    // Guarda el carrito en las SharedPreferences
    private fun guardarCarrito(context: Context) {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(productosEnCarrito)
        editor.putString(CARRITO_KEY, json)
        editor.apply()
    }

    // Actualiza la cantidad de un producto en el carrito
    fun actualizarCantidad(context: Context, productoId: Int, nuevaCantidad: Int) {
        val producto = productosEnCarrito.find { it.id == productoId }
        if (producto != null) {
            producto.cantidad = nuevaCantidad
            if (producto.cantidad <= 0) {
                eliminarProducto(context, producto)
            } else {
                guardarCarrito(context)
            }
        }
    }
}
