package com.pabloat.hotelperikero.ui.util

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun NavigationBottomBar(navHostController: NavHostController) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(
        NavigationItem(Icons.Filled.Home, "Inicio", "MainScreen"),
        NavigationItem(Icons.Filled.Search, "Busqueda", "HabitacionesScreen"),
        NavigationItem(Icons.Filled.MeetingRoom, "Espacios", "EspaciosScreen"),
        NavigationItem(Icons.Filled.Event, "Reservas", "UserReservationsScreen"),
        NavigationItem(Icons.Filled.AccountCircle, "Perfil", "ProfileScreen")
    )

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color(0xFF1A1C20)),
        containerColor = Color(0xFF1A1C20),
        contentColor = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                NavigationBottomBarItem(
                    icon = item.icon,
                    label = item.label,
                    selected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        navHostController.navigate(item.route)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun NavigationBottomBarItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.1f else 1f
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) Color.White else Color.Gray
    )
    val iconColor by animateColorAsState(
        targetValue = if (selected) Color(0xFF2A4B8D) else Color.Gray
    )

    Column(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClick() }
            .scale(scale),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .background(
                    if (selected) Color(0xFF2A4B8D).copy(alpha = 0.2f) else Color.Transparent,
                    shape = androidx.compose.foundation.shape.CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = textColor
        )
    }
}

data class NavigationItem(val icon: ImageVector, val label: String, val route: String)
