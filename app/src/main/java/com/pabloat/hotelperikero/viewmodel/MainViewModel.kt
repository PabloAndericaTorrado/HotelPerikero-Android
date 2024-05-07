package com.pabloat.GameHubConnect.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pabloat.GameHubConnect.data.VideojuegoRepository
import com.pabloat.GameHubConnect.data.local.Videojuego
import com.pabloat.GameHubConnect.ui.util.ScreenState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel para gestionar la lógica relacionada con los videojuegos.
 * @param repository Repositorio que proporciona acceso a los datos de los videojuegos.
 */
class MainViewModel(private val repository: VideojuegoRepository) : ViewModel() {

    // Lista mutable de videojuegos totales
    private val _videojuegos: MutableStateFlow<List<Videojuego>> = MutableStateFlow(listOf())
    var videojuegos = _videojuegos.asStateFlow()

    // Lista mutable de géneros de videojuegos para acceder posteriormente
    private val _generos: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val generos: StateFlow<List<String>> = _generos

    // Estado de la interfaz de usuario
    private val _uiState: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.Loading)

    // Manejador de excepciones para coroutines
    private val handler = CoroutineExceptionHandler { _, exception ->
        _uiState.value =
            ScreenState.Error("Ha ocurrido un error, revise su conexión a internet o inténtelo de nuevo más tarde")
    }

    // ID del videojuego seleccionado, para luego hacer alguna operación con él
    private val _selectedVideojuegoId = MutableStateFlow<Int?>(null)
    val selectedVideojuegoId: StateFlow<Int?> = _selectedVideojuegoId


    // Inicializa variable para buscar por genero
    private val busquedaGenero = MutableStateFlow("")

    // Lista mutable de videojuegos filtrados por género
    private val _videojuegosPorGenero = MutableStateFlow<List<Videojuego>>(emptyList())

    // Lista mutable de videojuegos guardados
    private val _savedGames = MutableStateFlow<List<Videojuego>>(emptyList())
    val savedGames: StateFlow<List<Videojuego>> get() = _savedGames.asStateFlow()

    /**
     * Guarda un videojuego en la lista de juegos guardados.
     * @return true si el juego se guarda correctamente, false si ya estaba guardado.
     */
    fun saveGameArray(videojuego: Videojuego): Boolean {
        val isAlreadySaved = _savedGames.value.any { it.id == videojuego.id }
        if (!isAlreadySaved) {
            _savedGames.value += videojuego
            return true
        }
        return false
    }

    /**
     * Elimina todos los juegos guardados en la lista
     */
    fun deleteAllSavedGames() {
        _savedGames.value = emptyList()
    }

    /**
     * Obtiene los videojuegos por género para luego poder acceder a cada uno.
     * @param genre Género de los videojuegos a recuperar.
     * @return Flow de lista de videojuegos filtrados por género.
     */
    fun getVideojuegosByGenre(genre: String): Flow<List<Videojuego>> {
        return repository.getVideojuegosByGenre(genre).onEach { videojuegos ->
            _videojuegosPorGenero.value =
                videojuegos
        }
    }

    // Inicialización
    init {
        viewModelScope.launch(handler) {
            // Se obtienen y observan todos los géneros disponibles
            repository.getAllGenres().collect { genres ->
                _generos.value = genres
            }
        }
    }

    /**
     * Establece el ID del videojuego seleccionado.
     * @param videojuegoId ID del videojuego seleccionado.
     */
    fun setSelectedVideojuegoId(videojuegoId: Int?) {
        _selectedVideojuegoId.value = videojuegoId
    }

    // Videojuego seleccionado
    private val _selectedVideojuego = MutableStateFlow<Videojuego?>(null)
    val selectedVideojuego: StateFlow<Videojuego?> = _selectedVideojuego

    /**
     * Establece el videojuego seleccionado.
     * @param videojuego Videojuego seleccionado.
     */
    fun setSelectedVideojuego(videojuego: Videojuego?) {
        _selectedVideojuego.value = videojuego
    }

    /**
     * Obtiene los videojuegos remotos.
     */
    fun getRemoteVideojuego() {
        viewModelScope.launch(handler) {
            delay(5000) // Simula un retardo antes de obtener los datos remotos
            Log.d("VM", "Lanzamos petición a la API")
            val remoteVideojuegos: List<Videojuego> =
                repository.getRemoteVideojuego()
            // Recogemos el resultado
            _videojuegos.value = remoteVideojuegos
            // Actualizamos el estado
            _uiState.value = ScreenState.Success(videojuegos)
            Log.d("VM", "Info obtenida: " + remoteVideojuegos.toString())
        }
        Log.d("VM", "Petición lanzada. Los datos irán llegando...")
    }

    /**
     * Agrega un videojuego a la base de datos local.
     * @param videojuego Videojuego a agregar.
     */
    fun addGame(videojuego: Videojuego) {
        viewModelScope.launch {
            Log.d("VM", "Añadimos el videojuego: $videojuego")
            repository.insertone(videojuego)
        }
    }

    /**
     * Busca un videojuego por título.
     * @param gameTitle Título del videojuego a buscar.
     * @return El videojuego encontrado, o null si no se encuentra.
     */
    suspend fun searchGame(gameTitle: String): Videojuego? {
        return withContext(Dispatchers.IO) {
            repository.searchVideojuegoByTitle(gameTitle)
        }
    }

    /**
     * Elimina un videojuego de la base de datos local.
     * @param id ID del videojuego a eliminar.
     * @return true si se elimina correctamente, false si ocurre algún error.
     */
    suspend fun deleteGame(id: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                repository.deleteVideojuego(id)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Actualiza la informacion de un videojuego
     * @param videojuego datos de un videojuego a actualizar.
     *
     */
    fun updateVideoGame(videojuego: Videojuego) {
        viewModelScope.launch {
            repository.updateVideojuego(videojuego)
        }
    }

    /**
     * Obtiene un videojuego por su ID.
     * @param videojuegoId ID del videojuego a obtener.
     * @return El videojuego encontrado, o null si no se encuentra.
     */
    suspend fun getSelectedVideojuego(videojuegoId: Int): Videojuego? {
        return repository.getVideojuegoById(videojuegoId)

    }

    /**
     * Actualiza la valoración de un videojuego.
     * @param id ID del videojuego a actualizar.
     * @param newRating Nueva valoración del videojuego.
     */
    fun updateVideojuegoRating(id: Int?, newRating: Float) {
        viewModelScope.launch {
            val videojuego = repository.getVideojuegoById(id)
            if (videojuego != null) {
                videojuego.valoracion = newRating
                repository.updateVideojuego(videojuego)
            }
        }
    }

    // Videojuego en el estado interno del ViewModel
    private val _videojuego: MutableStateFlow<Videojuego> =
        MutableStateFlow(Videojuego(0, "Default", "Default", "", "", "", "", "", 0.0f))
    var videojuego = _videojuego.asStateFlow()

    /**
     * Guarda un videojuego en una variable del viewModel
     * @param videojuego Videojuego a guardar.
     */
    fun saveGame(videojuego: Videojuego) {
        _videojuego.value = videojuego
    }

    /**
     * Obtiene el videojuego guardado en el viewModel.
     * @return El videojuego guardado.
     */
    fun getGame(): Videojuego {
        return videojuego.value
    }

    /**
     * Filtra los géneros de videojuegos según una consulta de búsqueda.
     */
    val generosFiltrados = busquedaGenero.map { query ->
        generos.value.filter { genre ->
            genre.contains(query, ignoreCase = true)
        }
    }

    /**
     * Gestiona la búsqueda de géneros de videojuegos.
     * @param query Consulta de búsqueda.
     */
    fun onBusquedaGenero(query: String) {
        busquedaGenero.value = query
    }


    /**
     * Implementadas en sus respectivas pantallas pero alomejor son necesarias
    val juegosFiltrados = busquedaJuego.map { query ->
        videojuegos.value.filter { videojuego ->
            videojuego.title.contains(query, ignoreCase = true)
        }
    }


    fun onBusquedaJuego(query: String) {
        busquedaJuego.value = query
    }
        */
}
