package com.pabloat.hotelperikero.navigation

sealed class Destinations(val route: String) {
    object MainScreen : Destinations(route = "MainScreen")
    object ManageScreen : Destinations(route = "ManageScreen")

    object InitScreen : Destinations(route = "com.pabloat.GameHubConnect.ui.views.InitScreen")


    object Contacto : Destinations(route = "ContactScreen")

}