package com.pabloat.hotelperikero.ui.views

//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import com.pabloat.hotelperikero.data.local.entities.Espacio
import com.pabloat.hotelperikero.viewmodel.HotelViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@ExperimentalFoundationApi
@Composable
fun EspaciosScreen(navHostController: NavHostController, mainViewModel: HotelViewModel) {
   val espacios = mainViewModel.espacios.collectAsState().value
    Scaffold(
        content = { padding ->
            Image(painter = painterResource(id = R.drawable.fondo_oscurecido),
                contentDescription = "background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop)
            Column(modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                androidx.compose.material3.Text(
                    "¡Nuestros Espacios Disponibles!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                EspacioList(espacios = espacios)
            }
        }
    )
}

@Composable
fun EspacioList(espacios: List<Espacio>) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(espacios) { espacio ->
            EspacioCard(espacio = espacio)
        }
    }
}

@Composable
fun EspacioCard(espacio: Espacio) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Column(Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = getEspacioImageResource(espacio.id)), // Implementar esta función para obtener recursos de imagen
                contentDescription = "Imagen del espacio ${espacio.nombre}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
            )
            Text(espacio.nombre, style = MaterialTheme.typography.bodyLarge)
            Text(espacio.descripcion, style = MaterialTheme.typography.bodySmall)
            Text("${espacio.precio}€ por hora", style = MaterialTheme.typography.bodySmall)
            if (espacio.disponible == 1) {
                Button(onClick = { /* Acción, por ejemplo, navegar a detalles */ }) {
                    Text("Reservar")
                }
            } else {
                Text("No disponible", color = Color.Red)
            }
        }
    }
}
