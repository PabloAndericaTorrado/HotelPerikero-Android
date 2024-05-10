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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.rememberNavController
import com.pabloat.hotelperikero.data.HotelRepository
import com.pabloat.hotelperikero.data.local.AppDataBase
import com.pabloat.hotelperikero.data.local.HotelDatasource
import com.pabloat.hotelperikero.data.remote.RemoteHotelDataSource
import com.pabloat.hotelperikero.data.remote.RetrofitBuilder
import com.pabloat.hotelperikero.navigation.MainNavigation
import com.pabloat.hotelperikero.navigation.MainTopBar
import com.pabloat.hotelperikero.viewmodel.HotelViewModel

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
    val context = LocalContext.current
    val appDatabase = remember { AppDataBase.getDatabase(context) }
    val remoteDataSource = remember { RemoteHotelDataSource(RetrofitBuilder.apiService) }
    val localDataSource = remember { HotelDatasource(context) }
    val repository = remember { HotelRepository(localDataSource, remoteDataSource) }
    val mainViewModel = HotelViewModel(repository)
    val navHostController = rememberNavController()

    // LaunchedEffect para cargar los datos solo una vez
    LaunchedEffect(true) {
        mainViewModel.fetchRooms()
        mainViewModel.fetchReservas()
        mainViewModel.fetchServicios()
        mainViewModel.fetchResenias()
    }

    Scaffold(topBar = { MainTopBar() }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.weight(1f)) {
                Image(
                    painter = painterResource(id = R.drawable.inicio),
                    contentDescription = "background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                MainNavigation(
                    onNavController = navHostController,
                    mainViewmodel = mainViewModel,
                    context = context)
            }
        }
    }


}

/*

    val habitacionesList by mainViewModel.habitaciones.collectAsState()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(habitacionesList) { habitacion ->
            HabitacionItem(habitacion)
        }
    }
}


 */
/*
@Composable
fun HabitacionItem(habitacion: Habitacion) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Número de habitación: ${habitacion.numero_habitacion}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Tipo: ${habitacion.tipo}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Precio: ${habitacion.precio}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Descripción: ${habitacion.descripcion}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Capacidad: ${habitacion.capacidad}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Características: ${habitacion.caracteristicas}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Disponibilidad: ${habitacion.disponibilidad}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Creado en: ${habitacion.created_at}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Actualizado en: ${habitacion.updated_at}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

 */

/*
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

 */