package com.example.ultrasoft.data.repository

import com.example.ultrasoft.base.BaseRepository
import com.example.ultrasoft.data.network.ApiInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Header
import retrofit2.http.Query
import javax.inject.Inject

class ComplainRepository @Inject constructor(private val apiInterface: ApiInterface) :
    BaseRepository() {

    suspend fun callApiCreateComplaint(
        token: String,
        params: Map<String, RequestBody>,
        file: MultipartBody.Part?,
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

    suspend fun callApiAdminCloseComplaint(
        token: String,
        complainId: String,
    ) = safeApiCall {
        apiInterface.callApiAdminCloseComplaint(token, complainId)
    }

    suspend fun callAdminApiAssignComplain(
        token: String,
        complainId: String,
        engineerId: String,
    ) = safeApiCall {
        apiInterface.callAdminApiAssignComplain(token, complainId, engineerId)
    }
    suspend fun callApiGetAllEngineer(token: String) = safeApiCall {
        apiInterface.callApiGetAllEngineer(token)
    }

}
