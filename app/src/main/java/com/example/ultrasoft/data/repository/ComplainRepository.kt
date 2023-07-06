package com.example.ultrasoft.data.repository

import com.example.ultrasoft.base.BaseRepository
import com.example.ultrasoft.data.network.ApiInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ComplainRepository @Inject constructor(private val apiInterface: ApiInterface) :
    BaseRepository() {

    suspend fun callApiCreateComplaint(
        token: String,
        params: Map<String, RequestBody>,
        file: MultipartBody.Part,
    ) = safeApiCall {
        apiInterface.callApiCreateComplaint(token, params, file)
    }

    suspend fun callApiReplyComplaint(
        url: String,
        token: String,
        params: Map<String, RequestBody>,
        file: MultipartBody.Part?,
    ) = safeApiCall {
        apiInterface.callApiReplyComplaint(url, token, params, file)
    }


    suspend fun callApiGetAllComplaint(
        url: String,
        token: String,
        status: String
    ) = safeApiCall {
        apiInterface.callApiGetAllComplaint(url, token, status)
    }

    suspend fun callApiGetComplaintById(
        url: String,
        token: String,
    ) = safeApiCall {
        apiInterface.callApiGetComplaintById(url, token)
    }

    suspend fun callApiEngResolveComplaint(
        token: String,
        complainId: String,
    ) = safeApiCall {
        apiInterface.callApiEngResolveComplaint(token, complainId)
    }

    suspend fun callApiGetAllAssetsCategory() = safeApiCall {
        apiInterface.callApiGetAllAssetsCategory()
    }
    suspend fun callApiCustomerCloseComplain(
        token: String,
        complainId: String,
    ) = safeApiCall {
        apiInterface.callApiCustomerCloseComplain(token, complainId)
    }
}