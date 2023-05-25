package com.example.ultrasoft.data.repository

import com.example.ultrasoft.base.BaseRepository
import com.example.ultrasoft.data.network.ApiInterface
import javax.inject.Inject

class LoginRepository @Inject constructor(private val apiInterface: ApiInterface):BaseRepository() {
}