package com.pabloat.GameHubConnect.navigation

sealed class Destinations(val route: String) {
    object MainScreen : Destinations(route = "MainScreen")
    object ManageScreen : Destinations(route = "ManageScreen")

    object GenreScreen : Destinations(route = "GenreScreen")

    object VideoGamesGenre : Destinations(route = "VideogamesGenreScreen/{genre}") {
        fun createRoute(genre: String): String {
            return "VideogamesGenreScreen/$genre"
        }
    }

    object AddScreen : Destinations(route = "AddScreen")


    object InitScreen : Destinations(route = "com.pabloat.GameHubConnect.ui.views.InitScreen")

    object DeleteGameScreen : Destinations(route = "DeleteGameScreen")

    object EditSearch : Destinations(route = "EditSearch")

    object EditScreen : Destinations(route = "EditScreen")

    object LoginScreen : Destinations(route = "LoginScreen")

    object DetailGameScreen : Destinations(route = "DetailGameScreen")

    object AddRatingScreen : Destinations(route = "AddRatingScreen")

    object ProfileScreen : Destinations(route = "com.pabloat.GameHubConnect.ui.views.ProfileScreen")

    object UserFavScreen : Destinations(route = "UserFavScreen")
}