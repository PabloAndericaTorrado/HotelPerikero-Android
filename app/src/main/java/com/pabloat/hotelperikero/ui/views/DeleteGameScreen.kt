package com.pabloat.GameHubConnect.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pabloat.GameHubConnect.data.local.Videojuego
import com.pabloat.GameHubConnect.viewmodel.MainViewModel
import com.pabloat.GameHubConnect.ui.util.BuscarJuegoTextField
import com.pabloat.GameHubConnect.ui.util.MostrarBotonEliminar
import com.pabloat.GameHubConnect.ui.util.MostrarSnackbar

/**
 * Esta screen muestra un buscador de videojuegos y un botón para eliminar para el videojuego que
 * busquemos en caso de que se encuentr.

 */
@Composable
fun DeleteGameScreen(mainViewModel: MainViewModel) {
    val texto = remember { mutableStateOf("") }
    val videojuegoEncontrado = remember { mutableStateOf<Videojuego?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val showSnackbar = remember { mutableStateOf(false) }

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BuscarJuegoTextField(texto, mainViewModel, coroutineScope, videojuegoEncontrado)
        MostrarBotonEliminar(videojuegoEncontrado, mainViewModel, coroutineScope, showSnackbar)
        MostrarSnackbar(showSnackbar = showSnackbar)
    }
}
