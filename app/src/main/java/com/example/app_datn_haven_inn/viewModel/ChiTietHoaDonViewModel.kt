package com.example.app_datn_haven_inn.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.ChiTietHoaDonModel
import com.example.app_datn_haven_inn.database.repository.ChiTietHoaDonReponsitory
import com.example.app_datn_haven_inn.database.service.ChiTietHoaDonService
import kotlinx.coroutines.launch

class ChiTietHoaDonViewModel : BaseViewModel()  {

    private val _chiTietHoaDonList = MutableLiveData<List<ChiTietHoaDonModel>?>()
    val chiTietHoaDonList: LiveData<List<ChiTietHoaDonModel>?> get() = _chiTietHoaDonList

    private val _chiTietHoaDon = MutableLiveData<ChiTietHoaDonModel?>()
    val chiTietHoaDon: LiveData<ChiTietHoaDonModel?> get() = _chiTietHoaDon

    private val _ischiTietHoaDonAdded = MutableLiveData<Boolean>()
    val ischiTietHoaDonAdded: LiveData<Boolean> get() = _ischiTietHoaDonAdded

    private val _ischiTietHoaDonUpdated = MutableLiveData<Boolean>()
    val ischiTietHoaDonUpdated: LiveData<Boolean> get() = _ischiTietHoaDonUpdated

    private val _ischiTietHoaDonDeleted = MutableLiveData<Boolean>()
    val ischiTietHoaDonDeleted: LiveData<Boolean> get() = _ischiTietHoaDonDeleted

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getListchiTietHoaDon() {
        viewModelScope.launch {
            try {

                val apiService : ChiTietHoaDonService = CreateService.createService()
                val chiTietHoaDonRepository = ChiTietHoaDonReponsitory(apiService)
                _chiTietHoaDonList.value = chiTietHoaDonRepository.getListChiTietHoaDon()
            } catch (e: Exception) {
                Log.e("chiTietHoaDonViewModel", "Error fetching chiTietHoaDon list", e)
                _errorMessage.value = "Error fetching chiTietHoaDon list: ${e.message}"
            }
        }
    }

    fun addchiTietHoaDon(chiTietHoaDon: ChiTietHoaDonModel) {
        viewModelScope.launch {
            try {
                val apiService : ChiTietHoaDonService = CreateService.createService()
                val chiTietHoaDonRepository = ChiTietHoaDonReponsitory(apiService)
                _ischiTietHoaDonAdded.value = chiTietHoaDonRepository.addChiTietHoaDon(chiTietHoaDon) != null
            } catch (e: Exception) {
                Log.e("chiTietHoaDonViewModel", "Error adding chiTietHoaDon", e)
                _errorMessage.value = "Error adding chiTietHoaDon: ${e.message}"
            }
        }
    }


    fun updatechiTietHoaDon(id: String, chiTietHoaDon: ChiTietHoaDonModel) {
        viewModelScope.launch {
            try {
                val apiService : ChiTietHoaDonService = CreateService.createService()
                val chiTietHoaDonRepository = ChiTietHoaDonReponsitory(apiService)
                _ischiTietHoaDonUpdated.value = chiTietHoaDonRepository.updateChiTietHoaDon(id, chiTietHoaDon) != null
            } catch (e: Exception) {
                Log.e("chiTietHoaDonViewModel", "Error updating chiTietHoaDon", e)
                _errorMessage.value = "Error updating chiTietHoaDon: ${e.message}"
            }
        }
    }

    fun deletechiTietHoaDon(id: String) {
        viewModelScope.launch {
            try {
                val apiService : ChiTietHoaDonService = CreateService.createService()
                val chiTietHoaDonRepository = ChiTietHoaDonReponsitory(apiService)
                _ischiTietHoaDonDeleted.value = chiTietHoaDonRepository.deleteChiTietHoaDon(id)
            } catch (e: Exception) {
                Log.e("chiTietHoaDonViewModel", "Error deleting chiTietHoaDon", e)
                _errorMessage.value = "Error deleting chiTietHoaDon: ${e.message}"
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}