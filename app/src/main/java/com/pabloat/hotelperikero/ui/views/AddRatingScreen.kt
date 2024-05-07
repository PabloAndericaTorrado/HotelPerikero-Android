package com.pabloat.GameHubConnect.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pabloat.GameHubConnect.data.local.Videojuego
import com.pabloat.GameHubConnect.viewmodel.MainViewModel
import kotlin.math.roundToInt

/**
 * AddRatingScreen es una función componible que representa la pantalla donde el usuario puede agregar una valoración a un juego.
 * @param navHostController es el controlador de navegación que gestiona la navegación dentro de la aplicación.
 * @param mainViewModel es el ViewModel que contiene la lógica de la aplicación.
 * @param videojuego es el juego al que se le quiere agregar una valoración.
 */
@Composable
fun AddRatingScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel,
    videojuego: Videojuego
) {
    var gameRate by remember { mutableFloatStateOf(videojuego.valoracion) }
    var error by remember { mutableStateOf(false) }
    val errorColor by animateColorAsState(targetValue = if (error) Color.Red else Color.Transparent, animationSpec = tween(durationMillis = 500),
        label = ""
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = Color.Transparent
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Valoración del Juego: ${videojuego.title}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    SliderValoracion(
                        rating = gameRate,
                        onRatingChange = { newRating ->
                            gameRate = newRating
                            error = newRating !in 0.0..10.0
                        }
                    )

                    AnimatedVisibility(visible = error) {
                        Text(
                            text = "La valoración debe estar entre 0 y 10",
                            color = errorColor,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    GuardadoButton(
                        onClick = {
                            if (!error) {
                                mainViewModel.updateVideojuegoRating(videojuego.id, gameRate)
                                navHostController.navigateUp()
                            }
                        },
                        isEnabled = !error
                    )
                }
            }
        }
    }
}

@Composable
fun SliderValoracion(rating: Float, onRatingChange: (Float) -> Unit) {
    var sliderPosition by remember { mutableFloatStateOf(rating) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Slider(
            value = sliderPosition,
            onValueChange = { newValue ->
                sliderPosition = newValue
                onRatingChange(newValue)
            },
            valueRange = 0f..10f,
            steps = 9,
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        )
        Text(
            text = "${sliderPosition.roundToInt()}/10",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun GuardadoButton(onClick: () -> Unit, isEnabled: Boolean) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(CircleShape),
        colors = ButtonDefaults.buttonColors(containerColor = if (isEnabled) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
    ) {
        Text(text = "Guardar Valoración", color = MaterialTheme.colorScheme.onSecondary)
    }
}
