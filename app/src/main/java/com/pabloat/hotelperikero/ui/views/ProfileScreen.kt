package com.pabloat.GameHubConnect.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pabloat.GameHubConnect.R
import com.pabloat.GameHubConnect.navigation.Destinations
import com.pabloat.GameHubConnect.ui.util.isConnectedToInternet
import com.pabloat.GameHubConnect.viewmodel.FireBaseViewModel
import com.pabloat.GameHubConnect.viewmodel.MainViewModel
import com.pabloat.GameHubConnect.viewmodel.PreferenceUtils
/**
 * Pantalla de perfil de usuario.
 * @param onNavController Controlador de navegación para la navegación entre pantallas.
 * @param mainViewModel ViewModel principal para gestionar la lógica relacionada con los videojuegos.
 * @param fireBaseViewModel ViewModel para interactuar con Firebase Authentication.
 */
@Composable
fun ProfileScreen(
    onNavController: NavHostController,
    mainViewModel: MainViewModel,
    fireBaseViewModel: FireBaseViewModel
) {
    // Obtener el correo electrónico del usuario y el nombre de usuario
    val emailUser: String = fireBaseViewModel.getStoredEmail()
    val userName: String = emailUser.split("@").first()
    val preferencesUtils = PreferenceUtils()
    val context = LocalContext.current
    val rememberMeState = remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF293036)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar avatar del usuario
            val image: Painter = painterResource(id = R.drawable.user)
            Image(
                painter = image,
                contentDescription = "Avatar del usuario",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mostrar información del usuario dependiendo de si está conectado a Internet
            if (isConnectedToInternet(context)) {
                Text(
                    text = "Bienvenido, $userName",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )

                Text(
                    text = "Estás logueado como $emailUser",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))
            } else {
                Text(
                    text = "Bienvenido, En este momento estás offline",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )

                Text(
                    text = "Accediendo como usuario anónimo",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))
            }

            // Botón para cerrar sesión
            ElevatedButton(
                onClick = {
                    // Eliminar todos los juegos guardados y cerrar sesión
                    mainViewModel.deleteAllSavedGames()
                    rememberMeState.value = false
                    preferencesUtils.saveRememberMeState(false, context)
                    preferencesUtils.saveUserCredentials("", "", context)
                    fireBaseViewModel.signOut()
                    onNavController.navigate(Destinations.LoginScreen.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Cerrar sesión",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
