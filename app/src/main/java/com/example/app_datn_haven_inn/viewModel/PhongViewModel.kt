package com.example.app_datn_haven_inn.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.PhongModel
import com.example.app_datn_haven_inn.database.repository.PhongRepository
import com.example.app_datn_haven_inn.database.service.PhongService
import kotlinx.coroutines.launch

class PhongViewModel : BaseViewModel() {

    private val _phongList = MutableLiveData<List<PhongModel>?>()
    val phongList: LiveData<List<PhongModel>?> get() = _phongList

    private val _phongListByIdLoaiPhong = MutableLiveData<List<PhongModel>?>()
    val phongListByIdLoaiPhong: LiveData<List<PhongModel>?> get() = _phongListByIdLoaiPhong

    private val _phong = MutableLiveData<PhongModel?>()
    val phong: LiveData<PhongModel?> get() = _phong

    private val _isphongAdded = MutableLiveData<Boolean>()
    val isphongAdded: LiveData<Boolean> get() = _isphongAdded

    private val _isphongUpdated = MutableLiveData<Boolean>()
    val isphongUpdated: LiveData<Boolean> get() = _isphongUpdated

    private val _isphongDeleted = MutableLiveData<Boolean>()
    val isphongDeleted: LiveData<Boolean> get() = _isphongDeleted

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _selectedRooms = MutableLiveData<List<PhongModel>>()
    val selectedRooms: LiveData<List<PhongModel>> = _selectedRooms

    // Biến lưu tổng tiền
    private val _totalPrice = MutableLiveData<Int>()
    val totalPrice: LiveData<Int> = _totalPrice


    fun getListphong() {
        viewModelScope.launch {
            try {

                val apiService : PhongService = CreateService.createService()
                val PhongRepository = PhongRepository(apiService)


                _phongList.value = PhongRepository.getListPhong()
            } catch (e: Exception) {
                Log.e("phongViewModel", "Error fetching phong list", e)
                _errorMessage.value = "Error fetching phong list: ${e.message}"
            }
        }
    }

    fun getListPhongByIdLoaiPhong(idLoaiPhong: String) {
        viewModelScope.launch {
            try {
                val apiService : PhongService = CreateService.createService()
                val PhongRepository = PhongRepository(apiService)
                _phongListByIdLoaiPhong.value = PhongRepository.getListPhongByIdLoaiPhong(idLoaiPhong)
            } catch (e: Exception) {
                Log.e("phongViewModel", "Error fetching phong list", e)
                _errorMessage.value = "Error fetching phong list: ${e.message}"
            }
        }
    }

    fun addphong(phong: PhongModel) {
        viewModelScope.launch {
            try {
                val apiService : PhongService = CreateService.createService()
                val PhongRepository = PhongRepository(apiService)
                _isphongAdded.value = PhongRepository.addPhong(phong) != null
            } catch (e: Exception) {
                Log.e("phongViewModel", "Error adding phong", e)
                _errorMessage.value = "Error adding phong: ${e.message}"
            }
        }
    }


    fun updatephong(id: String, phong: PhongModel) {
        viewModelScope.launch {
            try {
                val apiService : PhongService = CreateService.createService()
                val PhongRepository = PhongRepository(apiService)
                _isphongUpdated.value = PhongRepository.updatePhong(id, phong) != null
            } catch (e: Exception) {
                Log.e("phongViewModel", "Error updating phong", e)
                _errorMessage.value = "Error updating phong: ${e.message}"
            }
        }
    }

    fun deletephong(id: String) {
        viewModelScope.launch {
            try {
                val apiService : PhongService = CreateService.createService()
                val PhongRepository = PhongRepository(apiService)
                _isphongDeleted.value = PhongRepository.deletePhong(id)
            } catch (e: Exception) {
                Log.e("phongViewModel", "Error deleting phong", e)
                _errorMessage.value = "Error deleting phong: ${e.message}"
            }
        }
    }

    fun saveBookingData(rooms: List<PhongModel>, price: Int) {
        _selectedRooms.value = rooms
        _totalPrice.value = price
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}