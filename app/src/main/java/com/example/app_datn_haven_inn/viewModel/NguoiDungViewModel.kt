package com.example.app_datn_haven_inn.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.NguoiDungModel
import com.example.app_datn_haven_inn.database.repository.NguoiDungRepository
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import kotlinx.coroutines.launch
import java.io.File

class NguoiDungViewModel : BaseViewModel() {

    private val nguoiDungService: NguoiDungService = CreateService.createService()
    private val nguoiDungRepository: NguoiDungRepository = NguoiDungRepository(nguoiDungService)

    private val _nguoiDungList = MutableLiveData<List<NguoiDungModel>?>()
    val nguoiDungList: LiveData<List<NguoiDungModel>?> get() = _nguoiDungList

    private val _nguoiDung = MutableLiveData<NguoiDungModel?>()
    val nguoiDung: LiveData<NguoiDungModel?> get() = _nguoiDung

    private val _isnguoiDungAdded = MutableLiveData<Boolean>()
    val isnguoiDungAdded: LiveData<Boolean> get() = _isnguoiDungAdded

    private val _isnguoiDungUpdated = MutableLiveData<Boolean>()
    val isnguoiDungUpdated: LiveData<Boolean> get() = _isnguoiDungUpdated

    private val _isnguoiDungDeleted = MutableLiveData<Boolean>()
    val isnguoiDungDeleted: LiveData<Boolean> get() = _isnguoiDungDeleted

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getListNguoiDung() {
        viewModelScope.launch {
            try {
                _nguoiDungList.value = nguoiDungRepository.getListNguoiDung()
            } catch (e: Exception) {
                Log.e("NguoiDungViewModel", "Error fetching nguoiDung list", e)
                _errorMessage.value = "Error fetching nguoiDung list: ${e.message}"
            }
        }
    }


    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
