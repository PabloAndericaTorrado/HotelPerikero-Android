package com.pabloat.GameHubConnect.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pabloat.GameHubConnect.ui.util.VideojuegoCard
import com.pabloat.GameHubConnect.viewmodel.MainViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@ExperimentalFoundationApi
@Composable
fun MainScreen(mainViewModel: MainViewModel, navHostController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /**
             * Aquí se almacena el estado de los videojuego del viewModel y se actualiza cada vez que cambia.
             * Se utiliza para mostrar la lista de videojuegos.
             */
            val videojuegosList by mainViewModel.videojuegos.collectAsState()
            LazyColumn(modifier = Modifier.weight(9f)) {
                items(videojuegosList) { videojuego ->
                    VideojuegoCard(navHostController,videojuego,mainViewModel)
                }
            }
        }
    }
}

