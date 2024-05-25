package com.pabloat.hotelperikero.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pabloat.hotelperikero.data.local.dao.LocalReservaParkingDao

class ParkingViewModelFactory(val parkingDao: LocalReservaParkingDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParkingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ParkingViewModel(parkingDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
