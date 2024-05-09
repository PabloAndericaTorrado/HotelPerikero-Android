package com.pabloat.hotelperikero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.pabloat.hotelperikero.data.HotelRepository
import com.pabloat.hotelperikero.data.local.AppDataBase
import com.pabloat.hotelperikero.data.local.Habitacion
import com.pabloat.hotelperikero.data.local.HotelDatasource
import com.pabloat.hotelperikero.data.remote.RemoteHotelDataSource
import com.pabloat.hotelperikero.data.remote.RetrofitBuilder
import com.pabloat.hotelperikero.viewmodel.HotelViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel

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

    // LaunchedEffect para cargar los datos solo una vez
    LaunchedEffect(true) {
        mainViewModel.fetchRooms()
    }



    val habitacionesList by mainViewModel.habitaciones.collectAsState()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(habitacionesList) { habitacion ->
            HabitacionItem(habitacion)
        }
    }
}

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