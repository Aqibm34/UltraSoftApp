package com.example.ultrasoft.ui.fragment.dash

import androidx.lifecycle.ViewModel
import com.example.ultrasoft.data.repository.DashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashViewModel @Inject constructor(private val repository: DashRepository) : ViewModel() {
}