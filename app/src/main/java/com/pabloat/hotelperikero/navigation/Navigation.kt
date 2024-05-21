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
import com.pabloat.hotelperikero.data.local.dao.LocalReservaDao
import com.pabloat.hotelperikero.ui.views.ContactScreen
import com.pabloat.hotelperikero.ui.views.EspacioDetalleScreen
import com.pabloat.hotelperikero.ui.views.EspaciosScreen
import com.pabloat.hotelperikero.ui.views.HabitacionDetalleScreen
import com.pabloat.hotelperikero.ui.views.HabitacionesScreen
import com.pabloat.hotelperikero.ui.views.LoginScreen
import com.pabloat.hotelperikero.ui.views.MainScreen
import com.pabloat.hotelperikero.ui.views.ProfileScreen
import com.pabloat.hotelperikero.ui.views.ReseniasScreen
import com.pabloat.hotelperikero.ui.views.ReservationEspaciosFormScreen
import com.pabloat.hotelperikero.ui.views.ReservationFormScreen
import com.pabloat.hotelperikero.ui.views.ServiciosEventosScreen
import com.pabloat.hotelperikero.ui.views.ServiciosScreen
import com.pabloat.hotelperikero.ui.views.UserReservationsScreen
import com.pabloat.hotelperikero.viewmodel.HotelViewModel
import com.pabloat.hotelperikero.viewmodel.ReservaEventosViewModelFactory
import com.pabloat.hotelperikero.viewmodel.ReservaViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainNavigation(
    onNavController: NavHostController,
    mainViewmodel: HotelViewModel,
    context: Context,
    reservaViewModelFactory: ReservaViewModelFactory,
    reservaEventosViewModelFactory: ReservaEventosViewModelFactory
) {
    val destinoInicial = Destinations.MainScreen.route
    val idHabitacion = mainViewmodel.selectedHabitacionId.collectAsState().value
    val idEspacio = mainViewmodel.selectedespaciosID.collectAsState().value

    Log.d("MainNavigation", "idEspacio: $idEspacio")

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

        composable(Destinations.ServiciosEventosScreen.route) {
            ServiciosEventosScreen(navHostController = onNavController, mainViewmodel)
        }

        composable(Destinations.ReseniasScreen.route) {
            ReseniasScreen(navHostController = onNavController, mainViewmodel)
        }

        composable(Destinations.EspaciosScreen.route) {
            EspaciosScreen(navHostController = onNavController, mainViewmodel)
        }

        composable(Destinations.ProfileScreen.route) {
            ProfileScreen(navHostController = onNavController, mainViewmodel)
        }

        composable("reservation_form/{habitacionId}/{userId}") { backStackEntry ->
            val habitacionId = backStackEntry.arguments?.getString("habitacionId")?.toIntOrNull()
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
            if (habitacionId != null && userId != null) {
                ReservationFormScreen(
                    habitacionId = habitacionId,
                    userId = userId,
                    navHostController = onNavController,
                    viewModel = mainViewmodel,
                    reservaDao = reservaViewModelFactory.reservaDao
                )
            } else {
                // Manejar el caso de error aquí
            }
        }

        composable("reservationEspacio_form/{espacioId}/{userId}") { backStackEntry ->
            val espacioId = backStackEntry.arguments?.getString("espacioId")?.toIntOrNull()
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
            if (espacioId != null && userId != null) {
                ReservationEspaciosFormScreen(
                    espacioId = espacioId,
                    userId = userId,
                    navHostController = onNavController,
                    viewModel = mainViewmodel,
                    reservaEventosDao = reservaEventosViewModelFactory.reservaEventosDao
                )
            } else {
                // Manejar el caso de error aquí
            }
        }

        composable(Destinations.HabitacionDetalleScreen.route) {
            HabitacionDetalleScreen(
                navHostController = onNavController, viewModel = mainViewmodel,
                habitacionId = idHabitacion
            )
        }

        composable(Destinations.EspacioDetalleScreen.route) {
            EspacioDetalleScreen(
                navHostController = onNavController, viewModel = mainViewmodel,
                espacioId = 1
            )
        }

        composable(Destinations.UserReservationsScreen.route) {
            UserReservationsScreen(navHostController = onNavController, viewModel = mainViewmodel)
        }

    }
}
