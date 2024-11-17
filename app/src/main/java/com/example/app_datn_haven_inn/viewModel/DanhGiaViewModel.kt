package com.example.app_datn_haven_inn.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.DanhGiaModel
import com.example.app_datn_haven_inn.database.repository.DanhGiaRepository
import com.example.app_datn_haven_inn.database.service.DanhGiaService
import kotlinx.coroutines.launch

class DanhGiaViewModel : BaseViewModel() {
    private val _danhGiaList = MutableLiveData<List<DanhGiaModel>?>()
    val danhGiaList: LiveData<List<DanhGiaModel>?> get() = _danhGiaList

    private val _danhGia = MutableLiveData<DanhGiaModel?>()
    val danhGia: LiveData<DanhGiaModel?> get() = _danhGia

    private val _isdanhGiaAdded = MutableLiveData<Boolean>()
    val isdanhGiaAdded: LiveData<Boolean> get() = _isdanhGiaAdded

    private val _isdanhGiaUpdated = MutableLiveData<Boolean>()
    val isdanhGiaUpdated: LiveData<Boolean> get() = _isdanhGiaUpdated

    private val _isdanhGiaDeleted = MutableLiveData<Boolean>()
    val isdanhGiaDeleted: LiveData<Boolean> get() = _isdanhGiaDeleted

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getListdanhGia() {
        viewModelScope.launch {
            try {

                val apiService : DanhGiaService = CreateService.createService()
                val DanhGiaRepository = DanhGiaRepository(apiService)
                _danhGiaList.value = DanhGiaRepository.getListDanhGia()
            } catch (e: Exception) {
                Log.e("danhGiaViewModel", "Error fetching danhGia list", e)
                _errorMessage.value = "Error fetching danhGia list: ${e.message}"
            }
        }
    }

    fun adddanhGia(danhGia: DanhGiaModel) {
        viewModelScope.launch {
            try {
                val apiService : DanhGiaService = CreateService.createService()
                val DanhGiaRepository = DanhGiaRepository(apiService)
                _isdanhGiaAdded.value = DanhGiaRepository.addDanhGia(danhGia) != null
            } catch (e: Exception) {
                Log.e("danhGiaViewModel", "Error adding danhGia", e)
                _errorMessage.value = "Error adding danhGia: ${e.message}"
            }
        }
    }


    fun updatedanhGia(id: String, danhGia: DanhGiaModel) {
        viewModelScope.launch {
            try {
                val apiService : DanhGiaService = CreateService.createService()
                val DanhGiaRepository = DanhGiaRepository(apiService)
                _isdanhGiaUpdated.value = DanhGiaRepository.updateDanhGia(id, danhGia) != null
            } catch (e: Exception) {
                Log.e("danhGiaViewModel", "Error updating danhGia", e)
                _errorMessage.value = "Error updating danhGia: ${e.message}"
            }
        }
    }

    fun deletedanhGia(id: String) {
        viewModelScope.launch {
            try {
                val apiService : DanhGiaService = CreateService.createService()
                val DanhGiaRepository = DanhGiaRepository(apiService)
                _isdanhGiaDeleted.value = DanhGiaRepository.deleteDanhGia(id)
            } catch (e: Exception) {
                Log.e("danhGiaViewModel", "Error deleting danhGia", e)
                _errorMessage.value = "Error deleting danhGia: ${e.message}"
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}