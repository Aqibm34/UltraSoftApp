package com.example.ultrasoft.data.repository

import com.example.ultrasoft.base.BaseRepository
import com.example.ultrasoft.data.model.user.admin.CreateAdminRequest
import com.example.ultrasoft.data.model.user.customer.CreateCustomerRequest
import com.example.ultrasoft.data.model.user.engineer.CreateEngineerRequest
import com.example.ultrasoft.data.network.ApiInterface
import retrofit2.http.Body
import retrofit2.http.Header
import javax.inject.Inject

class UsersRepository @Inject constructor(private val apiInterface: ApiInterface) :
    BaseRepository() {

    suspend fun callApiCreateAdmin(token: String, body: CreateAdminRequest) = safeApiCall {
        apiInterface.callApiCreateAdmin(token, body)
    }

    suspend fun callApiGetAllAdmin(token: String) = safeApiCall {
        apiInterface.callApiGetAllAdmin(token)
    }

    suspend fun callApiCreateCustomer(token: String, body: CreateCustomerRequest) = safeApiCall {
        apiInterface.callApiCreateCustomer(token, body)
    }

    suspend fun callApiGetAllCustomer(token: String) = safeApiCall {
        apiInterface.callApiGetAllCustomer(token)
    }

    suspend fun callApiCreateEngineer(token: String, body: CreateEngineerRequest) = safeApiCall {
        apiInterface.callApiCreateEngineer(token, body)
    }

    suspend fun callApiGetAllEngineer(token: String) = safeApiCall {
        apiInterface.callApiGetAllEngineer(token)
    }

    suspend fun callApiBlockUser(token: String, id: String, role: String, action: String) =
        safeApiCall {
            apiInterface.callApiBlockUser(token,id,role,action)
        }

}