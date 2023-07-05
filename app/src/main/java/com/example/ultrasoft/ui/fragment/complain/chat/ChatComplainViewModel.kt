package com.example.ultrasoft.ui.fragment.complain.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ultrasoft.data.model.complain.CreateComplainResponse
import com.example.ultrasoft.data.model.complain.SingleComplainResponse
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.data.repository.ComplainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ChatComplainViewModel @Inject constructor(private val repository: ComplainRepository) :
    ViewModel() {

    private val _replyResponse = MutableLiveData<Resource<CreateComplainResponse>>()
    val replyResponse: LiveData<Resource<CreateComplainResponse>>
        get() = _replyResponse

    private val _singleComplainResponse = MutableLiveData<Resource<SingleComplainResponse>>()
    val singleComplainResponse: LiveData<Resource<SingleComplainResponse>>
        get() = _singleComplainResponse

    private val _closeComplainResponse = MutableLiveData<Resource<SingleComplainResponse>>()
    val closeComplainResponse: LiveData<Resource<SingleComplainResponse>>
        get() = _closeComplainResponse

    fun callApiReplyComplaint(
        url: String,
        token: String, params: Map<String, RequestBody>,
        file: MultipartBody.Part?,
    ) {
        _replyResponse.value = Resource.loading()
        viewModelScope.launch {
            _replyResponse.value = repository.callApiReplyComplaint(url,token, params, file)
        }
    }


    fun callApiGetComplaintById(
        url: String,
        token: String,
    ) {
        _singleComplainResponse.value = Resource.loading()
        viewModelScope.launch {
            _singleComplainResponse.value = repository.callApiGetComplaintById(url,token)
        }
    }
    fun callApiEngResolveComplaint(
        token: String,
        complainId: String,
    ) {
        _closeComplainResponse.value = Resource.loading()
        viewModelScope.launch {
            _closeComplainResponse.value = repository.callApiEngResolveComplaint(token,complainId)
        }
    }


}