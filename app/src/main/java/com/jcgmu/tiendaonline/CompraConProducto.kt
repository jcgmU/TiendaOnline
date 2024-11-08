package com.jcgmu.tiendaonline

import java.util.Date

data class CompraConProducto(
    val id: Int,
    val usuarioId: Int,
    val productoNombre: String,
    val cantidad: Int,
    val precioTotal: Double,
    val fecha: Long
)
