package com.pabloat.hotelperikero.ui.views


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.pabloat.hotelperikero.R

@Composable
fun ContactScreen(onNavController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFFAF8F0)), // Un fondo más cálido y suave
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Información de Contacto",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue, // Uso de colores de tema para coherencia
            modifier = Modifier.padding(bottom = 16.dp)
        )
        ContactCard(
            title = "Nuestra Ubicación",
            details = "Perikero Hotel, C. Estébanez Calderón, 10\nMarbella, CP: 29602",
            icon = Icons.Filled.Place // Icono más adecuado para la ubicación
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
                .clip(RoundedCornerShape(12.dp)) // Esquinas más redondeadas para una apariencia suave
        )
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

