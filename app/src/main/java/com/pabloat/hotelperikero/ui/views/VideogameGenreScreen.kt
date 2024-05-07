package com.pabloat.GameHubConnect.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pabloat.GameHubConnect.ui.util.VideojuegoCard
import com.pabloat.GameHubConnect.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun VideogameGenreScreen(onNavController: NavHostController, mainViewModel: MainViewModel, genre: String) {
    val videojuegosList by mainViewModel.getVideojuegosByGenre(genre = genre)
        .collectAsState(initial = emptyList())
    val busquedaVideojuego = remember { mutableStateOf("") }

    val videojuegosFiltrados =
        videojuegosList.filter { it.title.contains(busquedaVideojuego.value, ignoreCase = true) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = busquedaVideojuego.value,
                onValueChange = {
                    busquedaVideojuego.value = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                label = { Text("Buscar videojuegos", color = Color.White) },
                shape = RoundedCornerShape(16.dp),
                textStyle = TextStyle(color = Color.White),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White
                )
            )

            LazyColumn {
                items(videojuegosFiltrados) { videojuego ->
                    VideojuegoCard(onNavController, videojuego, mainViewModel)
                }
            }
        }
    }
}


