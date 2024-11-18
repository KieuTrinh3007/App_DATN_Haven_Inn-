package com.example.app_datn_haven_inn.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.LoaiPhongModel
import com.example.app_datn_haven_inn.database.repository.LoaiPhongRepository
import com.example.app_datn_haven_inn.database.service.LoaiPhongService
import kotlinx.coroutines.launch

class LoaiPhongViewModel : BaseViewModel() {
    private val _loaiPhongList = MutableLiveData<List<LoaiPhongModel>?>()
    val loaiPhongList: LiveData<List<LoaiPhongModel>?> get() = _loaiPhongList

    private val _loaiPhong = MutableLiveData<LoaiPhongModel?>()
    val loaiPhong: LiveData<LoaiPhongModel?> get() = _loaiPhong

    private val _isloaiPhongAdded = MutableLiveData<Boolean>()
    val isloaiPhongAdded: LiveData<Boolean> get() = _isloaiPhongAdded

    private val _isloaiPhongUpdated = MutableLiveData<Boolean>()
    val isloaiPhongUpdated: LiveData<Boolean> get() = _isloaiPhongUpdated

    private val _isloaiPhongDeleted = MutableLiveData<Boolean>()
    val isloaiPhongDeleted: LiveData<Boolean> get() = _isloaiPhongDeleted

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getListloaiPhong() {
        viewModelScope.launch {
            try {

                val apiService : LoaiPhongService = CreateService.createService()
                val LoaiPhongRepository = LoaiPhongRepository(apiService)
                _loaiPhongList.value = LoaiPhongRepository.getListLoaiPhong()
            } catch (e: Exception) {
                Log.e("loaiPhongViewModel", "Error fetching loaiPhong list", e)
                _errorMessage.value = "Error fetching loaiPhong list: ${e.message}"
            }
        }
    }

    fun addloaiPhong(loaiPhong: LoaiPhongModel) {
        viewModelScope.launch {
            try {
                val apiService : LoaiPhongService = CreateService.createService()
                val LoaiPhongRepository = LoaiPhongRepository(apiService)
                _isloaiPhongAdded.value = LoaiPhongRepository.addLoaiPhong(loaiPhong) != null
            } catch (e: Exception) {
                Log.e("loaiPhongViewModel", "Error adding loaiPhong", e)
                _errorMessage.value = "Error adding loaiPhong: ${e.message}"
            }
        }
    }


    fun updateloaiPhong(id: String, loaiPhong: LoaiPhongModel) {
        viewModelScope.launch {
            try {
                val apiService : LoaiPhongService = CreateService.createService()
                val LoaiPhongRepository = LoaiPhongRepository(apiService)
                _isloaiPhongUpdated.value = LoaiPhongRepository.updateLoaiPhong(id, loaiPhong) != null
            } catch (e: Exception) {
                Log.e("loaiPhongViewModel", "Error updating loaiPhong", e)
                _errorMessage.value = "Error updating loaiPhong: ${e.message}"
            }
        }
    }

    fun deleteloaiPhong(id: String) {
        viewModelScope.launch {
            try {
                val apiService : LoaiPhongService = CreateService.createService()
                val LoaiPhongRepository = LoaiPhongRepository(apiService)
                _isloaiPhongDeleted.value = LoaiPhongRepository.deleteLoaiPhong(id)
            } catch (e: Exception) {
                Log.e("loaiPhongViewModel", "Error deleting loaiPhong", e)
                _errorMessage.value = "Error deleting loaiPhong: ${e.message}"
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}