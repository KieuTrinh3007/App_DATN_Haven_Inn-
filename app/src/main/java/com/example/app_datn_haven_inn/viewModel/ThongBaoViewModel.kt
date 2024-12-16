package com.example.app_datn_haven_inn.viewModel

import android.content.Context
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

    private val _unreadCount = MutableLiveData<Int>()
    val unreadCount: LiveData<Int> get() = _unreadCount
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    // Thêm trạng thái isLoading
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getListthongBao() {
        viewModelScope.launch {
            try {
                _isLoading.value = true // Bắt đầu tải
                val apiService: ThongBaoService = CreateService.createService()
                val thongBaoRepository = ThongBaoRepository(apiService)
                _thongBaoList.value = thongBaoRepository.getListThongBao()
            } catch (e: Exception) {
                Log.e("thongBaoViewModel", "Error fetching thongBao list", e)
                _errorMessage.value = "Error fetching thongBao list: ${e.message}"
            } finally {
                _isLoading.value = false // Kết thúc tải
            }
        }
    }

    fun addthongBao(thongBao: ThongBaoModel) {
        viewModelScope.launch {
            try {
                _isLoading.value = true // Bắt đầu tải
                val apiService: ThongBaoService = CreateService.createService()
                val thongBaoRepository = ThongBaoRepository(apiService)
                _isthongBaoAdded.value = thongBaoRepository.addThongBao(thongBao) != null
            } catch (e: Exception) {
                Log.e("thongBaoViewModel", "Error adding thongBao", e)
                _errorMessage.value = "Error adding thongBao: ${e.message}"
            } finally {
                _isLoading.value = false // Kết thúc tải
            }
        }
    }

    fun updatethongBao(id: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true // Bắt đầu tải
                val apiService: ThongBaoService = CreateService.createService()
                val thongBaoRepository = ThongBaoRepository(apiService)
                _isthongBaoUpdated.value = thongBaoRepository.updateThongBao(id) != null
            } catch (e: Exception) {
                Log.e("thongBaoViewModel", "Error updating thongBao", e)
                _errorMessage.value = "Error updating thongBao: ${e.message}"
            } finally {
                _isLoading.value = false // Kết thúc tải
            }
        }
    }
    fun getUnreadCount(): Int {
        return _thongBaoList.value?.count { !it.trangThai } ?: 0
    }
    fun updateUnreadCount() {
        _unreadCount.value = _thongBaoList.value?.count { !it.trangThai } ?: 0
    }

    fun deletethongBao(id: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true // Bắt đầu tải
                val apiService: ThongBaoService = CreateService.createService()
                val thongBaoRepository = ThongBaoRepository(apiService)
                _isthongBaoDeleted.value = thongBaoRepository.deleteThongBao(id)
            } catch (e: Exception) {
                Log.e("thongBaoViewModel", "Error deleting thongBao", e)
                _errorMessage.value = "Error deleting thongBao: ${e.message}"
            } finally {
                _isLoading.value = false // Kết thúc tải
            }
        }
    }

    fun toggleTrangThai(position: Int) {
        _thongBaoList.value?.let { thongBaoList ->
            val updatedList = thongBaoList.toMutableList()
            val thongBao = updatedList[position]
            thongBao.trangThai = !thongBao.trangThai
            _thongBaoList.value = updatedList
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
