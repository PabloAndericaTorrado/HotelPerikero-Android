package com.pabloat.hotelperikero.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pabloat.hotelperikero.data.local.dao.LocalReservaDao

class ReservaViewModelFactory(val reservaDao: LocalReservaDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReservaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReservaViewModel(reservaDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
