package com.pabloat.GameHubConnect.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pabloat.GameHubConnect.navigation.Destinations
import com.pabloat.GameHubConnect.ui.util.CustomTextField
import com.pabloat.GameHubConnect.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun EditSearch(onNavController: NavHostController, mainViewModel: MainViewModel) {
    var texto by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomTextField(value = texto, label = "Título", onValueChange = { texto = it })
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            coroutineScope.launch {
                val videojuego = mainViewModel.searchGame(texto)
                if (videojuego != null) {
                    mainViewModel.setSelectedVideojuego(videojuego)
                    onNavController.navigate(Destinations.EditScreen.route)
                }
            }
        }) {
            Text("Buscar juego")
        }
    }
}

