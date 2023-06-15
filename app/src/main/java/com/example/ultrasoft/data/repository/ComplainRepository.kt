package com.example.ultrasoft.data.repository

import com.example.ultrasoft.base.BaseRepository
import com.example.ultrasoft.data.network.ApiInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.PartMap
import javax.inject.Inject

class ComplainRepository @Inject constructor(private val apiInterface: ApiInterface) :
    BaseRepository() {

    suspend fun callApiCreateComplaint(
        token: String,
        params: Map<String, RequestBody>,
        file: MultipartBody.Part,
    ) = safeApiCall {
        apiInterface.callApiCreateComplaint(token,params,file)
    }

    suspend fun callApiReplyComplaint(
        token: String,
        params: Map<String, RequestBody>,
        file: MultipartBody.Part?,
    ) = safeApiCall {
        apiInterface.callApiReplyComplaint(token,params,file)
    }


    suspend fun callApiGetAllComplaint(
        token: String,
    ) = safeApiCall {
        apiInterface.callApiGetAllComplaint(token)
    }
}