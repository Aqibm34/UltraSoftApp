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
import com.example.ultrasoft.data.model.complain.ComplainCountData
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.databinding.FragmentDashBoardBinding
import com.example.ultrasoft.utility.AppConstants
import com.example.ultrasoft.utility.capitalizeWords
import com.example.ultrasoft.utility.showAlert
import com.example.ultrasoft.utility.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardFragment :
    BaseFragment<FragmentDashBoardBinding>(FragmentDashBoardBinding::inflate) {

    private val viewModel: DashViewModel by viewModels()
    private var countData = ComplainCountData(0, 0, 0, 0)
    override fun setUpViews() {
        binding.tvName.text = String.format(
            "%s ~ %s",
            appPreferences.getName().capitalizeWords(),
            appPreferences.getRole().capitalizeWords()
        )
        binding.tvLogOut.setOnClickListener { logOut() }
        binding.tvCreateUser.setOnClickListener { findNavController().navigate(R.id.action_dashBoardFragment_to_usersFragment) }
        binding.tvCreateAsset.setOnClickListener { findNavController().navigate(R.id.action_dashBoardFragment_to_assetOptionsFragment) }
        binding.tvComplain.setOnClickListener {
            findNavController().navigate(
                DashBoardFragmentDirections.actionDashBoardFragmentToComplainOptionsFragment(
                    countData
                )
            )
        }
        binding.tvInProcessCountValue.setOnClickListener {
            findNavController().navigate(
                DashBoardFragmentDirections.actionDashBoardFragmentToAllComplainOptionsFragment(
                    countData, 0
                )
            )
        }
        binding.tvUnAssignCountValue.setOnClickListener {
            findNavController().navigate(
                DashBoardFragmentDirections.actionDashBoardFragmentToAllComplainOptionsFragment(
                    countData, 1
                )
            )
        }
        binding.tvResolveCountValue.setOnClickListener {
            findNavController().navigate(
                DashBoardFragmentDirections.actionDashBoardFragmentToAllComplainOptionsFragment(
                    countData, 2
                )
            )
        }
        binding.tvCloseCountValue.setOnClickListener {
            findNavController().navigate(
                DashBoardFragmentDirections.actionDashBoardFragmentToAllComplainOptionsFragment(
                    countData, 3
                )
            )
        }

        val url = when (appPreferences.getRole()) {
            AppConstants.UserTypes.ADMIN.name -> AppConstants.ADMIN_COMPLAIN_COUNT_URL
            AppConstants.UserTypes.CUSTOMER.name -> AppConstants.CUSTOMER_COMPLAIN_COUNT_URL
            AppConstants.UserTypes.ENGINEER.name -> AppConstants.ENG_COMPLAIN_COUNT_URL
            else -> ""
        }
        viewModel.callApiComplaintsCount(url, appPreferences.getToken())
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
        viewModel.complainCountResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> showLoading()
                Resource.Status.SUCCESS -> {
                    hideLoading()
                    if (it.data?.status_code == 1) {
                        countData = it.data.data
                        binding.tvInProcessCountValue.text =
                            String.format("%d", it.data.data.IN_PROGRESS)
                        binding.tvUnAssignCountValue.text =
                            String.format("%d", it.data.data.UN_ASSIGNED)
                        binding.tvResolveCountValue.text =
                            String.format("%d", it.data.data.RESOLVED)
                        binding.tvCloseCountValue.text =
                            String.format("%d", it.data.data.CLOSED)
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