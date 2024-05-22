package com.pabloat.hotelperikero.ui.util

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
import com.pabloat.hotelperikero.data.local.entities.User
import kotlinx.coroutines.flow.MutableStateFlow

class CacheLists {
    /**
     * La forma más óptima de realizar las peticiones en Kotlin es haciendo las peticiones
     * solo cuando realmente necesitamos hacerlas. Hay que hacer las siguientes comprobaciones
     *
     * 1. Ver si existen datos en las listas.
     *  1.1. Si existen datos en las listas mostrarlos (FIN)
     *
     * SI NO EXISTEN DATOS EN LAS LISTAS:
     * 2. Comprobar si existen datos en la base de datos.
     * 3. Comparar los datos de la base de datos con la petición.
     * 4. Actualiza, borra y añade los datos que son distintos
     * 5. Meter los datos en las listas.
     *
     * EN LAS PANTALLAS SE UTILIZAN LOS DATOS DE ESTAS LISTAS SIEMPRE!!!!!
     */
    companion object {
        var cacheHabitaciones = MutableStateFlow<List<Habitacion>>(emptyList())
        var cacheEspacios = MutableStateFlow<List<Espacio>>(emptyList())
        var cacheResenias = MutableStateFlow<List<Resenia>>(emptyList())
        var cacheReservas = MutableStateFlow<List<Reserva>>(emptyList())
        var cacheReservaEventos = MutableStateFlow<List<ReservaEventos>>(emptyList())
        var cacheServicios = MutableStateFlow<List<Servicio>>(emptyList())
        var cacheServicioEventos = MutableStateFlow<List<ServicioEvento>>(emptyList())
        var cacheUsers = MutableStateFlow<List<User>>(emptyList())
        var cacheRandomHabitaciones = MutableStateFlow<List<Habitacion>>(emptyList())
        var cacheUserReservations = MutableStateFlow<List<Reserva>>(emptyList())
        var cacheUserEventReservations = MutableStateFlow<List<ReservaEventos>>(emptyList())
        var cacheServiceReservations = MutableStateFlow<List<ReservaServicio>>(emptyList())
        var cacheParkingReservations = MutableStateFlow<List<ReservaParking>>(emptyList())
        var cacheAnonParkingReservations =
            MutableStateFlow<List<ReservaParkingAnonimo>>(emptyList())
    }
}