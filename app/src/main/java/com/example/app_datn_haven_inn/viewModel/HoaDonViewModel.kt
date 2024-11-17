package com.example.app_datn_haven_inn.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.HoaDonModel
import com.example.app_datn_haven_inn.database.repository.HoaDonRepository
import com.example.app_datn_haven_inn.database.service.HoaDonService

import kotlinx.coroutines.launch

class HoaDonViewModel : BaseViewModel() {
    private val _hoaDonList = MutableLiveData<List<HoaDonModel>?>()
    val hoaDonList: LiveData<List<HoaDonModel>?> get() = _hoaDonList

    private val _hoaDon = MutableLiveData<HoaDonModel?>()
    val hoaDon: LiveData<HoaDonModel?> get() = _hoaDon

    private val _ishoaDonAdded = MutableLiveData<Boolean>()
    val ishoaDonAdded: LiveData<Boolean> get() = _ishoaDonAdded

    private val _ishoaDonUpdated = MutableLiveData<Boolean>()
    val ishoaDonUpdated: LiveData<Boolean> get() = _ishoaDonUpdated

    private val _ishoaDonDeleted = MutableLiveData<Boolean>()
    val ishoaDonDeleted: LiveData<Boolean> get() = _ishoaDonDeleted

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getListhoaDon() {
        viewModelScope.launch {
            try {

                val apiService : HoaDonService = CreateService.createService()
                val HoaDonRepository = HoaDonRepository(apiService)
                _hoaDonList.value = HoaDonRepository.getListHoaDon()
            } catch (e: Exception) {
                Log.e("hoaDonViewModel", "Error fetching hoaDon list", e)
                _errorMessage.value = "Error fetching hoaDon list: ${e.message}"
            }
        }
    }

    fun addhoaDon(hoaDon: HoaDonModel) {
        viewModelScope.launch {
            try {
                val apiService : HoaDonService = CreateService.createService()
                val HoaDonRepository = HoaDonRepository(apiService)
                _ishoaDonAdded.value = HoaDonRepository.addHoaDon(hoaDon) != null
            } catch (e: Exception) {
                Log.e("hoaDonViewModel", "Error adding hoaDon", e)
                _errorMessage.value = "Error adding hoaDon: ${e.message}"
            }
        }
    }


    fun updatehoaDon(id: String, hoaDon: HoaDonModel) {
        viewModelScope.launch {
            try {
                val apiService : HoaDonService = CreateService.createService()
                val HoaDonRepository = HoaDonRepository(apiService)
                _ishoaDonUpdated.value = HoaDonRepository.updateHoaDon(id, hoaDon) != null
            } catch (e: Exception) {
                Log.e("hoaDonViewModel", "Error updating hoaDon", e)
                _errorMessage.value = "Error updating hoaDon: ${e.message}"
            }
        }
    }

    fun deletehoaDon(id: String) {
        viewModelScope.launch {
            try {
                val apiService : HoaDonService = CreateService.createService()
                val HoaDonRepository = HoaDonRepository(apiService)
                _ishoaDonDeleted.value = HoaDonRepository.deleteHoaDon(id)
            } catch (e: Exception) {
                Log.e("hoaDonViewModel", "Error deleting hoaDon", e)
                _errorMessage.value = "Error deleting hoaDon: ${e.message}"
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}