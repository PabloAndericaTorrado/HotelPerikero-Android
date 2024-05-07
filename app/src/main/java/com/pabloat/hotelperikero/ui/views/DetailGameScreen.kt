package com.pabloat.GameHubConnect.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.pabloat.GameHubConnect.data.local.Videojuego
import com.pabloat.GameHubConnect.ui.util.VideojuegoDetailCard
import com.pabloat.GameHubConnect.viewmodel.MainViewModel

/**
 * DetailGameScreen es la screen que muestra los detalles de un videojuego. Le llega el id del videojuego
 * seleccionado y muestra sus atributos en una tarjeta.
 */

@Composable
fun DetailGameScreen(onNavController: NavHostController, mainViewModel: MainViewModel){
    val selectedVideojuegoId by mainViewModel.selectedVideojuegoId.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val videojuegoState = remember(selectedVideojuegoId) {
        mutableStateOf<Videojuego?>(null)
    }

    LaunchedEffect(selectedVideojuegoId) {
        val videojuego = mainViewModel.getSelectedVideojuego(selectedVideojuegoId ?: -1)
        videojuegoState.value = videojuego
    }

    val videojuego = videojuegoState.value

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CustomSnackbarHost(snackbarHostState = snackbarHostState)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Control de la tarjeta de detalles del videojuego
            if (videojuego != null) {
                VideojuegoDetailCard(onNavController, videojuego = videojuego, mainViewModel, snackbarHostState, coroutineScope)
            } else {
                Text(text = "Cargando...")
            }
        }
    }
}

@Composable
fun CustomSnackbarHost(snackbarHostState: SnackbarHostState, modifier: Modifier = Modifier) {
    SnackbarHost(hostState = snackbarHostState, modifier = modifier) { data ->
        Snackbar(
            snackbarData = data,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            actionColor = MaterialTheme.colorScheme.primary
        )
    }
}