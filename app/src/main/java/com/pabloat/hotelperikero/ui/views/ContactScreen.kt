package com.pabloat.hotelperikero.ui.views


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pabloat.hotelperikero.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ContactScreen(onNavController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.Gray,
                title = {
                    Text(
                        "¡Nuestro Contacto!",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                }
            )

        }
    ) { paddingValues ->
        Image(
            painter = painterResource(id = R.drawable.fondo_oscurecido),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ContactCard(
                title = "Nuestra Ubicación",
                details = "Perikero Hotel, C. Estébanez Calderón, 10\nMarbella, CP: 29602",
                icon = Icons.Filled.Place
            )
            ContactCard(
                title = "Información de Contacto",
                details = "Teléfono: +123 456 7890\nCorreo Electrónico: info@perikerohotel.com"
            )
            Image(
                painter = painterResource(id = R.drawable.map_image),
                contentDescription = "Map Image",
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )

        }
    }
}


@Composable
fun ContactCard(title: String, details: String, icon: ImageVector? = null) {
    Card(
        backgroundColor = Color.White,
        shape = RoundedCornerShape(12.dp), // Esquinas más redondeadas
        elevation = 4.dp, // Sombra sutil para profundidad
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp) // Espaciado vertical aumentado
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp) // Padding interno más generoso para un aspecto lujoso
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.Blue,
                    modifier = Modifier.size(50.dp) // Icono más grande para mayor presencia
                )
                Spacer(Modifier.width(20.dp))
            }
            Column {
                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold) // Texto más grande y en negrita
                Text(details, style = TextStyle(fontSize = 18.sp, color = Color.DarkGray)) // Color suavizado para el texto
            }
        }
    }
}

