package com.pabloat.hotelperikero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.rememberNavController
import com.pabloat.hotelperikero.data.VideojuegoRepository
import com.pabloat.hotelperikero.data.local.AppDataBase
import com.pabloat.hotelperikero.data.local.VideojuegoDatasource
import com.pabloat.hotelperikero.data.remote.RemoteVideojuegoDataSource
import com.pabloat.hotelperikero.data.remote.RetrofitBuilder
import com.pabloat.hotelperikero.navigation.MainNavigation
import com.pabloat.hotelperikero.navigation.MainTopBar
import com.pabloat.hotelperikero.ui.util.NavigationBottomBar
import com.pabloat.hotelperikero.viewmodel.FireBaseViewModel
import com.pabloat.hotelperikero.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                MainApp()
            }

        }
    }
}

@Composable
fun MainApp() {
    val navHostController = rememberNavController()
    val remoteDatasource = RemoteVideojuegoDataSource(RetrofitBuilder.apiService)
    val localDatasource = VideojuegoDatasource(LocalContext.current)
    val context = LocalContext.current
    val repository = VideojuegoRepository(localDatasource, remoteDatasource)
    val mainViewModel = MainViewModel(repository)
    val firebaseViewModel = FireBaseViewModel()
    AppDataBase.getDatabase(LocalContext.current)
    mainViewModel.getRemoteVideojuego()
    val background: Painter = painterResource(id = R.drawable.inicio)

    Scaffold(topBar = { MainTopBar() }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.weight(1f)) {
                Image(
                    painter = background,
                    contentDescription = "background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                MainNavigation(
                    onNavController = navHostController,
                    mainViewmodel = mainViewModel,
                    fireBaseViewModel = firebaseViewModel,
                    context = context
                )
            }
            NavigationBottomBar(navHostController)
        }
    }
}