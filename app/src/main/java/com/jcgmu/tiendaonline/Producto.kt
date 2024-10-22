package com.jcgmu.tiendaonline

import java.io.Serializable

data class Producto(
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagenResId: Int
) : Serializable
