
# Documentación ANDROID

El proyecto **Hotel Perikero - ANDROID** es una aplicación móvil para la gestión de reservas de habitaciones y espacios en un hotel. Está desarrollada usando Android Jetpack Compose y Kotlin, proporcionando una interfaz de usuario moderna y fluida.

## Estructura del Proyecto

El proyecto sigue una arquitectura MVVM (Model-View-ViewModel) que separa claramente la lógica de negocio, la lógica de presentación y la interfaz de usuario.` 

![enter image description here](https://files.oaiusercontent.com/file-k4aVkSlZK6LGjfyyBReaTd84?se=2024-05-22T08:53:19Z&sp=r&sv=2023-11-03&sr=b&rscc=max-age=299,%20immutable&rscd=attachment;%20filename=image.png&sig=B0KsDo5XhHqYJGTsuqWD03w/QKTol6PAOiDMa8v5bTo=)



## Requisitos

- Android Studio Bumblebee o superior
- JDK 11
- Gradle 7.0.2 o superior

## Componentes Principales

### ViewModel

El `HotelViewModel` gestiona la lógica de negocio y los datos de la aplicación. Utiliza `MutableStateFlow` para gestionar el estado de los datos del usuario y las reservas. Se encarga de manejar la autenticación del usuario, las reservas de habitaciones y espacios, y proporciona datos a las diferentes pantallas de la aplicación.

### Navegación

La navegación se gestiona utilizando `NavHostController` y una clase `Destinations` que define todas las rutas de la aplicación. Esto permite una navegación estructurada y clara entre las diferentes pantallas de la aplicación.

## Pantallas

Las pantallas principales incluyen:

- **MainScreen:** Pantalla principal de la aplicación donde se muestran las opciones principales.
- **LoginScreen:** Pantalla para el inicio de sesión del usuario.
- **ProfileScreen:** Pantalla que muestra los detalles del perfil del usuario.
- **ReservationFormScreen:** Pantalla para realizar reservas de habitaciones.
- **ReservationEspaciosFormScreen:** Pantalla para realizar reservas de espacios del hotel.
- **ContactScreen:** Pantalla que muestra la información de contacto del hotel.
- **EspacioDetalleScreen:** Pantalla que muestra los detalles de un espacio específico.
- **EspaciosScreen:** Pantalla que lista todos los espacios disponibles en el hotel.
- **HabitacionDetalleScreen:** Pantalla que muestra los detalles de una habitación específica.
- **HabitacionesScreen:** Pantalla que lista todas las habitaciones disponibles en el hotel.
- **ReseniasScreen:** Pantalla que muestra las reseñas de los clientes.
- **ServiciosScreen:** Pantalla que lista todos los servicios disponibles en el hotel.
- **ServiciosEventosScreen:** Pantalla específica para la gestión de servicios relacionados con eventos.

## Entidades

Las entidades representan las estructuras de datos principales de la aplicación, como `Reserva`, `Espacio`, `Habitación`, y `Usuario`. Estas entidades se almacenan en la base de datos local y se sincronizan con el servidor remoto.

## Funcionalidades

### Inicio de Sesión

La funcionalidad de inicio de sesión permite a los usuarios autenticarse utilizando su correo electrónico y contraseña. Si las credenciales son correctas, los datos del usuario se almacenan en el ViewModel y se redirige al usuario a la pantalla principal.

### Perfil de Usuario

La pantalla de perfil muestra los detalles del usuario autenticado, como nombre, correo electrónico, teléfono, ciudad y dirección. Si el usuario no está autenticado, se muestra una opción para iniciar sesión.

### Reserva de Habitaciones

Los usuarios pueden buscar y reservar habitaciones disponibles. La pantalla de reserva permite seleccionar las fechas de check-in y check-out, el número de personas y muestra el precio total de la reserva.

### Reserva de Espacios

Similar a la reserva de habitaciones, los usuarios pueden reservar espacios del hotel para eventos. La pantalla de reserva de espacios permite seleccionar las fechas y horas de inicio y fin, el número de personas y muestra el precio total de la reserva.

## Guía de Estilo

El proyecto sigue las convenciones de código de Kotlin y Android Jetpack Compose. Se recomienda seguir las mejores prácticas de diseño de interfaces y mantener la consistencia en el uso de temas y estilos.``

## Video de demostración

[![Demostración](https://img.youtube.com/vi/VIDEO_ID/maxresdefault.jpg)](https://drive.google.com/file/d/1WKIFt7YklbNfwyxvufaveVwgMuRL6nBh/view?usp=sharing)

