package com.pabloat.hotelperikero.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pabloat.hotelperikero.data.local.dao.LocalReseniaDao

class ReseniaViewModelFactory(val reseniaDao: LocalReseniaDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReseniaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReseniaViewModel(reseniaDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}