package com.example.ultrasoft.ui.fragment.dash

import android.view.View
import androidx.core.content.ContextCompat
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
            findNavController().navigate(R.id.action_dashBoardFragment_to_createComplainFragment)
        }
        binding.tvViewComplain.setOnClickListener {
            findNavController().navigate(
                DashBoardFragmentDirections.actionDashBoardFragmentToAllComplainFragment(
                    countData,
                    0
                )
            )
        }

        var minusForStatusEng = 1
        if (appPreferences.getRole() != AppConstants.UserTypes.ENGINEER.name) {
            binding.cvUnAssign.root.visibility = View.VISIBLE
            minusForStatusEng = 0
        }

        binding.cvUnAssign.tvTitle.text = resources.getString(R.string.un_assinged)
        binding.cvInProcess.tvTitle.text = resources.getString(R.string.inprocess)
        binding.cvResolved.tvTitle.text = resources.getString(R.string.resolved)
        binding.cvClosed.tvTitle.text = resources.getString(R.string.closed)
        binding.cvResolved.tvValue.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.green
            )
        )
        binding.cvClosed.tvValue.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.green
            )
        )

        binding.cvUnAssign.root.setOnClickListener {
            findNavController().navigate(
                DashBoardFragmentDirections.actionDashBoardFragmentToAllComplainFragment(
                    countData, 0 - minusForStatusEng
                )
            )
        }
        binding.cvInProcess.root.setOnClickListener {
            findNavController().navigate(
                DashBoardFragmentDirections.actionDashBoardFragmentToAllComplainFragment(
                    countData, 1 - minusForStatusEng
                )
            )
        }
        binding.cvResolved.root.setOnClickListener {
            findNavController().navigate(
                DashBoardFragmentDirections.actionDashBoardFragmentToAllComplainFragment(
                    countData, 2 - minusForStatusEng
                )
            )
        }
        binding.cvClosed.root.setOnClickListener {
            findNavController().navigate(
                DashBoardFragmentDirections.actionDashBoardFragmentToAllComplainFragment(
                    countData, 3 - minusForStatusEng
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
                        binding.cvUnAssign.tvValue.text =
                            String.format("%d", it.data.data.UN_ASSIGNED)
                        binding.cvInProcess.tvValue.text =
                            String.format("%d", it.data.data.IN_PROGRESS)
                        binding.cvResolved.tvValue.text =
                            String.format("%d", it.data.data.RESOLVED)
                        binding.cvClosed.tvValue.text =
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