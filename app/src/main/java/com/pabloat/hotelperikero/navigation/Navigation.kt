package com.pabloat.hotelperikero.navigation

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.pabloat.hotelperikero.ui.views.ContactScreen
import com.pabloat.hotelperikero.ui.views.EspaciosScreen
import com.pabloat.hotelperikero.ui.views.HabitacionDetalleScreen
import com.pabloat.hotelperikero.ui.views.HabitacionesScreen
import com.pabloat.hotelperikero.ui.views.LoginScreen
import com.pabloat.hotelperikero.ui.views.MainScreen
import com.pabloat.hotelperikero.ui.views.ReseniasScreen
import com.pabloat.hotelperikero.ui.views.ReservationFormScreen
import com.pabloat.hotelperikero.ui.views.ServiciosScreen
import com.pabloat.hotelperikero.viewmodel.HotelViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainNavigation(
    onNavController: NavHostController,
    mainViewmodel: HotelViewModel,
    context: Context
) {
    val destinoInicial = Destinations.MainScreen.route
    val id = mainViewmodel.selectedHabitacionId.collectAsState().value
    Log.d("Habitacion ID", "ID: $id")

    NavHost(navController = onNavController, startDestination = destinoInicial) {
        composable(Destinations.LoginScreen.route) {
            LoginScreen(navController = onNavController, mainViewmodel)
        }

        composable(Destinations.MainScreen.route) {
            MainScreen(navHostController = onNavController, mainViewModel = mainViewmodel)
        }

        composable(Destinations.Contacto.route) {
            ContactScreen(onNavController)
        }

        composable(Destinations.HabitacionesScreen.route) {
            HabitacionesScreen(navHostController = onNavController, mainViewmodel)
        }

        composable(Destinations.ServiciosScreen.route) {
            ServiciosScreen(navHostController = onNavController, mainViewmodel)
        }

        composable(Destinations.ReseniasScreen.route) {
            ReseniasScreen(navHostController = onNavController, mainViewmodel)
        }

        composable(Destinations.EspaciosScreen.route) {
            EspaciosScreen(navHostController = onNavController, mainViewmodel)
        }

        composable("reservation_form/{habitacionId}/{userId}") { backStackEntry ->
            val habitacionId = backStackEntry.arguments?.getString("habitacionId")?.toIntOrNull()
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
            if (habitacionId != null && userId != null) {
                ReservationFormScreen(
                    habitacionId = habitacionId,
                    userId = userId,
                    navHostController = onNavController,
                    viewModel = mainViewmodel
                )
            } else {
                // Manejar el caso de error aquí
            }
        }

        composable(Destinations.HabitacionDetalleScreen.route) {
            HabitacionDetalleScreen(
                navHostController = onNavController, viewModel = mainViewmodel,
                habitacionId = id
            )
        }
    }
}
