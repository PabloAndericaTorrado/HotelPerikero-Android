package com.pabloat.GameHubConnect.ui.views
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pabloat.GameHubConnect.R
import com.pabloat.GameHubConnect.navigation.Destinations
import com.pabloat.GameHubConnect.ui.util.CategoryCard
import com.pabloat.GameHubConnect.ui.util.WelcomeSection
import com.pabloat.GameHubConnect.viewmodel.FireBaseViewModel

/**
 * InitScreen
 * Pantalla de inicio de la aplicación, con opciones para explorar los videojuegos, o en caso
 * de que seas administrador, poder entrar al menú de opciones del CRUD.
 */
@Composable
fun InitScreen(onNavController: NavHostController, firebaseViewModel: FireBaseViewModel) {
    val background: Painter = painterResource(id = R.drawable.inicio)
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) { visible = true }

    Surface(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = background,
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WelcomeSection(visible)
            Spacer(modifier = Modifier.weight(1f))
            CategoryCard(title = "¡Explorar!", icon = Icons.Default.Gamepad, onNavigate = { onNavController.navigate(Destinations.GenreScreen.route) })
            Spacer(modifier = Modifier.height(12.dp))
            // Aqui, si el usuario es el administrador, se muestra la opción de administrar videojuegos.
            if (firebaseViewModel.getStoredEmail() == "admin@admin.com") {
                CategoryCard(title = "Administrar videojuegos", icon = Icons.Default.Settings, onNavigate = { onNavController.navigate(Destinations.ManageScreen.route) })
            }
        }
    }
}

