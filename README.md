📌 Proyecto Persistencia - Aplicación del Clima

📋 Descripción del Proyecto

Este proyecto es una aplicación de Android desarrollada con Jetpack Compose y Room Database para consultar y almacenar información meteorológica de distintas ciudades. Los datos se obtienen a través de la API de OpenWeatherMap y se gestionan con una base de datos local. Además, la aplicación incorpora inyección de dependencias con Dagger-Hilt.

🛠️ Tecnologías Utilizadas

Kotlin - Lenguaje de programación principal.

Jetpack Compose - Para el diseño de interfaces.

Room Database - Para el almacenamiento de ciudades guardadas.

Dagger-Hilt - Para la inyección de dependencias.

Retrofit - Para la obtención de datos desde la API de OpenWeatherMap.

LiveData y ViewModel - Para la gestión de estado.

🌟 Funcionalidades Principales

✅ Consultar el clima de una ciudad en tiempo real.
✅ Guardar ciudades en la base de datos local.
✅ Ver lista de ciudades guardadas con sus datos climáticos.
✅ Editar información de una ciudad.
✅ Actualizar el clima de una ciudad guardada.
✅ Eliminar una ciudad de la base de datos.
✅ Notificaciones visuales para operaciones exitosas y errores.

👥 Integrantes y Contribuciones

Robert George Vorobchevici

🔹 Ha clonado el proyecto y añadido las dependencias de Dagger-Hilt.
🔹 Creada la clase Application y el módulo de la base de datos.
🔹 Implementadas ciudades por defecto en la base de datos.
🔹 Junto con Sergio Benzal, identificó y solucionó el error que causaba el cierre inesperado de la app.

Carlos Gómez

🔹 Realizó cambios en ClimaViewModel para mejorar la gestión de datos.
🔹 Modificó la pantalla de navegación para optimizar la estructura del NavHost.
🔹 Creó el formulario de edición de ciudades y lo integró con la navegación.
🔹 Actualizó ListaCiudadesScreen para mejorar la UX y corregir errores visuales.

Sergio Benzal

🔹 No pudo realizar muchas modificaciones directas en el código.
🔹 Aportó en la búsqueda de información y documentación.
🔹 Colaboró con Robert en la solución de errores y revisó detalles finales con el equipo a través de Discord.
