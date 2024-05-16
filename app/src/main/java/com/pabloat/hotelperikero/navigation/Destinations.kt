package com.pabloat.hotelperikero.navigation

sealed class Destinations(val route: String) {
    object MainScreen : Destinations(route = "MainScreen")
    object ManageScreen : Destinations(route = "ManageScreen")
    object InitScreen : Destinations(route = "com.pabloat.GameHubConnect.ui.views.InitScreen")
    object HabitacionesScreen : Destinations(route = "HabitacionesScreen")
    object Contacto : Destinations(route = "ContactScreen")
    object ServiciosScreen : Destinations(route = "ServiciosScreen")
    object ReseniasScreen : Destinations(route = "ReseniasScreen")
    object EspaciosScreen : Destinations(route = "EspaciosScreen")
    object HabitacionDetalleScreen : Destinations(route = "HabitacionDetalleScreen")
    object EspacioDetalleScreen : Destinations("EspacioDetalleScreen")
    object LoginScreen : Destinations(route = "LoginScreen")
    object ReservationFormScreen : Destinations(route = "reservation_form/{habitacionId}/{userId}")
    object ServiciosEventosScreen : Destinations(route = "ServiciosEventosScreen")
    object ReservationEspaciosFormScreen :
        Destinations(route = "reservationEspacio_form/{espacioId}/{userId}")
    object ProfileScreen : Destinations(route = "ProfileScreen")

}
