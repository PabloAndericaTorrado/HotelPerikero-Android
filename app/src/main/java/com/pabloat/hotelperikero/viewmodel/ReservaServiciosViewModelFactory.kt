package com.pabloat.hotelperikero.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pabloat.hotelperikero.data.local.dao.LocalReservaServicioDao

class ReservaServiciosViewModelFactory(val reservaServicioDao: LocalReservaServicioDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReservaServicioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReservaServicioViewModel(reservaServicioDao) as T

        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}