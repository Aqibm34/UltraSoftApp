package com.example.ultrasoft.ui.fragment.complain.all

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ultrasoft.data.model.complain.AllComplaintsResponse
import com.example.ultrasoft.data.model.user.BlockResponse
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.data.repository.ComplainRepository
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


    fun callApiGetAllComplaint(
        token: String
    ) {
        _allComplainResponse.value = Resource.loading()
        viewModelScope.launch {
            _allComplainResponse.value = repository.callApiGetAllComplaint(token)
        }
    }

}