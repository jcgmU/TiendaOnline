package com.jcgmu.tiendaonline

import androidx.room.*

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuarios")
    suspend fun obtenerUsuarios(): List<Usuario>

    @Query("SELECT * FROM usuarios WHERE id = :usuarioId LIMIT 1")
    suspend fun obtenerUsuarioPorId(usuarioId: Int): Usuario?

    @Query("DELETE FROM usuarios WHERE id = :usuarioId")
    suspend fun eliminarUsuarioPorId(usuarioId: Int): Int

    @Update
    suspend fun actualizarUsuario(usuario: Usuario): Int

    // Método de login para validar correo y contraseña
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contrasena = :contrasena LIMIT 1")
    suspend fun login(correo: String, contrasena: String): Usuario?

    // Método para obtener usuarios con ubicación
    @Query("SELECT * FROM usuarios WHERE latitud IS NOT NULL AND longitud IS NOT NULL")
    suspend fun obtenerUsuariosConUbicacion(): List<Usuario>
}
