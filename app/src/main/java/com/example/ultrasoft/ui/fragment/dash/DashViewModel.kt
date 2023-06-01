package com.example.ultrasoft.ui.fragment.dash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ultrasoft.data.model.login.LoginRequest
import com.example.ultrasoft.data.model.login.LoginResponse
import com.example.ultrasoft.data.model.login.LogoutResponse
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.data.repository.DashRepository
import com.example.ultrasoft.utility.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashViewModel @Inject constructor(private val repository: DashRepository) : ViewModel() {

    private val _logoutResponse = SingleLiveEvent<Resource<LogoutResponse>>()
    val logoutResponse: LiveData<Resource<LogoutResponse>>
        get() = _logoutResponse

    fun callApiLogout(token: String) {
        _logoutResponse.value = Resource.loading()
        viewModelScope.launch {
            _logoutResponse.value = repository.callApiLogout(token)
        }
    }

}