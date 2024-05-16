package com.pabloat.hotelperikero.ui.util


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
        NavigationItem(
            Icons.Filled.Home,
            "Inicio",
            "MainScreen"
        ),
        NavigationItem(Icons.Filled.Search, "Busqueda", "HabitacionesScreen"),
        NavigationItem(Icons.Filled.MeetingRoom, "Espacios", "EspaciosScreen"),
        NavigationItem(Icons.Filled.Event, "Reservas", ""),
        NavigationItem(
            Icons.Filled.AccountCircle,
            "Perfil",
            "com.pabloat.GameHubConnect.ui.views.ProfileScreen"
        )
    )

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        containerColor = Color(0xFA090F16),
        contentColor = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
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
        targetValue = if (selected) 1.2f else 1f,
        animationSpec = tween(durationMillis = 300), label = ""
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) Color.White else Color.LightGray,
        animationSpec = tween(durationMillis = 300), label = ""
    )
    val iconColor by animateColorAsState(
        targetValue = if (selected) Color.White else Color.LightGray,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    Column(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClick() }
            .scale(scale),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = textColor
        )
    }
}

data class NavigationItem(val icon: ImageVector, val label: String, val route: String)
