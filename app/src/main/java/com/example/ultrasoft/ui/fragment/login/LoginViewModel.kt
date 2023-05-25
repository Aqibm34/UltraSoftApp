package com.example.ultrasoft.ui.fragment.login

import androidx.lifecycle.ViewModel
import com.example.ultrasoft.base.BaseRepository
import com.example.ultrasoft.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {

    fun login(userName: String, password: String, role: String) {

    }
}