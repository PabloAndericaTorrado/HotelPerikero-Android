package com.pabloat.hotelperikero

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.pabloat.hotelperikero.data.HotelRepository
import com.pabloat.hotelperikero.data.local.AppDataBase
import com.pabloat.hotelperikero.data.local.HotelDatasource
import com.pabloat.hotelperikero.data.remote.RemoteHotelDataSource
import com.pabloat.hotelperikero.data.remote.RetrofitBuilder
import com.pabloat.hotelperikero.navigation.MainNavigation
import com.pabloat.hotelperikero.navigation.MainTopBar
import com.pabloat.hotelperikero.viewmodel.HotelViewModel
import com.pabloat.hotelperikero.viewmodel.HotelViewModelFactory

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainApp() {
    val context = LocalContext.current
    val appDatabase = remember { AppDataBase.getDatabase(context) }
    val remoteDataSource = remember { RemoteHotelDataSource(RetrofitBuilder.apiService) }
    val localDataSource = remember { HotelDatasource(context) }
    val repository = remember { HotelRepository(localDataSource, remoteDataSource) }
    val viewModelFactory = remember { HotelViewModelFactory(repository) }
    val mainViewModel: HotelViewModel = viewModel(factory = viewModelFactory)
    val navHostController = rememberNavController()

    Scaffold(topBar = { MainTopBar() }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.weight(1f)) {
                MainNavigation(
                    onNavController = navHostController,
                    mainViewmodel = mainViewModel,
                    context = context
                )
            }
        }
    }
}
