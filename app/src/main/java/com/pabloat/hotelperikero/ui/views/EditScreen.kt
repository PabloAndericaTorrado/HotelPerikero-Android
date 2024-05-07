package com.pabloat.GameHubConnect.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pabloat.GameHubConnect.navigation.Destinations
import com.pabloat.GameHubConnect.ui.util.CustomTextField
import com.pabloat.GameHubConnect.ui.util.VolverAtrasButton
import com.pabloat.GameHubConnect.viewmodel.MainViewModel

/**
 * EditScreen
 * Pantalla de edición de videojuegos. Muestra los campos de texto con los datos del videojuego
 * seleccionado, y un botón para guardar los cambios.
 */
@Composable
fun EditScreen(onNavController: NavHostController, mainViewModel: MainViewModel) {
    val videojuego by mainViewModel.selectedVideojuego.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        videojuego?.let { juego ->
            var editedTitle by remember { mutableStateOf(juego.title) }
            CustomTextField(value = editedTitle, label = "Título", onValueChange = { editedTitle = it })

            var editedThumbnail by remember { mutableStateOf(juego.thumbnail) }
            CustomTextField(value = editedThumbnail, label = "Thumbnail", onValueChange = { editedThumbnail = it })

            var editedDeveloper by remember { mutableStateOf(juego.developer) }
            CustomTextField(value = editedDeveloper, label = "Desarrollador", onValueChange = { editedDeveloper = it })

            var editedDescription by remember { mutableStateOf(juego.shortDescription) }
            CustomTextField(value = editedDescription, label = "Descripción", onValueChange = { editedDescription = it })

            var editedGenre by remember { mutableStateOf(juego.genre) }
            CustomTextField(value = editedGenre, label = "Género", onValueChange = { editedGenre = it })

            Button(onClick = {
                mainViewModel.updateVideoGame(juego.copy(
                    title = editedTitle,
                    thumbnail = editedThumbnail,
                    developer = editedDeveloper,
                    shortDescription = editedDescription,
                    genre = editedGenre
                ))
                onNavController.navigate(Destinations.ManageScreen.route)
            }) {
                Text("Guardar cambios")
            }
            VolverAtrasButton(onNavController = onNavController, route = Destinations.ManageScreen.route, text = "Volver al Menú")
        }
    }
}