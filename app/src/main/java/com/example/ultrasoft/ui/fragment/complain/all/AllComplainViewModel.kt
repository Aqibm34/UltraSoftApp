package com.example.ultrasoft.ui.fragment.complain.all

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ultrasoft.data.model.CommonResponse
import com.example.ultrasoft.data.model.complain.AllComplaintsResponse
import com.example.ultrasoft.data.model.complain.SingleComplainResponse
import com.example.ultrasoft.data.model.user.BlockResponse
import com.example.ultrasoft.data.model.user.engineer.AllEngineerResponse
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.data.repository.ComplainRepository
import com.example.ultrasoft.utility.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AllComplainViewModel @Inject constructor(private val repository: ComplainRepository) :
    ViewModel() {

    private val _allComplainResponse = MutableLiveData<Resource<AllComplaintsResponse>>()
    val allComplainResponse: LiveData<Resource<AllComplaintsResponse>>
        get() = _allComplainResponse

    private val _assignedResponse = MutableLiveData<Resource<CommonResponse>>()
    val assignedResponse: LiveData<Resource<CommonResponse>>
        get() = _assignedResponse

    private val _allEngineerResponse = SingleLiveEvent<Resource<AllEngineerResponse>>()
    val allEngineerResponse: LiveData<Resource<AllEngineerResponse>>
        get() = _allEngineerResponse

    private val _resolveComplainResponse = MutableLiveData<Resource<SingleComplainResponse>>()
    val resolveComplainResponse: LiveData<Resource<SingleComplainResponse>>
        get() = _resolveComplainResponse

    private val _closeComplainResponse = MutableLiveData<Resource<SingleComplainResponse>>()
    val closeComplainResponse: LiveData<Resource<SingleComplainResponse>>
        get() = _closeComplainResponse


    fun callApiGetAllComplaint(
        url: String,
        token: String,
        status: String
    ) {
        _allComplainResponse.value = Resource.loading()
        viewModelScope.launch {
            _allComplainResponse.value = repository.callApiGetAllComplaint(url, token, status)
        }
    }


    fun callAdminApiAssignComplain(
        token: String,
        complainId: String,
        engineerId: String,
    ) {
        _assignedResponse.value = Resource.loading()
        viewModelScope.launch {
            _assignedResponse.value =
                repository.callAdminApiAssignComplain(token, complainId, engineerId)
        }
    }

    fun callApiGetAllEngineer(token: String) {
        _allEngineerResponse.value = Resource.loading()
        viewModelScope.launch {
            _allEngineerResponse.value = repository.callApiGetAllEngineer(token)
        }
    }

    fun callApiEngResolveComplaint(
        token: String,
        complainId: String,
    ) {
        _resolveComplainResponse.value = Resource.loading()
        viewModelScope.launch {
            _resolveComplainResponse.value =
                repository.callApiEngResolveComplaint(token, complainId)
        }
    }

    fun callApiCustomerCloseComplain(
        token: String,
        complainId: String,
    ) {
        _closeComplainResponse.value = Resource.loading()
        viewModelScope.launch {
            _closeComplainResponse.value =
                repository.callApiCustomerCloseComplain(token, complainId)
        }
    }

    fun callApiAdminCloseComplaint(
        token: String,
        complainId: String,
    ) {
        _closeComplainResponse.value = Resource.loading()
        viewModelScope.launch {
            _closeComplainResponse.value = repository.callApiAdminCloseComplaint(token, complainId)
        }
    }

}