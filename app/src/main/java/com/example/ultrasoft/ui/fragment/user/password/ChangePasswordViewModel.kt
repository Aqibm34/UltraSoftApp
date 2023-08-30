package com.example.ultrasoft.ui.fragment.user.password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ultrasoft.data.model.CommonMessageResponse
import com.example.ultrasoft.data.model.CommonResponse
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.data.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(private val repository: UsersRepository) :
    ViewModel() {

    private val _changePasswordResponse = MutableLiveData<Resource<CommonMessageResponse>>()
    val changePasswordResponse: LiveData<Resource<CommonMessageResponse>>
        get() = _changePasswordResponse

    private val _resetPasswordResponse = MutableLiveData<Resource<CommonMessageResponse>>()
    val resetPasswordResponse: LiveData<Resource<CommonMessageResponse>>
        get() = _resetPasswordResponse

    fun callApiChangePassword(
        url: String,
        token: String,
        currentPassword: String,
        newPassword: String,
    ) {
        _changePasswordResponse.value = Resource.loading()
        viewModelScope.launch {
            _changePasswordResponse.value =
                repository.callApiChangePassword(url, token, currentPassword, newPassword)
        }

    }
    fun callApiResetPassword(
        url: String,
        mobile: String,
    ) {
        _changePasswordResponse.value = Resource.loading()
        viewModelScope.launch {
            _changePasswordResponse.value =
                repository.callApiResetPassword(url, mobile)
        }

    }
}