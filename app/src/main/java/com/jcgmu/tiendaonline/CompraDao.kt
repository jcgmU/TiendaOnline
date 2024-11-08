package com.jcgmu.tiendaonline

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface CompraDao {



    @Query("SELECT * FROM compras WHERE usuarioId = :usuarioId ORDER BY fecha DESC")
    suspend fun obtenerComprasPorUsuario(usuarioId: Int): List<Compra>

    @Insert
    suspend fun insertarCompra(compra: Compra)

    @Update
    suspend fun actualizarCompra(compra: Compra)

    @Delete
    suspend fun eliminarCompra(compra: Compra)
}
