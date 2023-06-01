package com.example.ultrasoft.ui.fragment.user.all

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ultrasoft.data.model.user.admin.AllAdminResponse
import com.example.ultrasoft.data.model.user.admin.CreateAdminRequest
import com.example.ultrasoft.data.model.user.admin.CreateAdminResponse
import com.example.ultrasoft.data.model.user.customer.AllCustomerResponse
import com.example.ultrasoft.data.model.user.customer.CreateCustomerRequest
import com.example.ultrasoft.data.model.user.customer.CreateCustomerResponse
import com.example.ultrasoft.data.model.user.engineer.AllEngineerResponse
import com.example.ultrasoft.data.model.user.engineer.CreateEngineerRequest
import com.example.ultrasoft.data.model.user.engineer.CreateEngineerResponse
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.data.repository.UsersRepository
import com.example.ultrasoft.utility.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllUsersViewModel @Inject constructor(private val repository: UsersRepository) : ViewModel() {

    private val _allAdminResponse = SingleLiveEvent<Resource<AllAdminResponse>>()
    val allAdminResponse: LiveData<Resource<AllAdminResponse>>
        get() = _allAdminResponse

    private val _allCustomerResponse = SingleLiveEvent<Resource<AllCustomerResponse>>()
    val allCustomerResponse: LiveData<Resource<AllCustomerResponse>>
        get() = _allCustomerResponse


    private val _allEngineerResponse = SingleLiveEvent<Resource<AllEngineerResponse>>()
    val allEngineerResponse: LiveData<Resource<AllEngineerResponse>>
        get() = _allEngineerResponse


    fun callApiGetAllAdmin(token: String) {
        _allAdminResponse.value = Resource.loading()
        viewModelScope.launch {
            _allAdminResponse.value = repository.callApiGetAllAdmin(token)
        }
    }

    fun callApiGetAllEngineer(token: String) {
        _allEngineerResponse.value = Resource.loading()
        viewModelScope.launch {
            _allEngineerResponse.value = repository.callApiGetAllEngineer(token)
        }
    }

    fun callApiGetAllCustomer(token: String) {
        _allCustomerResponse.value = Resource.loading()
        viewModelScope.launch {
            _allCustomerResponse.value = repository.callApiGetAllCustomer(token)
        }
    }


}