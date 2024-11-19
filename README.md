
# TiendaOnline

Este proyecto es una aplicación de tienda en línea desarrollada en Android. La aplicación permite a los usuarios explorar productos, agregar al carrito, realizar compras y ver el historial de compras. Los administradores pueden gestionar productos y usuarios.

## Características
- Inicio de sesión de usuario y administrador.
- Visualización de productos y detalles.
- Carrito de compras y flujo de compra con integración de pagos mediante ePayco.
- Gestión de productos y usuarios para administradores.
- Historial de compras.
- Geolocalización de usuarios en el mapa.
- Uso de Material Design para una interfaz moderna y funcional.

## Cambios Recientes
### 18 de noviembre de 2024
1. **Corrección de errores en el uso de ePayco:**
   - Se corrigieron referencias a métodos inexistentes en la clase `Card` del SDK de ePayco.
   - Se implementó el método `setHasCvv(true)` para la validación del código de seguridad (CVC).

2. **Validaciones y flujo de pago mejorado:**
   - Se añadieron validaciones para los campos del formulario de pago, asegurando que los datos de la tarjeta sean completos antes de procesar el pago.
   - Se manejaron excepciones para mostrar mensajes de error claros en caso de fallos durante la creación de tokens o cobros.

3. **Actualización de dependencias:**
   - Se confirmó el uso de la versión `v3.13.0` del SDK de ePayco.

4. **Documentación del uso de claves:**
   - Se incluyó una advertencia para que los usuarios configuren sus propias claves de API (`API_KEY` y `PRIVATE_KEY`) en el archivo de configuración.

5. **Mejoras en el flujo del carrito de compras:**
   - Al eliminar todos los productos del carrito, la aplicación regresa automáticamente al listado de productos.

## Requisitos previos
- Android Studio instalado en su sistema.
- Claves propias de API para ePayco (`API_KEY` y `PRIVATE_KEY`).
- API key de Google Maps configurada en el archivo `AndroidManifest.xml`.

## Instalación
Para clonar y ejecutar este proyecto en su máquina local, siga estos pasos:

### Clonar el repositorio
```bash
git clone https://github.com/jcgmU/TiendaOnline.git
```

### Abrir el proyecto en Android Studio
1. Abra Android Studio.
2. Vaya a `File > Open` y seleccione la carpeta del proyecto `TiendaOnline`.
3. Permita que Android Studio sincronice el proyecto.

### Configurar Google Maps API Key
1. En el archivo `AndroidManifest.xml`, reemplace el valor de `YOUR_GOOGLE_MAPS_API_KEY` con su propia clave de API de Google Maps.
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_GOOGLE_MAPS_API_KEY" />
```

### Configurar Claves de ePayco
1. En el archivo `CarritoActivity.kt`, configure las claves públicas y privadas propias en el bloque de autenticación:
```kotlin
val auth = Authentication().apply {
    apiKey = "YOUR_PUBLIC_API_KEY"
    privateKey = "YOUR_PRIVATE_API_KEY"
    lang = "ES"
    test = true
}
```

### Ejecutar la aplicación
1. Conecte un dispositivo Android o inicie un emulador en Android Studio.
2. Haga clic en el botón `Run` o use el atajo `Shift + F10` para compilar y ejecutar la aplicación.

## Credenciales del administrador
El usuario administrador se debe inicializar al iniciar la aplicación. Estas credenciales son necesarias para probar funcionalidades como la cámara y agregar productos:
- **Correo:** admin@tiendaonline.com
- **Password:** admin123

## Estructura del Proyecto
- `app/src/main/java/com/jcgmu/tiendaonline/` - Contiene el código fuente del proyecto.
- `app/src/main/res/` - Contiene los recursos del proyecto, incluyendo layouts, strings y estilos.
- `AndroidManifest.xml` - Archivo de manifiesto donde se configuran los permisos y componentes.

## Contribuciones
Si desea contribuir a este proyecto, haga un fork del repositorio y cree una nueva rama para sus características o correcciones. Luego envíe un pull request con una descripción detallada de los cambios.

## Licencia
Este proyecto está bajo la Licencia de este repositorio individual.

## Contacto
Si tiene preguntas o necesita soporte adicional, puede contactarme en juancamilogarcia@ucompensar.edu.co o crear un issue en el repositorio.
