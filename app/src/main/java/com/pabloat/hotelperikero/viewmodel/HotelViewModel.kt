package com.pabloat.hotelperikero.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pabloat.hotelperikero.data.HotelRepository
import com.pabloat.hotelperikero.data.local.entities.Espacio
import com.pabloat.hotelperikero.data.local.entities.Habitacion
import com.pabloat.hotelperikero.data.local.entities.Resenia
import com.pabloat.hotelperikero.data.local.entities.Reserva
import com.pabloat.hotelperikero.data.local.entities.ReservaEventos
import com.pabloat.hotelperikero.data.local.entities.ReservaParking
import com.pabloat.hotelperikero.data.local.entities.ReservaParkingAnonimo
import com.pabloat.hotelperikero.data.local.entities.ReservaServicio
import com.pabloat.hotelperikero.data.local.entities.Servicio
import com.pabloat.hotelperikero.data.local.entities.ServicioEvento
import com.pabloat.hotelperikero.data.remote.dtos.LastIdResponse
import com.pabloat.hotelperikero.ui.util.CacheLists.Companion.cacheAnonParkingReservations
import com.pabloat.hotelperikero.ui.util.CacheLists.Companion.cacheEspacios
import com.pabloat.hotelperikero.ui.util.CacheLists.Companion.cacheHabitaciones
import com.pabloat.hotelperikero.ui.util.CacheLists.Companion.cacheParkingReservations
import com.pabloat.hotelperikero.ui.util.CacheLists.Companion.cacheRandomHabitaciones
import com.pabloat.hotelperikero.ui.util.CacheLists.Companion.cacheResenias
import com.pabloat.hotelperikero.ui.util.CacheLists.Companion.cacheReservaEventos
import com.pabloat.hotelperikero.ui.util.CacheLists.Companion.cacheReservas
import com.pabloat.hotelperikero.ui.util.CacheLists.Companion.cacheServiceReservations
import com.pabloat.hotelperikero.ui.util.CacheLists.Companion.cacheServicioEventos
import com.pabloat.hotelperikero.ui.util.CacheLists.Companion.cacheServicios
import com.pabloat.hotelperikero.ui.util.CacheLists.Companion.cacheUserEventReservations
import com.pabloat.hotelperikero.ui.util.CacheLists.Companion.cacheUserReservations
import com.pabloat.hotelperikero.ui.util.ScreenState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class HotelViewModel(private val repository: HotelRepository) : ViewModel() {
    val habitaciones: StateFlow<List<Habitacion>> = cacheHabitaciones.asStateFlow()
    val reservas: StateFlow<List<Reserva>> = cacheReservas.asStateFlow()
    val servicios: StateFlow<List<Servicio>> = cacheServicios.asStateFlow()
    val resenias: StateFlow<List<Resenia>> = cacheResenias.asStateFlow()
    val reservasEventos: StateFlow<List<ReservaEventos>> = cacheReservaEventos.asStateFlow()
    val serviciosEventos: StateFlow<List<ServicioEvento>> = cacheServicioEventos.asStateFlow()
    val espacios: StateFlow<List<Espacio>> = cacheEspacios.asStateFlow()
    val randomHabitaciones: StateFlow<List<Habitacion>> = cacheRandomHabitaciones.asStateFlow()
    val userReservations: StateFlow<List<Reserva>> = cacheUserReservations.asStateFlow()
    val userEventReservations: StateFlow<List<ReservaEventos>> =
        cacheUserEventReservations.asStateFlow()
    val serviceReservations: StateFlow<List<ReservaServicio>> =
        cacheServiceReservations.asStateFlow()
    val reservasParking: StateFlow<List<ReservaParking>> = cacheParkingReservations.asStateFlow()
    val reservasParkingAnonimo: StateFlow<List<ReservaParkingAnonimo>> =
        cacheAnonParkingReservations.asStateFlow()


    private val _uiState: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.Loading)
    val uiState: StateFlow<ScreenState> = _uiState.asStateFlow()

    private val handler = CoroutineExceptionHandler { _, exception ->
        _uiState.value = ScreenState.Error(exception.localizedMessage ?: "Unknown error")
    }

    init {
        if (cacheHabitaciones.value.isEmpty()) {
            fetchInitialData()
        } else {
            _uiState.value = ScreenState.Success(cacheHabitaciones.value)
        }
    }

    private fun fetchInitialData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                fetchRooms()
                fetchRandomRooms()
                fetchReservas()
                fetchServicios()
                fetchResenias()
                fetchReservasEventos()
                fetchServiciosEventos()
                fetchEspacios()
                fetchReservasServicios()
                fetchReservasParking()
                fetchReservasParkingAnonimo()

                _uiState.value =
                    ScreenState.Success(cacheHabitaciones.value) // Proporciona la lista de habitaciones
            } catch (e: Exception) {
                _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun fetchReservasParkingAnonimo() = viewModelScope.launch(handler + Dispatchers.IO) {
        repository.getLocalReservasParkingAnonimo()
            .onEach { localData ->
                if (localData.isNotEmpty()) {
                    cacheAnonParkingReservations.value = localData
                } else {
                    val remoteData = fetchRemoteReservaParkingAnonimo()
                    cacheAnonParkingReservations.value = remoteData
                }
            }.collect()
    }

    private suspend fun fetchRemoteReservaParkingAnonimo(): List<ReservaParkingAnonimo> {
        return try {
            repository.getRemoteReservaParkingAnonimo()
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
            emptyList()
        }
    }

    private fun fetchReservasParking() = viewModelScope.launch(handler + Dispatchers.IO) {
        repository.getLocalReservasParking()
            .onEach { localData ->
                if (localData.isNotEmpty()) {
                    cacheParkingReservations.value = localData
                } else {
                    val remoteData = fetchRemoteReservaParking()
                    cacheParkingReservations.value = remoteData
                }
            }.collect()
    }

    private suspend fun fetchRemoteReservaParking(): List<ReservaParking> {
        return try {
            repository.getRemoteReservaParking()
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
            emptyList()
        }
    }



    private fun fetchReservasServicios() = viewModelScope.launch(handler + Dispatchers.IO) {
        repository.getLocalReservasServicios()
            .onEach { localData ->
                if (localData.isNotEmpty()) {
                    cacheServiceReservations.value = localData
                } else {
                    val remoteData = fetchRemoteReservaServicios()
                    cacheServiceReservations.value = remoteData
                }
            }.collect()
    }

    private suspend fun fetchRemoteReservaServicios(): List<ReservaServicio> {
        return try {
            repository.getRemoteReservaServicios()
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
            emptyList()
        }
    }

    private fun fetchEspacios() = viewModelScope.launch(handler + Dispatchers.IO) {
        repository.getLocalEspacios()
            .onEach { localData ->
                if (localData.isNotEmpty()) {
                    cacheEspacios.value = localData
                } else {
                    val remoteData = fetchRemoteEspacios()
                    cacheEspacios.value = remoteData
                }
            }.collect()
    }

    private suspend fun fetchRemoteEspacios(): List<Espacio> {
        return try {
            repository.getRemoteEspacios()
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
            emptyList()
        }
    }

    private fun fetchServiciosEventos() = viewModelScope.launch(handler + Dispatchers.IO) {
        repository.getLocalServiciosEventos()
            .onEach { localData ->
                if (localData.isNotEmpty()) {
                    cacheServicioEventos.value = localData
                } else {
                    val remoteData = fetchRemoteServiciosEventos()
                    cacheServicioEventos.value = remoteData
                }
            }.collect()
    }

    private suspend fun fetchRemoteServiciosEventos(): List<ServicioEvento> {
        return try {
            repository.getRemoteServiciosEvento()
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
            emptyList()
        }
    }

    private fun fetchReservasEventos() = viewModelScope.launch(handler + Dispatchers.IO) {
        repository.getLocalReservaEvento()
            .onEach { localData ->
                if (localData.isNotEmpty()) {
                    cacheReservaEventos.value = localData
                } else {
                    val remoteData = fetchRemoteReservasEventos()
                    cacheReservaEventos.value = remoteData
                }
            }.collect()
    }

    private suspend fun fetchRemoteReservasEventos(): List<ReservaEventos> {
        return try {
            repository.getRemoteReservasEventos()
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
            emptyList()
        }
    }

    private fun fetchResenias() = viewModelScope.launch(handler + Dispatchers.IO) {
        repository.getLocalResenia()
            .onEach { localData ->
                if (localData.isNotEmpty()) {
                    cacheResenias.value = localData
                } else {
                    val remoteData = fetchRemoteResenias()
                    cacheResenias.value = remoteData
                }
            }.collect()
    }

    private suspend fun fetchRemoteResenias(): List<Resenia> {
        return try {
            repository.getRemoteResenias()
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
            emptyList()
        }
    }

    private fun fetchReservas() = viewModelScope.launch(handler + Dispatchers.IO) {
        repository.getLocalReserva()
            .onEach { localData ->
                if (localData.isNotEmpty()) {
                    cacheReservas.value = localData
                } else {
                    val remoteData = fetchRemoteReservas()
                    cacheReservas.value = remoteData
                }
            }.collect()
    }

    private suspend fun fetchRemoteReservas(): List<Reserva> {
        return try {
            repository.getRemoteReservas()
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
            emptyList()
        }
    }

    private fun fetchServicios() = viewModelScope.launch(handler + Dispatchers.IO) {
        repository.getLocalServicio()
            .onEach { localData ->
                if (localData.isNotEmpty()) {
                    cacheServicios.value = localData
                } else {
                    val remoteData = fetchRemoteServicios()
                    cacheServicios.value = remoteData
                }
            }.collect()
    }

    private suspend fun fetchRemoteServicios(): List<Servicio> {
        return try {
            repository.getRemoteServicios()
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
            emptyList()
        }
    }

    private fun fetchRooms() = viewModelScope.launch(handler + Dispatchers.IO) {
        repository.getLocalHabitacion()
            .onEach { localData ->
                if (localData.isNotEmpty()) {
                    cacheHabitaciones.value = localData
                } else {
                    val remoteData = fetchRemoteRooms()
                    cacheHabitaciones.value = remoteData
                }
            }.collect()
    }

    private suspend fun fetchRemoteRooms(): List<Habitacion> {
        return try {
            repository.getRemoteHabitaciones()
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
            emptyList()
        }
    }

    fun loadLocalRooms() = viewModelScope.launch(handler + Dispatchers.IO) {
        repository.getLocalHabitacion()
            .onEach { localRooms ->
                cacheHabitaciones.value = localRooms
                _uiState.value = ScreenState.Success(localRooms)
            }.collect()
    }

    fun loadLocalServices() = viewModelScope.launch(handler + Dispatchers.IO) {
        repository.getLocalServicio()
            .onEach { localServices ->
                cacheServicios.value = localServices
                _uiState.value = if (localServices.isNotEmpty()) {
                    ScreenState.SuccessServicios(localServices)
                } else {
                    ScreenState.Error("No se encontraron servicios")
                }
            }.collect()
    }

    private fun fetchRandomRooms() = viewModelScope.launch(handler + Dispatchers.IO) {
        try {
            val randomRooms = repository.getRandomLocalHabitaciones()
            cacheRandomHabitaciones.value = randomRooms
            _uiState.value = ScreenState.Success(randomRooms)
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
        }

    }

    fun fetchRandomResenias() = viewModelScope.launch(handler + Dispatchers.IO) {
        try {
            val remoteData = repository.getRemoteResenias()
            cacheResenias.value = remoteData
            _uiState.value = ScreenState.SuccessResenias(remoteData)
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
        }
    }

    fun getHabitacionById(id: Int?): Habitacion? {
        val cachedRoom = cacheHabitaciones.value.find { it.id == id }
        if (cachedRoom != null) {
            return cachedRoom
        }
        viewModelScope.launch(handler + Dispatchers.IO) {
            try {
                val room = repository.getHabitacionById(id)
                room?.let {
                    val updatedList = cacheHabitaciones.value.toMutableList().apply { add(it) }
                    cacheHabitaciones.value = updatedList
                    repository.saveHabitaciones(updatedList)
                }
            } catch (e: Exception) {
                _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
            }
        }
        return null
    }

    private val _selectedHabitacionId = MutableStateFlow<Int?>(null)
    val selectedHabitacionId: StateFlow<Int?> = _selectedHabitacionId.asStateFlow()

    fun selectHabitacionId(id: Int?) {
        _selectedHabitacionId.value = id
    }

    private val _userData = MutableStateFlow<JSONObject?>(null)
    val userData: StateFlow<JSONObject?> = _userData

    fun setUserData(data: JSONObject?) {
        viewModelScope.launch {
            _userData.emit(data)
        }
    }

    fun isUserLoggedIn(): Boolean {
        return _userData.value != null
    }

    fun addReserva(reserva: Reserva) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertReserva(reserva)
        }
    }

    fun addReservaEvento(reserva: ReservaEventos) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertReservaEvento(reserva)
        }
    }

    suspend fun getReservasByHabitacion(habitacionId: Int): List<Reserva> {
        return withContext(Dispatchers.IO) {
            repository.getReservasByHabitacion(habitacionId)
        }
    }

    suspend fun getReservasByEspacio(espacioId: Int): List<ReservaEventos> {
        return withContext(Dispatchers.IO) {
            repository.getReservasByEspacio(espacioId)
        }
    }

    fun loadUserEventReservations(userId: Int) {
        viewModelScope.launch {
            try {
                val userEventReservations = repository.getReservasEventosByUserId(userId)
                cacheUserEventReservations.value = userEventReservations
            } catch (e: Exception) {
                _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
            }
        }
    }
    suspend fun getLastReservationId(): LastIdResponse {
        Log.d("Vakero", repository.getLastReservationId().toString())
        return repository.getLastReservationId()
    }

    fun loadUserReservations(userId: Int) {
        viewModelScope.launch {
            try {
                val userReservations = repository.getReservasByUserId(userId)
                cacheUserReservations.value = userReservations
            } catch (e: Exception) {
                _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private val _selectedespaciosID = MutableStateFlow<Int?>(null)
    val selectedespaciosID: StateFlow<Int?> = _selectedespaciosID.asStateFlow()
    fun selectEspacioId(id: Int?) {
        _selectedespaciosID.value = id
        Log.d("MainNavigation", "Espacio ViewModel: $id")
    }

    fun getEspacioById(id: Int?): Espacio? {
        val cachedRoom = cacheEspacios.value.find { it.id == id }
        if (cachedRoom != null) {
            return cachedRoom
        }
        viewModelScope.launch(handler + Dispatchers.IO) {
            try {
                val room = repository.getEspaciosById(id)
                room?.let {
                    val updatedList = cacheEspacios.value.toMutableList().apply { add(it) }
                    cacheEspacios.value = updatedList
                    repository.saveEspacios(updatedList)
                }
            } catch (e: Exception) {
                _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
            }
        }
        return null
    }

    fun logIn(user: JSONObject) {
        viewModelScope.launch {
            _userData.value = user
        }
    }

    // Método para cerrar sesión
    fun logOut() {
        viewModelScope.launch {
            _userData.value = null
        }
    }
}
