package com.pabloat.hotelperikero.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pabloat.hotelperikero.data.local.dao.LocalReservaEventosDao

class ReservaEventosViewModelFactory(val reservaEventosDao: LocalReservaEventosDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReservaEventosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReservaEventosViewModel(reservaEventosDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
