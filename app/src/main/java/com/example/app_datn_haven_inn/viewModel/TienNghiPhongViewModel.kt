package com.example.app_datn_haven_inn.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.TienNghiPhongModel
import com.example.app_datn_haven_inn.database.repository.TienNghiPhongRepository
import com.example.app_datn_haven_inn.database.service.TienNghiPhongService
import kotlinx.coroutines.launch

class TienNghiPhongViewModel : BaseViewModel() {

    private val _tienNghiPhongList = MutableLiveData<List<TienNghiPhongModel>?>()
    val tienNghiPhongList: LiveData<List<TienNghiPhongModel>?> get() = _tienNghiPhongList

    private val _tienNghiPhongListByIdLoaiPhong = MutableLiveData<List<TienNghiPhongModel>?>()
    val tienNghiPhongListByIdLoaiPhong: LiveData<List<TienNghiPhongModel>?> get() = _tienNghiPhongList

    private val _tienNghiPhong = MutableLiveData<TienNghiPhongModel?>()
    val tienNghiPhong: LiveData<TienNghiPhongModel?> get() = _tienNghiPhong

    private val _istienNghiPhongAdded = MutableLiveData<Boolean>()
    val istienNghiPhongAdded: LiveData<Boolean> get() = _istienNghiPhongAdded

    private val _istienNghiPhongUpdated = MutableLiveData<Boolean>()
    val istienNghiPhongUpdated: LiveData<Boolean> get() = _istienNghiPhongUpdated

    private val _istienNghiPhongDeleted = MutableLiveData<Boolean>()
    val istienNghiPhongDeleted: LiveData<Boolean> get() = _istienNghiPhongDeleted

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getListtienNghiPhong() {
        viewModelScope.launch {
            try {

                val apiService : TienNghiPhongService = CreateService.createService()
                val TienNghiPhongRepository = TienNghiPhongRepository(apiService)
                _tienNghiPhongList.value = TienNghiPhongRepository.getListTienNghiPhong()
            } catch (e: Exception) {
                Log.e("tienNghiPhongViewModel", "Error fetching tienNghiPhong list", e)
                _errorMessage.value = "Error fetching tienNghiPhong list: ${e.message}"
            }
        }
    }

    fun getListTienNghiPhongByIdLoaiPhong(idLoaiPhong: String) {
        viewModelScope.launch {
            try {
                val apiService : TienNghiPhongService = CreateService.createService()
                val TienNghiPhongRepository = TienNghiPhongRepository(apiService)
                _tienNghiPhongListByIdLoaiPhong.value = TienNghiPhongRepository.getListTienNghiByIdLoaiPhong(idLoaiPhong)
            } catch (e: Exception) {
                Log.e("tienNghiPhongViewModel", "Error fetching tienNghiPhong list", e)
                _errorMessage.value = "Error fetching tienNghiPhong list: ${e.message}"
            }
        }
    }

    fun addtienNghiPhong(tienNghiPhong: TienNghiPhongModel) {
        viewModelScope.launch {
            try {
                val apiService : TienNghiPhongService = CreateService.createService()
                val TienNghiPhongRepository = TienNghiPhongRepository(apiService)
                _istienNghiPhongAdded.value = TienNghiPhongRepository.addTienNghiPhong(tienNghiPhong) != null
            } catch (e: Exception) {
                Log.e("tienNghiPhongViewModel", "Error adding tienNghiPhong", e)
                _errorMessage.value = "Error adding tienNghiPhong: ${e.message}"
            }
        }
    }


    fun updatetienNghiPhong(id: String, tienNghiPhong: TienNghiPhongModel) {
        viewModelScope.launch {
            try {
                val apiService : TienNghiPhongService = CreateService.createService()
                val TienNghiPhongRepository = TienNghiPhongRepository(apiService)
                _istienNghiPhongUpdated.value = TienNghiPhongRepository.updateTienNghiPhong(id, tienNghiPhong) != null
            } catch (e: Exception) {
                Log.e("tienNghiPhongViewModel", "Error updating tienNghiPhong", e)
                _errorMessage.value = "Error updating tienNghiPhong: ${e.message}"
            }
        }
    }

    fun deletetienNghiPhong(id: String) {
        viewModelScope.launch {
            try {
                val apiService : TienNghiPhongService = CreateService.createService()
                val TienNghiPhongRepository = TienNghiPhongRepository(apiService)
                _istienNghiPhongDeleted.value = TienNghiPhongRepository.deleteTienNghiPhong(id)
            } catch (e: Exception) {
                Log.e("tienNghiPhongViewModel", "Error deleting tienNghiPhong", e)
                _errorMessage.value = "Error deleting tienNghiPhong: ${e.message}"
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}