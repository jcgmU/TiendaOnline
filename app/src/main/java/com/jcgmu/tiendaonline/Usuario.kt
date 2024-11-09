package com.jcgmu.tiendaonline

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var nombre: String,
    var correo: String,
    val contrasena: String,
    val rol: String,
    var latitud: Double?,
    var longitud: Double?
)
