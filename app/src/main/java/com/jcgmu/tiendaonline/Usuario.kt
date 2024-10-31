// Usuario.kt
package com.jcgmu.tiendaonline

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val correo: String,
    val contrasena: String,
    var rol: String,
    val latitud: Double?,    // Nuevo campo
    val longitud: Double?    // Nuevo campo
)
