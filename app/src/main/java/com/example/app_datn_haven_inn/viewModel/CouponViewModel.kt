package com.example.app_datn_haven_inn.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.CouponModel
import com.example.app_datn_haven_inn.database.repository.CouponRepository
import com.example.app_datn_haven_inn.database.service.CouponService
import com.example.app_datn_haven_inn.utils.SharedPrefsHelper
import kotlinx.coroutines.launch

class CouponViewModel : BaseViewModel()  {

    private val _couponList = MutableLiveData<List<CouponModel>?>()
    val couponList: LiveData<List<CouponModel>?> get() = _couponList

    private val _coupon = MutableLiveData<CouponModel?>()
    val coupon: LiveData<CouponModel?> get() = _coupon

    private val _iscouponAdded = MutableLiveData<Boolean>()
    val iscouponAdded: LiveData<Boolean> get() = _iscouponAdded

    private val _iscouponUpdated = MutableLiveData<Boolean>()
    val iscouponUpdated: LiveData<Boolean> get() = _iscouponUpdated

    private val _iscouponDeleted = MutableLiveData<Boolean>()
    val iscouponDeleted: LiveData<Boolean> get() = _iscouponDeleted

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getListcoupon() {
        viewModelScope.launch {
            try {

                val apiService : CouponService = CreateService.createService()
                val CouponRepository = CouponRepository(apiService)
                _couponList.value = CouponRepository.getListCoupon()
            } catch (e: Exception) {
                Log.e("couponViewModel", "Error fetching coupon list", e)
                _errorMessage.value = "Error fetching coupon list: ${e.message}"
            }
        }
    }

    fun addcoupon(coupon: CouponModel) {
        viewModelScope.launch {
            try {
                val apiService : CouponService = CreateService.createService()
                val CouponRepository = CouponRepository(apiService)
                _iscouponAdded.value = CouponRepository.addCoupon(coupon) != null
            } catch (e: Exception) {
                Log.e("couponViewModel", "Error adding coupon", e)
                _errorMessage.value = "Error adding coupon: ${e.message}"
            }
        }
    }


    fun updatecoupon(id: String, coupon: CouponModel) {
        viewModelScope.launch {
            try {
                val apiService : CouponService = CreateService.createService()
                val CouponRepository = CouponRepository(apiService)
                _iscouponUpdated.value = CouponRepository.updateCoupon(id, coupon) != null
            } catch (e: Exception) {
                Log.e("couponViewModel", "Error updating coupon", e)
                _errorMessage.value = "Error updating coupon: ${e.message}"
            }
        }
    }

    fun deletecoupon(id: String) {
        viewModelScope.launch {
            try {
                val apiService : CouponService = CreateService.createService()
                val CouponRepository = CouponRepository(apiService)
                _iscouponDeleted.value = CouponRepository.deleteCoupon(id)
            } catch (e: Exception) {
                Log.e("couponViewModel", "Error deleting coupon", e)
                _errorMessage.value = "Error deleting coupon: ${e.message}"
            }
        }
    }
    fun getCouponListByUser(context: Context) {
        viewModelScope.launch {
            try {
                val userId = SharedPrefsHelper.getIdNguoiDung(context)
                if (userId != null) {
                    val apiService: CouponService = CreateService.createService()
                    val couponRepository = CouponRepository(apiService)
                    val result = couponRepository.getListCouponByUser(userId)
                    _couponList.value = result ?: emptyList()
                } else {
                    _errorMessage.value = "Không tìm thấy ID người dùng."
                }
            } catch (e: Exception) {
                Log.e("CouponViewModel", "Error fetching coupon list by user", e)
                _errorMessage.value = "Error fetching coupon list by user: ${e.message}"
            }
        }
    }


    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}