
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
### 23 de noviembre de 2024
1. **Flujo mejorado para el pago y registro de compras:**
   - Se agregó funcionalidad para guardar automáticamente las transacciones exitosas en la base de datos después del pago.
   - Se limpió el carrito de compras tras registrar las transacciones en la base de datos, asegurando consistencia en el flujo.

2. **Corrección de errores en el historial de compras:**
   - Se corrigió un problema donde las compras no aparecían en el historial debido a un error en la consulta de datos.
   - Se añadieron logs en las actividades relacionadas para facilitar la depuración y detección de problemas.

3. **Manejo de errores mejorado:**
   - Se implementó una validación para identificar si el usuario está autenticado antes de registrar las compras.
   - Se mejoraron los mensajes de alerta en caso de fallos durante el registro de compras o consultas.

4. **Mejoras de interfaz:**
   - Ajustes en la visualización de productos en el historial de compras, mostrando el nombre del producto, cantidad y precio total en un diseño más claro y conciso.

### Cambios anteriores
- **18 de noviembre de 2024:** Mejoras en el flujo de pago, validaciones y corrección de errores en la integración con ePayco.

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
1. En el archivo `PaymentPageActivity.kt`, configure las claves públicas y privadas propias en el bloque de autenticación:
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

### Generar el APK y probar la app en un teléfono Android
1. **Configurar el entorno de compilación:**
   - Asegúrese de que Android Studio esté correctamente configurado con el SDK y las herramientas de compilación necesarias.
   - Conecte su cuenta de desarrollador de Google si planea distribuir la aplicación.

2. **Generar el APK:**
   - En Android Studio, vaya a `Build > Build Bundle(s) / APK(s) > Build APK(s)`.
   - Espere a que Android Studio compile el APK. Una vez finalizado, aparecerá una notificación con la ruta donde se generó el archivo APK.

3. **Ubicar el APK generado:**
   - Normalmente, el archivo APK se encuentra en la carpeta `app/build/outputs/apk/release/` o `app/build/outputs/apk/debug/` dependiendo de la configuración de compilación que eligió.

4. **Habilitar la instalación desde orígenes desconocidos:**
   - En su dispositivo Android, vaya a `Configuración > Seguridad > Fuentes desconocidas` y habilite la opción para permitir la instalación de aplicaciones desde fuera de la Play Store.

5. **Transferir el APK al dispositivo:**
   - Conecte su teléfono a su computadora mediante un cable USB.
   - Copie el archivo APK a una ubicación accesible en su dispositivo, como la carpeta `Descargas`.

6. **Instalar la aplicación:**
   - Use un explorador de archivos en su dispositivo para localizar el archivo APK.
   - Tóquelo para iniciar la instalación y siga las instrucciones en pantalla.

7. **Probar la aplicación:**
   - Una vez instalada, abra la aplicación desde el menú de aplicaciones.
   - Inicie sesión con las credenciales de prueba y verifique que todas las funcionalidades funcionen correctamente.
