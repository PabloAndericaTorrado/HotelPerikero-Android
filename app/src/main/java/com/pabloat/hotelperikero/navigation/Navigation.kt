package com.pabloat.hotelperikero.navigation

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
/*
import com.pabloat.hotelperikero.ui.util.isConnectedToInternet
import com.pabloat.hotelperikero.ui.views.AddRatingScreen
import com.pabloat.hotelperikero.ui.views.AddScreen
import com.pabloat.hotelperikero.ui.views.DeleteGameScreen
import com.pabloat.hotelperikero.ui.views.DetailGameScreen
import com.pabloat.hotelperikero.ui.views.EditScreen
import com.pabloat.hotelperikero.ui.views.EditSearch
import com.pabloat.hotelperikero.ui.views.GenreScreen
import com.pabloat.hotelperikero.ui.views.InitScreen
import com.pabloat.hotelperikero.ui.views.LoginScreen
import com.pabloat.hotelperikero.ui.views.MainScreen
import com.pabloat.hotelperikero.ui.views.ManageScreen
import com.pabloat.hotelperikero.ui.views.ProfileScreen
import com.pabloat.hotelperikero.ui.views.UserFavScreen
import com.pabloat.hotelperikero.ui.views.VideogameGenreScreen
import com.pabloat.hotelperikero.viewmodel.FireBaseViewModel
import com.pabloat.hotelperikero.viewmodel.MainViewModel
import com.pabloat.hotelperikero.viewmodel.PreferenceUtils

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainNavigation(
    onNavController: NavHostController,
    mainViewmodel: MainViewModel,
    fireBaseViewModel: FireBaseViewModel,
    context: Context
) {
    val preferencesUtils = PreferenceUtils()

    val rememberMeState = preferencesUtils.getRememberMeState(context)


    if (isConnectedToInternet(context)) {
        LaunchedEffect(rememberMeState) {
            if (rememberMeState) {
                val savedEmail = preferencesUtils.getSavedEmail(context)
                val savedPassword = preferencesUtils.getSavedPassword(context)
                if (savedEmail != null && savedPassword != null) {
                    fireBaseViewModel.SingInWithEmailAndPassword(context, savedEmail, savedPassword,
                        home = {
                            if (fireBaseViewModel.getStoredEmail() == "admin@admin.com") {
                                onNavController.navigate(Destinations.ManageScreen.route)
                            } else {
                                onNavController.navigate(Destinations.InitScreen.route)
                            }
                        },
                        fail = {
                            onNavController.navigate(Destinations.LoginScreen.route)
                        }
                    )
                } else {
                    onNavController.navigate(Destinations.LoginScreen.route)
                }
            } else {
                onNavController.navigate(Destinations.LoginScreen.route)
            }
        }

    }

    val isConnected = isConnectedToInternet(context)
    val destinoInicial = if (isConnected) {
        Destinations.LoginScreen.route
    } else {
        Destinations.InitScreen.route
    }



    NavHost(navController = onNavController, startDestination = destinoInicial) {
        composable(Destinations.MainScreen.route) {
            MainScreen(mainViewmodel, navHostController = onNavController)
        }
        composable(Destinations.ManageScreen.route) {
            ManageScreen(navHostController = onNavController)
        }
        composable(Destinations.GenreScreen.route) {
            GenreScreen(
                mainViewModel = mainViewmodel
            ) { genre ->
                onNavController.navigate(Destinations.VideoGamesGenre.createRoute(genre))
            }
        }
        composable(Destinations.VideoGamesGenre.route) { backStackEntry ->
            val genre = backStackEntry.arguments?.getString("genre")
            if (genre != null) {
                VideogameGenreScreen(onNavController, mainViewModel = mainViewmodel, genre = genre)
            }
        }
        composable(Destinations.InitScreen.route) {
            InitScreen(onNavController = onNavController, fireBaseViewModel)
        }
        composable(Destinations.AddScreen.route) {
            AddScreen(navHostController = onNavController, mainViewModel = mainViewmodel)
        }

        composable(Destinations.DeleteGameScreen.route) {
            DeleteGameScreen(mainViewModel = mainViewmodel)
        }

        composable(Destinations.EditSearch.route) {
            EditSearch(onNavController = onNavController, mainViewModel = mainViewmodel)
        }
        composable(Destinations.EditScreen.route) {
            EditScreen(onNavController = onNavController, mainViewModel = mainViewmodel)
        }
        composable(Destinations.LoginScreen.route) {
            LoginScreen(navController = onNavController, fireBaseViewModel)
        }
        composable(Destinations.DetailGameScreen.route) {
            DetailGameScreen(onNavController, mainViewmodel)
        }

        composable(Destinations.AddRatingScreen.route) {
            AddRatingScreen(onNavController, mainViewmodel, mainViewmodel.getGame())
        }

        composable(Destinations.ProfileScreen.route){
            ProfileScreen(onNavController, mainViewmodel, fireBaseViewModel)
        }

        composable(Destinations.UserFavScreen.route){
            UserFavScreen(onNavController,mainViewmodel)
        }
    }
}

 */