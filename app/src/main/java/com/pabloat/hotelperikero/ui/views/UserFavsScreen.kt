package com.pabloat.GameHubConnect.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pabloat.GameHubConnect.ui.util.VideojuegoCard
import com.pabloat.GameHubConnect.viewmodel.MainViewModel

@Composable
fun UserFavScreen(onNavController: NavHostController, mainViewmodel: MainViewModel) {
    val savedGames by mainViewmodel.savedGames.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Text(
                text = "¡Tus videojuegos favoritos!",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier.padding(2.dp).align(alignment = Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (savedGames.isNotEmpty()) {
            LazyColumn {
                items(savedGames) { videojuego ->
                    VideojuegoCard(onNavController, videojuego, mainViewmodel)
                }
            }
        } else {
            Text(
                text = "Todavía no has guardado ningún videojuego.",
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
                modifier = Modifier.padding(16.dp).align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}