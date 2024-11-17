package com.example.app_datn_haven_inn.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.YeuThichModel
import com.example.app_datn_haven_inn.database.repository.YeuThichRepository
import com.example.app_datn_haven_inn.database.service.YeuThichService
import kotlinx.coroutines.launch

class YeuThichViewModel : BaseViewModel() {
    private val _yeuThichList = MutableLiveData<List<YeuThichModel>?>()
    val yeuThichList: LiveData<List<YeuThichModel>?> get() = _yeuThichList

    private val _yeuThich = MutableLiveData<YeuThichModel?>()
    val yeuThich: LiveData<YeuThichModel?> get() = _yeuThich

    private val _isyeuThichAdded = MutableLiveData<Boolean>()
    val isyeuThichAdded: LiveData<Boolean> get() = _isyeuThichAdded

    private val _isyeuThichUpdated = MutableLiveData<Boolean>()
    val isyeuThichUpdated: LiveData<Boolean> get() = _isyeuThichUpdated

    private val _isyeuThichDeleted = MutableLiveData<Boolean>()
    val isyeuThichDeleted: LiveData<Boolean> get() = _isyeuThichDeleted

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getListyeuThich() {
        viewModelScope.launch {
            try {

                val apiService : YeuThichService = CreateService.createService()
                val YeuThichRepository = YeuThichRepository(apiService)
                _yeuThichList.value = YeuThichRepository.getListYeuThich()
            } catch (e: Exception) {
                Log.e("yeuThichViewModel", "Error fetching yeuThich list", e)
                _errorMessage.value = "Error fetching yeuThich list: ${e.message}"
            }
        }
    }

    fun addyeuThich(yeuThich: YeuThichModel) {
        viewModelScope.launch {
            try {
                val apiService : YeuThichService = CreateService.createService()
                val YeuThichRepository = YeuThichRepository(apiService)
                _isyeuThichAdded.value = YeuThichRepository.addYeuThich(yeuThich) != null
            } catch (e: Exception) {
                Log.e("yeuThichViewModel", "Error adding yeuThich", e)
                _errorMessage.value = "Error adding yeuThich: ${e.message}"
            }
        }
    }


    fun updateyeuThich(id: String, yeuThich: YeuThichModel) {
        viewModelScope.launch {
            try {
                val apiService : YeuThichService = CreateService.createService()
                val YeuThichRepository = YeuThichRepository(apiService)
                _isyeuThichUpdated.value = YeuThichRepository.updateYeuThich(id, yeuThich) != null
            } catch (e: Exception) {
                Log.e("yeuThichViewModel", "Error updating yeuThich", e)
                _errorMessage.value = "Error updating yeuThich: ${e.message}"
            }
        }
    }

    fun deleteyeuThich(id: String) {
        viewModelScope.launch {
            try {
                val apiService : YeuThichService = CreateService.createService()
                val YeuThichRepository = YeuThichRepository(apiService)
                _isyeuThichDeleted.value = YeuThichRepository.deleteYeuThich(id)
            } catch (e: Exception) {
                Log.e("yeuThichViewModel", "Error deleting yeuThich", e)
                _errorMessage.value = "Error deleting yeuThich: ${e.message}"
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}