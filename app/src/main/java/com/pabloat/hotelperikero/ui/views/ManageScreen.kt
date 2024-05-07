package com.pabloat.GameHubConnect.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pabloat.GameHubConnect.navigation.Destinations
import com.pabloat.GameHubConnect.ui.util.ActionButton

@Composable
fun ManageScreen(navHostController: NavHostController) {
    /**
     * Lista de acciones que se pueden realizar en la pantalla de gestión.
     * Cada acción tiene una etiqueta y una ruta a la que navegar cuando le damos clic.
     */
    val acciones = listOf(
        "Ver Juegos" to Destinations.MainScreen.route,
        "Editar Juego" to Destinations.EditSearch.route,
        "Añadir Juego" to Destinations.AddScreen.route,
        "Borrar Juego" to Destinations.DeleteGameScreen.route
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Gestión de Videojuegos", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp), color = Color.White)

        /**
         * Se crea un botón por cada acción que se puede realizar.
         * Cada botón navega a la ruta correspondiente.
         */
        acciones.forEach { (label, destination) ->
            ActionButton(label = label) { navHostController.navigate(destination) }
        }
    }
}
