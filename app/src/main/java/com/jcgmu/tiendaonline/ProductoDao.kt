package com.jcgmu.tiendaonline

import androidx.room.*

@Dao
interface ProductoDao {

    @Query("SELECT * FROM productos")
    suspend fun obtenerProductos(): List<Producto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProducto(producto: Producto)

    @Update
    suspend fun actualizarProducto(producto: Producto)

    @Delete
    suspend fun eliminarProducto(producto: Producto)

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun obtenerProductoPorId(id: Int): Producto?
}
