package com.example.ultrasoft.ui.fragment.complain.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ultrasoft.data.model.user.BlockResponse
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.data.repository.ComplainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class CreateComplainViewModel @Inject constructor(private val repository: ComplainRepository) :
    ViewModel() {

    private val _createComplainResponse = MutableLiveData<Resource<BlockResponse>>()
    val createComplainResponse: LiveData<Resource<BlockResponse>>
        get() = _createComplainResponse

    fun createComplain(
        token: String, params: Map<String, RequestBody>,
        file: MultipartBody.Part,
    ) {
        _createComplainResponse.value = Resource.loading()
        viewModelScope.launch {
            _createComplainResponse.value = repository.createComplain(token, params, file)
        }
    }
}