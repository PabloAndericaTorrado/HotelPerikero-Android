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
    val remoteDatasource = RemoteHotelDataSource(RetrofitBuilder.apiService)
    val localDatasource = HotelDatasource(LocalContext.current)
    val context = LocalContext.current
    val repository = HotelRepository(localDatasource, remoteDatasource)
    val mainViewModel = HotelViewModel(repository)
    AppDataBase.getDatabase(LocalContext.current)
    mainViewModel.getRemoteHabitacion()
    val habitaciones by mainViewModel.habitaciones.collectAsState()
    HabitacionesList(habitaciones =  habitaciones)
}


@Composable
fun HabitacionesList(habitaciones: List<Habitacion>) {
    LazyColumn {
        items(habitaciones) { habitacion ->
            HabitacionItem(habitacion = habitacion)

        }
    }
}

@Composable
fun HabitacionItem(habitacion: Habitacion) {
    Text(text = "Número de habitación: ${habitacion.numero_habitacion}")
    Text(text = "Tipo: ${habitacion.tipo}")
    Text(text = "Precio: ${habitacion.precio}")
    Text(text = "Descripción: ${habitacion.descripcion}")
    Text(text = "Capacidad: ${habitacion.capacidad}")
    Text(text = "Características: ${habitacion.caracteristicas}")
    Text(text = "Disponibilidad: ${habitacion.disponibilidad}")
    Text(text = "Creado en: ${habitacion.created_at}")
    Text(text = "Actualizado en: ${habitacion.updated_at}")
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