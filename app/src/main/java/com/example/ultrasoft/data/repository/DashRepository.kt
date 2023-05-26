package com.example.ultrasoft.data.repository

import com.example.ultrasoft.base.BaseRepository
import com.example.ultrasoft.data.model.login.LoginRequest
import com.example.ultrasoft.data.network.ApiInterface
import javax.inject.Inject

class DashRepository @Inject constructor(private val apiInterface: ApiInterface) :
    BaseRepository() {

    suspend fun callApiLogout(token: String) =
        safeApiCall {
            apiInterface.callApiLogout(token)
        }
}