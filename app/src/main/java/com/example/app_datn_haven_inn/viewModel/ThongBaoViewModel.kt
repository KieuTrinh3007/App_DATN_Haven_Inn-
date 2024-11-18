package com.example.app_datn_haven_inn.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.ThongBaoModel
import com.example.app_datn_haven_inn.database.repository.ThongBaoRepository
import com.example.app_datn_haven_inn.database.service.ThongBaoService
import kotlinx.coroutines.launch

class ThongBaoViewModel : BaseViewModel() {
    private val _thongBaoList = MutableLiveData<List<ThongBaoModel>?>()
    val thongBaoList: LiveData<List<ThongBaoModel>?> get() = _thongBaoList

    private val _thongBao = MutableLiveData<ThongBaoModel?>()
    val thongBao: LiveData<ThongBaoModel?> get() = _thongBao

    private val _isthongBaoAdded = MutableLiveData<Boolean>()
    val isthongBaoAdded: LiveData<Boolean> get() = _isthongBaoAdded

    private val _isthongBaoUpdated = MutableLiveData<Boolean>()
    val isthongBaoUpdated: LiveData<Boolean> get() = _isthongBaoUpdated

    private val _isthongBaoDeleted = MutableLiveData<Boolean>()
    val isthongBaoDeleted: LiveData<Boolean> get() = _isthongBaoDeleted

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getListthongBao() {
        viewModelScope.launch {
            try {

                val apiService : ThongBaoService = CreateService.createService()
                val ThongBaoRepository = ThongBaoRepository(apiService)
                _thongBaoList.value = ThongBaoRepository.getListThongBao()
            } catch (e: Exception) {
                Log.e("thongBaoViewModel", "Error fetching thongBao list", e)
                _errorMessage.value = "Error fetching thongBao list: ${e.message}"
            }
        }
    }

    fun addthongBao(thongBao: ThongBaoModel) {
        viewModelScope.launch {
            try {
                val apiService : ThongBaoService = CreateService.createService()
                val ThongBaoRepository = ThongBaoRepository(apiService)
                _isthongBaoAdded.value = ThongBaoRepository.addThongBao(thongBao) != null
            } catch (e: Exception) {
                Log.e("thongBaoViewModel", "Error adding thongBao", e)
                _errorMessage.value = "Error adding thongBao: ${e.message}"
            }
        }
    }


    fun updatethongBao(id: String, thongBao: ThongBaoModel) {
        viewModelScope.launch {
            try {
                val apiService : ThongBaoService = CreateService.createService()
                val ThongBaoRepository = ThongBaoRepository(apiService)
                _isthongBaoUpdated.value = ThongBaoRepository.updateThongBao(id, thongBao) != null
            } catch (e: Exception) {
                Log.e("thongBaoViewModel", "Error updating thongBao", e)
                _errorMessage.value = "Error updating thongBao: ${e.message}"
            }
        }
    }

    fun deletethongBao(id: String) {
        viewModelScope.launch {
            try {
                val apiService : ThongBaoService = CreateService.createService()
                val ThongBaoRepository = ThongBaoRepository(apiService)
                _isthongBaoDeleted.value = ThongBaoRepository.deleteThongBao(id)
            } catch (e: Exception) {
                Log.e("thongBaoViewModel", "Error deleting thongBao", e)
                _errorMessage.value = "Error deleting thongBao: ${e.message}"
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}