package com.example.ultrasoft.ui.fragment.user.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ultrasoft.data.model.user.admin.CreateAdminRequest
import com.example.ultrasoft.data.model.user.admin.CreateAdminResponse
import com.example.ultrasoft.data.model.user.customer.CreateCustomerRequest
import com.example.ultrasoft.data.model.user.customer.CreateCustomerResponse
import com.example.ultrasoft.data.model.user.engineer.CreateEngineerRequest
import com.example.ultrasoft.data.model.user.engineer.CreateEngineerResponse
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.data.repository.UsersRepository
import com.example.ultrasoft.utility.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateUserViewModel @Inject constructor(private val repository: UsersRepository):ViewModel(){

    private val _createCustomerResponse = SingleLiveEvent<Resource<CreateCustomerResponse>>()
    val createCustomerResponse: LiveData<Resource<CreateCustomerResponse>>
        get() = _createCustomerResponse

    private val _createAdminResponse = SingleLiveEvent<Resource<CreateAdminResponse>>()
    val createAdminResponse: LiveData<Resource<CreateAdminResponse>>
        get() = _createAdminResponse

    private val _createEngineerResponse = SingleLiveEvent<Resource<CreateEngineerResponse>>()
    val createEngineerResponse: LiveData<Resource<CreateEngineerResponse>>
        get() = _createEngineerResponse


    fun callApiCreateCustomer(token: String, body: CreateCustomerRequest) {
        _createCustomerResponse.value = Resource.loading()
        viewModelScope.launch {
            _createCustomerResponse.value = repository.callApiCreateCustomer(token, body)
        }
    }


    fun callApiCreateAdmin(token: String, body: CreateAdminRequest) {
        _createAdminResponse.value = Resource.loading()
        viewModelScope.launch {
            _createAdminResponse.value = repository.callApiCreateAdmin(token, body)
        }
    }

    fun callApiCreateEngineer(token: String, body: CreateEngineerRequest) {
        _createEngineerResponse.value = Resource.loading()
        viewModelScope.launch {
            _createEngineerResponse.value = repository.callApiCreateEngineer(token, body)
        }
    }


}