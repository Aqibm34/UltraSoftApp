package com.example.ultrasoft.data.repository

import com.example.ultrasoft.base.BaseRepository
import com.example.ultrasoft.data.model.user.admin.CreateAdminRequest
import com.example.ultrasoft.data.model.user.customer.CreateCustomerRequest
import com.example.ultrasoft.data.model.user.engineer.CreateEngineerRequest
import com.example.ultrasoft.data.network.ApiInterface
import retrofit2.http.Body
import retrofit2.http.Header
import javax.inject.Inject

class AssetsRepository @Inject constructor(private val apiInterface: ApiInterface) :
    BaseRepository() {


}