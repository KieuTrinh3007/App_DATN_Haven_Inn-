package com.example.app_datn_haven_inn.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.TienNghiModel
import com.example.app_datn_haven_inn.database.repository.TienNghiRepository
import com.example.app_datn_haven_inn.database.service.TienNghiService
import kotlinx.coroutines.launch

class TienNghiViewModel : BaseViewModel() {

    private val _tienNghiList = MutableLiveData<List<TienNghiModel>?>()
    val tienNghiList: LiveData<List<TienNghiModel>?> get() = _tienNghiList

    private val _tienNghi = MutableLiveData<TienNghiModel?>()
    val tienNghi: LiveData<TienNghiModel?> get() = _tienNghi

    private val _istienNghiAdded = MutableLiveData<Boolean>()
    val istienNghiAdded: LiveData<Boolean> get() = _istienNghiAdded

    private val _istienNghiUpdated = MutableLiveData<Boolean>()
    val istienNghiUpdated: LiveData<Boolean> get() = _istienNghiUpdated

    private val _istienNghiDeleted = MutableLiveData<Boolean>()
    val istienNghiDeleted: LiveData<Boolean> get() = _istienNghiDeleted

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getListtienNghi() {
        viewModelScope.launch {
            try {

                val apiService : TienNghiService = CreateService.createService()
                val TienNghiRepository = TienNghiRepository(apiService)
                _tienNghiList.value = TienNghiRepository.getListTienNghi()
            } catch (e: Exception) {
                Log.e("tienNghiViewModel", "Error fetching tienNghi list", e)
                _errorMessage.value = "Error fetching tienNghi list: ${e.message}"
            }
        }
    }

    fun addtienNghi(tienNghi: TienNghiModel) {
        viewModelScope.launch {
            try {
                val apiService : TienNghiService = CreateService.createService()
                val TienNghiRepository = TienNghiRepository(apiService)
                _istienNghiAdded.value = TienNghiRepository.addTienNghi(tienNghi) != null
            } catch (e: Exception) {
                Log.e("tienNghiViewModel", "Error adding tienNghi", e)
                _errorMessage.value = "Error adding tienNghi: ${e.message}"
            }
        }
    }


    fun updatetienNghi(id: String, tienNghi: TienNghiModel) {
        viewModelScope.launch {
            try {
                val apiService : TienNghiService = CreateService.createService()
                val TienNghiRepository = TienNghiRepository(apiService)
                _istienNghiUpdated.value = TienNghiRepository.updateTienNghi(id, tienNghi) != null
            } catch (e: Exception) {
                Log.e("tienNghiViewModel", "Error updating tienNghi", e)
                _errorMessage.value = "Error updating tienNghi: ${e.message}"
            }
        }
    }

    fun deletetienNghi(id: String) {
        viewModelScope.launch {
            try {
                val apiService : TienNghiService = CreateService.createService()
                val TienNghiRepository = TienNghiRepository(apiService)
                _istienNghiDeleted.value = TienNghiRepository.deleteTienNghi(id)
            } catch (e: Exception) {
                Log.e("tienNghiViewModel", "Error deleting tienNghi", e)
                _errorMessage.value = "Error deleting tienNghi: ${e.message}"
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}