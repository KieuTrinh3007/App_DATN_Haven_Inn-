package com.example.app_datn_haven_inn.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.DichVuModel
import com.example.app_datn_haven_inn.database.repository.DichVuRepository
import com.example.app_datn_haven_inn.database.service.DichVuService
import kotlinx.coroutines.launch

class DichVuViewModel : BaseViewModel() {
    private val _dichVuList = MutableLiveData<List<DichVuModel>?>()
    val dichVuList: LiveData<List<DichVuModel>?> get() = _dichVuList

    private val _dichVu = MutableLiveData<DichVuModel?>()
    val dichVu: LiveData<DichVuModel?> get() = _dichVu

    private val _isdichVuAdded = MutableLiveData<Boolean>()
    val isdichVuAdded: LiveData<Boolean> get() = _isdichVuAdded

    private val _isdichVuUpdated = MutableLiveData<Boolean>()
    val isdichVuUpdated: LiveData<Boolean> get() = _isdichVuUpdated

    private val _isdichVuDeleted = MutableLiveData<Boolean>()
    val isdichVuDeleted: LiveData<Boolean> get() = _isdichVuDeleted

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getListdichVu() {
        viewModelScope.launch {
            try {

                val apiService : DichVuService = CreateService.createService()
                val DichVuRepository = DichVuRepository(apiService)
                _dichVuList.value = DichVuRepository.getListDichVu()
            } catch (e: Exception) {
                Log.e("dichVuViewModel", "Error fetching dichVu list", e)
                _errorMessage.value = "Error fetching dichVu list: ${e.message}"
            }
        }
    }

    fun adddichVu(dichVu: DichVuModel) {
        viewModelScope.launch {
            try {
                val apiService : DichVuService = CreateService.createService()
                val DichVuRepository = DichVuRepository(apiService)
                _isdichVuAdded.value = DichVuRepository.addDichVu(dichVu) != null
            } catch (e: Exception) {
                Log.e("dichVuViewModel", "Error adding dichVu", e)
                _errorMessage.value = "Error adding dichVu: ${e.message}"
            }
        }
    }


    fun updatedichVu(id: String, dichVu: DichVuModel) {
        viewModelScope.launch {
            try {
                val apiService : DichVuService = CreateService.createService()
                val DichVuRepository = DichVuRepository(apiService)
                _isdichVuUpdated.value = DichVuRepository.updateDichVu(id, dichVu) != null
            } catch (e: Exception) {
                Log.e("dichVuViewModel", "Error updating dichVu", e)
                _errorMessage.value = "Error updating dichVu: ${e.message}"
            }
        }
    }

    fun deletedichVu(id: String) {
        viewModelScope.launch {
            try {
                val apiService : DichVuService = CreateService.createService()
                val DichVuRepository = DichVuRepository(apiService)
                _isdichVuDeleted.value = DichVuRepository.deleteDichVu(id)
            } catch (e: Exception) {
                Log.e("dichVuViewModel", "Error deleting dichVu", e)
                _errorMessage.value = "Error deleting dichVu: ${e.message}"
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}