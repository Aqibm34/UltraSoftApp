package com.example.ultrasoft.data.network

import com.example.ultrasoft.data.model.login.LoginRequest
import com.example.ultrasoft.data.model.login.LoginResponse
import com.example.ultrasoft.data.model.login.LogoutResponse
import com.example.ultrasoft.data.model.user.BlockResponse
import com.example.ultrasoft.data.model.user.admin.AllAdminResponse
import com.example.ultrasoft.data.model.user.admin.CreateAdminRequest
import com.example.ultrasoft.data.model.user.admin.CreateAdminResponse
import com.example.ultrasoft.data.model.user.customer.AllCustomerResponse
import com.example.ultrasoft.data.model.user.customer.CreateCustomerRequest
import com.example.ultrasoft.data.model.user.customer.CreateCustomerResponse
import com.example.ultrasoft.data.model.user.engineer.AllEngineerResponse
import com.example.ultrasoft.data.model.user.engineer.CreateEngineerRequest
import com.example.ultrasoft.data.model.user.engineer.CreateEngineerResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface ApiInterface {

    @POST("user/login")
    suspend fun callApiLogin(@Body body: LoginRequest): Response<LoginResponse>

    @POST("user/logout")
    suspend fun callApiLogout(@Header("X-AUTH-TOKE") token: String): Response<LogoutResponse>

    @POST("admin/create/admin")
    suspend fun callApiCreateAdmin(
        @Header("X-AUTH-TOKEN") token: String, @Body body: CreateAdminRequest
    ): Response<CreateAdminResponse>

    @GET("admin/getall/admin")
    suspend fun callApiGetAllAdmin(
        @Header("X-AUTH-TOKEN") token: String,
    ): Response<AllAdminResponse>


    @POST("admin/create/customer")
    suspend fun callApiCreateCustomer(
        @Header("X-AUTH-TOKEN") token: String, @Body body: CreateCustomerRequest
    ): Response<CreateCustomerResponse>

    @GET("admin/getall/customer")
    suspend fun callApiGetAllCustomer(
        @Header("X-AUTH-TOKEN") token: String,
    ): Response<AllCustomerResponse>


    @POST("admin/create/engineer")
    suspend fun callApiCreateEngineer(
        @Header("X-AUTH-TOKEN") token: String, @Body body: CreateEngineerRequest
    ): Response<CreateEngineerResponse>

    @GET("admin/getall/engineer")
    suspend fun callApiGetAllEngineer(
        @Header("X-AUTH-TOKEN") token: String,
    ): Response<AllEngineerResponse>

    @GET("/admin/block/unblock/{id}")
    suspend fun callApiBlockUser(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("id") id: String,
        @Query("role") role: String,
        @Query("action") action: String,
    ): Response<BlockResponse>



}