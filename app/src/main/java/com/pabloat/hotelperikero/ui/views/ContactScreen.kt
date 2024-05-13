package com.pabloat.hotelperikero.ui.views


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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pabloat.hotelperikero.R

@Composable
fun ContactScreen(onNavController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Información de Contacto",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White, // Uso de colores de tema para coherencia
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
                .fillMaxSize()
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

