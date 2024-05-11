package com.pabloat.hotelperikero.navigation

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.pabloat.hotelperikero.ui.views.ContactScreen
import com.pabloat.hotelperikero.ui.views.HabitacionesScreen
import com.pabloat.hotelperikero.ui.views.MainScreen
import com.pabloat.hotelperikero.ui.views.ServiciosScreen
import com.pabloat.hotelperikero.viewmodel.HotelViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainNavigation(
    onNavController: NavHostController,
    mainViewmodel: HotelViewModel,
    context: Context
) {


    val destinoInicial = Destinations.MainScreen.route
    val habitaciones = mainViewmodel.habitaciones.collectAsState().value
    val randomHabitaciones = mainViewmodel.fetchRandomRooms() // NO LA BORREIS SI SE USA

    NavHost(navController = onNavController, startDestination = destinoInicial) {
        composable(Destinations.MainScreen.route) {
            MainScreen(navHostController = onNavController,mainViewmodel)
        }

        composable(Destinations.Contacto.route){
            ContactScreen(onNavController)
        }

        composable(Destinations.HabitacionesScreen.route){
            HabitacionesScreen(habitaciones, navHostController = onNavController,mainViewmodel)
        }

        composable(Destinations.ServiciosScreen.route){
            ServiciosScreen(navHostController = onNavController,mainViewmodel)
        }
    }
}

