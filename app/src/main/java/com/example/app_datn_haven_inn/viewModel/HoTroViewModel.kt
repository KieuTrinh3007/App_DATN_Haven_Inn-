package com.example.app_datn_haven_inn.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.HoTroModel
import com.example.app_datn_haven_inn.database.repository.HoTroRepository
import com.example.app_datn_haven_inn.database.service.HoTroService
import kotlinx.coroutines.launch

class HoTroViewModel : BaseViewModel() {
    private val _hoTroList = MutableLiveData<List<HoTroModel>?>()
    val hoTroList: LiveData<List<HoTroModel>?> get() = _hoTroList

    private val _hoTro = MutableLiveData<HoTroModel?>()
    val hoTro: LiveData<HoTroModel?> get() = _hoTro

    private val _ishoTroAdded = MutableLiveData<Boolean>()
    val ishoTroAdded: LiveData<Boolean> get() = _ishoTroAdded

    private val _ishoTroUpdated = MutableLiveData<Boolean>()
    val ishoTroUpdated: LiveData<Boolean> get() = _ishoTroUpdated

    private val _ishoTroDeleted = MutableLiveData<Boolean>()
    val ishoTroDeleted: LiveData<Boolean> get() = _ishoTroDeleted

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getListhoTro() {
        viewModelScope.launch {
            try {

                val apiService : HoTroService = CreateService.createService()
                val HoTroRepository = HoTroRepository(apiService)
                _hoTroList.value = HoTroRepository.getListHoTro()
            } catch (e: Exception) {
                Log.e("hoTroViewModel", "Error fetching hoTro list", e)
                _errorMessage.value = "Error fetching hoTro list: ${e.message}"
            }
        }
    }

    fun addhoTro(hoTro: HoTroModel) {
        viewModelScope.launch {
            try {
                val apiService : HoTroService = CreateService.createService()
                val HoTroRepository = HoTroRepository(apiService)
                _ishoTroAdded.value = HoTroRepository.addHoTro(hoTro) != null
            } catch (e: Exception) {
                Log.e("hoTroViewModel", "Error adding hoTro", e)
                _errorMessage.value = "Error adding hoTro: ${e.message}"
            }
        }
    }


    fun updatehoTro(id: String, hoTro: HoTroModel) {
        viewModelScope.launch {
            try {
                val apiService : HoTroService = CreateService.createService()
                val HoTroRepository = HoTroRepository(apiService)
                _ishoTroUpdated.value = HoTroRepository.updateHoTro(id, hoTro) != null
            } catch (e: Exception) {
                Log.e("hoTroViewModel", "Error updating hoTro", e)
                _errorMessage.value = "Error updating hoTro: ${e.message}"
            }
        }
    }

    fun deletehoTro(id: String) {
        viewModelScope.launch {
            try {
                val apiService : HoTroService = CreateService.createService()
                val HoTroRepository = HoTroRepository(apiService)
                _ishoTroDeleted.value = HoTroRepository.deleteHoTro(id)
            } catch (e: Exception) {
                Log.e("hoTroViewModel", "Error deleting hoTro", e)
                _errorMessage.value = "Error deleting hoTro: ${e.message}"
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}