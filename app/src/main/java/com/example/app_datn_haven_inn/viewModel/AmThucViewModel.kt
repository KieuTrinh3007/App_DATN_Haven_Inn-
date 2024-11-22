package com.example.app_datn_haven_inn.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.AmThucModel
import com.example.app_datn_haven_inn.database.repository.AmThucRepository
import com.example.app_datn_haven_inn.database.service.AmThucService
import kotlinx.coroutines.launch

class AmThucViewModel : BaseViewModel() {
    private val _amThucList = MutableLiveData<List<AmThucModel>?>()
    val amThucList: LiveData<List<AmThucModel>?> get() = _amThucList

    private val _amThuc = MutableLiveData<AmThucModel?>()
    val amThuc: LiveData<AmThucModel?> get() = _amThuc

    private val _isamThucAdded = MutableLiveData<Boolean>()
    val isamThucAdded: LiveData<Boolean> get() = _isamThucAdded

    private val _isamThucUpdated = MutableLiveData<Boolean>()
    val isamThucUpdated: LiveData<Boolean> get() = _isamThucUpdated

    private val _isamThucDeleted = MutableLiveData<Boolean>()
    val isamThucDeleted: LiveData<Boolean> get() = _isamThucDeleted

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage
//
fun getListamThuc() {
    viewModelScope.launch {
        try {
            // Tạo dịch vụ API
            val apiService: AmThucService = CreateService.createService()
            val amThucRepository = AmThucRepository(apiService)

            // Lấy danh sách món ăn
            _amThucList.value = amThucRepository.getListAmThuc()
        } catch (e: Exception) {
            // Kiểm tra loại lỗi và log chi tiết
            when (e) {
                is retrofit2.HttpException -> {
                    // Lỗi HTTP (ví dụ: 401, 404, 500)
                    Log.e("amThucViewModel", "HTTP Error: ${e.code()} - ${e.message()}")
                    _errorMessage.value = "HTTP Error: ${e.code()} - ${e.message()}"
                }
                is java.net.UnknownHostException -> {
                    // Lỗi kết nối mạng (ví dụ: không tìm thấy server)
                    Log.e("amThucViewModel", "Network Error: ${e.message}")
                    _errorMessage.value = "Network Error: Check your internet connection."
                }
                is java.net.SocketTimeoutException -> {
                    // Lỗi timeout
                    Log.e("amThucViewModel", "Timeout Error: ${e.message}")
                    _errorMessage.value = "Timeout Error: The request took too long."
                }
                else -> {
                    // Lỗi chung
                    Log.e("amThucViewModel", "Unknown Error: ${e.message}")
                    _errorMessage.value = "An unknown error occurred: ${e.message}"
                }
            }
        }
    }
}


    fun addamThuc(amThuc: AmThucModel) {
        viewModelScope.launch {
            try {
                val apiService : AmThucService = CreateService.createService()
                val amThucRepository = AmThucRepository(apiService)
                _isamThucAdded.value = amThucRepository.addAmThuc(amThuc) != null
            } catch (e: Exception) {
                Log.e("amThucViewModel", "Error adding amThuc", e)
                _errorMessage.value = "Error adding amThuc: ${e.message}"
            }
        }
    }


    fun updateamThuc(id: String, amThuc: AmThucModel) {
        viewModelScope.launch {
            try {
                val apiService : AmThucService = CreateService.createService()
                val amThucRepository = AmThucRepository(apiService)
                _isamThucUpdated.value = amThucRepository.updateAmThuc(id, amThuc) != null
            } catch (e: Exception) {
                Log.e("amThucViewModel", "Error updating amThuc", e)
                _errorMessage.value = "Error updating amThuc: ${e.message}"
            }
        }
    }

    fun deleteamThuc(id: String) {
        viewModelScope.launch {
            try {
                val apiService : AmThucService = CreateService.createService()
                val amThucRepository = AmThucRepository(apiService)
                _isamThucDeleted.value = amThucRepository.deleteAmThuc(id)
            } catch (e: Exception) {
                Log.e("amThucViewModel", "Error deleting amThuc", e)
                _errorMessage.value = "Error deleting amThuc: ${e.message}"
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}