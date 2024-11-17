package com.example.app_datn_haven_inn.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.NguoiDungModel
import com.example.app_datn_haven_inn.database.repository.LoginRepository
import com.example.app_datn_haven_inn.database.service.LoginService
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {

    private val _loginList = MutableLiveData<List<NguoiDungModel>?>()
    val loginList: LiveData<List<NguoiDungModel>?> get() = _loginList

    private val _login = MutableLiveData<NguoiDungModel?>()
    val login: LiveData<NguoiDungModel?> get() = _login

    private val _isloginAdded = MutableLiveData<Boolean>()
    val isloginAdded: LiveData<Boolean> get() = _isloginAdded

    private val _isloginUpdated = MutableLiveData<Boolean>()
    val isloginUpdated: LiveData<Boolean> get() = _isloginUpdated

    private val _isloginDeleted = MutableLiveData<Boolean>()
    val isloginDeleted: LiveData<Boolean> get() = _isloginDeleted

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getListlogin() {
        viewModelScope.launch {
            try {

                val apiService : LoginService = CreateService.createService()
                val LoginRepository = LoginRepository(apiService)
                _loginList.value = LoginRepository.getListNguoiDung()
            } catch (e: Exception) {
                Log.e("loginViewModel", "Error fetching login list", e)
                _errorMessage.value = "Error fetching login list: ${e.message}"
            }
        }
    }

    fun addlogin(login: NguoiDungModel) {
        viewModelScope.launch {
            try {
                val apiService : LoginService = CreateService.createService()
                val LoginRepository = LoginRepository(apiService)
                _isloginAdded.value = LoginRepository.addNguoiDung(login) != null
            } catch (e: Exception) {
                Log.e("loginViewModel", "Error adding login", e)
                _errorMessage.value = "Error adding login: ${e.message}"
            }
        }
    }


    fun updatelogin(id: String, login: NguoiDungModel) {
        viewModelScope.launch {
            try {
                val apiService : LoginService = CreateService.createService()
                val LoginRepository = LoginRepository(apiService)
                _isloginUpdated.value = LoginRepository.updateNguoiDung(id, login) != null
            } catch (e: Exception) {
                Log.e("loginViewModel", "Error updating login", e)
                _errorMessage.value = "Error updating login: ${e.message}"
            }
        }
    }


    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}