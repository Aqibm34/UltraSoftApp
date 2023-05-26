package com.example.ultrasoft.data.network

import com.example.ultrasoft.data.model.login.LoginRequest
import com.example.ultrasoft.data.model.login.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface ApiInterface {

    @POST("user/login")
    suspend fun callApiLogin(@Body body: LoginRequest): Response<LoginResponse>

    @POST("user/logout")
    suspend fun callApiLogout(@Header("X-AUTH-TOKE") token: String): Response<LoginResponse>

}