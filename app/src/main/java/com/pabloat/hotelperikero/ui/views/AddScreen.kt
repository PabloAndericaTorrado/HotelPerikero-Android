package com.pabloat.GameHubConnect.ui.views

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pabloat.GameHubConnect.data.local.Videojuego
import com.pabloat.GameHubConnect.viewmodel.MainViewModel


/**
 * AddScreen es una función componible que representa la pantalla donde el usuario puede agregar un nuevo juego a la base de datos.
 * @param navHostController es el controlador de navegación que gestiona la navegación dentro de la aplicación.
 * @param mainViewModel es el ViewModel que contiene la lógica de la aplicación.
 */

@Composable
fun AddScreen(navHostController: NavHostController, mainViewModel: MainViewModel) {
    val gameName = remember { mutableStateOf("") }
    val gameDescription = remember { mutableStateOf("") }
    val gameGenre = remember { mutableStateOf("") }
    val gameImage = remember { mutableStateOf("") }
    val gameDeveloper = remember { mutableStateOf("") }
    val gamePlatform = remember { mutableStateOf("") }
    val gameDate = remember { mutableStateOf("") }
    val gameRate = remember { mutableFloatStateOf(0.0f) }
    val showSnackbar = remember { mutableStateOf(false) }
    val videojuego = Videojuego(
        id = null,
        title = gameName.value,
        thumbnail = gameImage.value,
        developer = gameDeveloper.value,
        shortDescription = gameDescription.value,
        genre = gameGenre.value,
        platform = gamePlatform.value,
        date = gameDate.value,
        valoracion = gameRate.value
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CuadroTexto(value = gameName, label = "Nombre del juego")
        CuadroTexto(value = gameImage, label = "Imagen del juego")
        CuadroTexto(value = gameDeveloper, label = "Desarrollador del juego")
        CuadroTexto(value = gameDescription, label = "Descripción del juego")
        CuadroTexto(value = gameGenre, label = "Género del juego")
        CuadroTexto(value = gamePlatform, label = "Plataforma del juego")
        CuadroTexto(value = gameDate, label = "Fecha de salida del juego")
        Button(
            onClick = {
                mainViewModel.addGame(videojuego)
                Log.d("VM", "Pulsamos el boton de añadir juego")
                showSnackbar.value = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Añadir juego")
        }
        MostrarSnackbar(showSnackbar = showSnackbar)
    }
}

@Composable
fun CuadroTexto(value: MutableState<String>, label: String) {
    OutlinedTextField(
        value = value.value,
        onValueChange = { value.value = it },
        label = { Text(label, color = Color.White) },
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(color = Color.White)
    )

    Spacer(modifier = Modifier.height(16.dp))
}
@Composable
private fun MostrarSnackbar(showSnackbar: MutableState<Boolean>) {
    if (showSnackbar.value) {
        Snackbar(
            action = {
                TextButton(onClick = { showSnackbar.value = false }) {
                    Text("Cerrar")
                }
            }
        ) {
            Text("El videojuego se ha añadido correctamente")
        }
    }
}