package com.jcgmu.tiendaonline

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagenUri: String,
    var cantidad: Int = 1 // Propiedad para gestionar la cantidad en el carrito
)
