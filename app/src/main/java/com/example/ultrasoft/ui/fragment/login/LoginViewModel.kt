package com.example.ultrasoft.ui.fragment.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ultrasoft.base.BaseRepository
import com.example.ultrasoft.data.model.login.LoginRequest
import com.example.ultrasoft.data.model.login.LoginResponse
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.data.repository.LoginRepository
import com.example.ultrasoft.utility.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {


    private val _loginResponse = SingleLiveEvent<Resource<LoginResponse>>()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse

    fun callApiLogin(body:LoginRequest,fcmToken:String) {
        _loginResponse.value = Resource.loading()
        viewModelScope.launch {
            _loginResponse.value = repository.callApiLogin(body,fcmToken)
        }
    }
}