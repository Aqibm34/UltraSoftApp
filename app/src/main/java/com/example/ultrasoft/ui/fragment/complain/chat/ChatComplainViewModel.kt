package com.example.ultrasoft.ui.fragment.complain.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ultrasoft.data.model.complain.CreateComplainResponse
import com.example.ultrasoft.data.model.user.BlockResponse
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

    fun callApiReplyComplaint(
        token: String, params: Map<String, RequestBody>,
        file: MultipartBody.Part?,
    ) {
        _replyResponse.value = Resource.loading()
        viewModelScope.launch {
            _replyResponse.value = repository.callApiReplyComplaint(token, params, file)
        }
    }



}