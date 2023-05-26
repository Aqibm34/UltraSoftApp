package com.example.ultrasoft.ui.fragment.dash

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ultrasoft.R
import com.example.ultrasoft.base.BaseFragment
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.databinding.FragmentDashBoardBinding
import com.example.ultrasoft.utility.AppConstants
import com.example.ultrasoft.utility.showAlert
import com.example.ultrasoft.utility.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardFragment :
    BaseFragment<FragmentDashBoardBinding>(FragmentDashBoardBinding::inflate) {

    private val viewModel: DashViewModel by viewModels()
    override fun setUpViews() {
        binding.tvName.text = appPreferences.getName()
        binding.tvRole.text = appPreferences.getRole()
        binding.tvLogOut.setOnClickListener { logOut()}
    }

    private fun logOut() {
        showAlert(
            resources.getString(R.string.are_you_sure_you_want_to_logout),
            AppConstants.AlertType.INFO,
            resources.getString(R.string.logout)
        ) {
            if (it == AppConstants.AlertResponseType.YES) {
                viewModel.callApiLogout(appPreferences.getToken())
            }
        }
    }


    override fun observeView() {
        viewModel.logoutResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> showLoading()
                Resource.Status.SUCCESS -> {
                    hideLoading()
                    if (it.data?.status_code == 1) {
                        appPreferences.clearPreferences()
                        requireContext().toast(it.data.message)
                        findNavController().navigate(R.id.action_dashBoardFragment_to_loginFragment)
                    } else {
                        showAlert(it.data?.message, AppConstants.AlertType.ERROR) {}
                    }
                }
                Resource.Status.ERROR -> {
                    hideLoading()
                    showAlert(it.message, AppConstants.AlertType.ERROR) {}
                }
            }
        }
    }

}