package com.jcgmu.tiendaonline

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration

@Database(entities = [Usuario::class, Producto::class, Compra::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class) // Referencia a la clase, no una instancia
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao
    abstract fun compraDao(): CompraDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Migración de la versión 2 a la 3
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Crear la tabla 'compras'
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `compras` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `usuarioId` INTEGER NOT NULL,
                        `productoId` INTEGER NOT NULL,
                        `fecha` INTEGER NOT NULL,
                        FOREIGN KEY(`usuarioId`) REFERENCES `usuarios`(`id`) ON DELETE CASCADE,
                        FOREIGN KEY(`productoId`) REFERENCES `productos`(`id`) ON DELETE CASCADE
                    )
                """.trimIndent())

                // Crear índices para 'usuarioId' y 'productoId'
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_compras_usuarioId` ON `compras` (`usuarioId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_compras_productoId` ON `compras` (`productoId`)")
            }
        }

        fun obtenerBaseDatos(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tienda_online_database"
                )
                    .addMigrations(MIGRATION_2_3) // Añadir migraciones
                    //.fallbackToDestructiveMigration() // Opcional: comenta o elimina esta línea
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Insertar el usuario administrador aquí
            db.execSQL(
                "INSERT INTO usuarios (id, nombre, correo, contrasena, rol, latitud, longitud) " +
                        "VALUES (1, 'Administrador', 'admin@tiendaonline.com', 'admin123', 'admin', NULL, NULL)"
            )
        }
    }
}
