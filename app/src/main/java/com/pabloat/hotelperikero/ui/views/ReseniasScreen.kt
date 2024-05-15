package com.pabloat.hotelperikero.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pabloat.hotelperikero.R
import com.pabloat.hotelperikero.data.local.entities.Resenia
import com.pabloat.hotelperikero.viewmodel.HotelViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReseniasScreen(navHostController: NavHostController, mainViewModel: HotelViewModel) {
    val resenias = mainViewModel.resenias.collectAsState().value

    Box() {

        Scaffold(
            topBar = {
                androidx.compose.material.TopAppBar(
                    backgroundColor = Color.Gray,
                    title = {
                        Text(
                            "¡Nuestras Reseñas!",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navHostController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Regresar",
                                tint = Color.White
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->

            Image(   painter = painterResource(id = R.drawable.fondo1),
                contentDescription = "background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop)
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(resenias) { resenia ->
                    ReseniaCard(resenia)
                }
            }
        }
    }

}

@Composable
fun ReseniaCard(resenia: Resenia) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Reseña por Usuario ${resenia.usuario_id}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = resenia.comentario,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )
            EstrellasCalificacion(calificacion = resenia.calificacion)
        }
    }
}

@Composable
fun EstrellasCalificacion(calificacion: Int) {
    Row {
        repeat(5) { index ->
            Icon(
                imageVector = if (index < calificacion) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = null,  // Decorative element
                tint = if (index < calificacion) Color.Yellow else Color.Gray
            )
        }
    }
}
