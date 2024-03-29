package com.example.ultrasoft.data.network

import com.example.ultrasoft.data.model.CommonMessageResponse
import com.example.ultrasoft.data.model.CommonResponse
import com.example.ultrasoft.data.model.asset.AllAssetCategoryResponse
import com.example.ultrasoft.data.model.complain.AllComplaintsResponse
import com.example.ultrasoft.data.model.complain.ComplaintsCountResposne
import com.example.ultrasoft.data.model.complain.CreateComplainResponse
import com.example.ultrasoft.data.model.complain.SingleComplainResponse
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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface ApiInterface {

    @POST("user/login")
    suspend fun callApiLogin(@Body body: LoginRequest,@Query("notificationId") fcmToken:String): Response<LoginResponse>

    @GET("user/logout")
    suspend fun callApiLogout(@Header("X-AUTH-TOKEN") token: String): Response<LogoutResponse>

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

    @Multipart
    @POST("/customer/create/complaint")
    suspend fun callApiCreateComplaint(
        @Header("X-AUTH-TOKEN") token: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part file: MultipartBody.Part?,
    ): Response<CreateComplainResponse>

    @Multipart
    @POST
    suspend fun callApiReplyComplaint(
        @Url url: String,
        @Header("X-AUTH-TOKEN") token: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part file: MultipartBody.Part?,
    ): Response<CreateComplainResponse>

    @GET
    suspend fun callApiGetAllComplaint(
        @Url url: String,
        @Header("X-AUTH-TOKEN") token: String,
        @Query("status") status: String,
    ): Response<AllComplaintsResponse>

    @GET
    suspend fun callApiGetComplaintById(
        @Url url: String,
        @Header("X-AUTH-TOKEN") token: String,
    ): Response<SingleComplainResponse>

    @POST("engineer/resolve/complaint")
    suspend fun callApiEngResolveComplaint(
        @Header("X-AUTH-TOKEN") token: String,
        @Query("complainId") complaintId: String
    ): Response<SingleComplainResponse>

    @GET("/admin/close/admin/complaint")
    suspend fun callApiAdminCloseComplaint(
        @Header("X-AUTH-TOKEN") token: String,
        @Query("complainId") complaintId: String
    ): Response<SingleComplainResponse>

    @GET("admin/getall/category")
    suspend fun callApiGetAllAssetsCategory(): Response<AllAssetCategoryResponse>

    @POST("customer/close/complaint")
    suspend fun callApiCustomerCloseComplain(
        @Header("X-AUTH-TOKEN") token: String,
        @Query("complaintId") complaintId: String
    ): Response<SingleComplainResponse>

    //ADMIN
    @GET("admin/assigntoeng/admin/complaint")
    suspend fun callAdminApiAssignComplain(
        @Header("X-AUTH-TOKEN") token: String,
        @Query("complainId") complainId: String,
        @Query("engineerId") engineerId: String,
    ): Response<CommonResponse>

    @GET
    suspend fun callApiComplaintsCount(
        @Url url: String,
        @Header("X-AUTH-TOKEN") token: String,
    ): Response<ComplaintsCountResposne>

    @POST
    suspend fun callApiChangePassword(
        @Url url: String,
        @Header("X-AUTH-TOKEN") token: String,
        @Query("Current Password") currentPassword: String,
        @Query("New Password") newPassword: String,
    ): Response<CommonMessageResponse>

    @POST
    suspend fun callApiResetPassword(
        @Url url: String,
        @Query("mobileNumber") currentPassword: String,
        @Query("password") password: String,
        @Query("role") role: String,

    ): Response<CommonMessageResponse>


}