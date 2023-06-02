package com.example.ultrasoft.ui.fragment.user.all

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ultrasoft.R
import com.example.ultrasoft.base.BaseFragment
import com.example.ultrasoft.data.model.user.admin.AdminData
import com.example.ultrasoft.data.model.user.admin.AllAdminResponse
import com.example.ultrasoft.data.model.user.customer.AllCustomerResponse
import com.example.ultrasoft.data.model.user.customer.CustomerData
import com.example.ultrasoft.data.model.user.engineer.AllEngineerResponse
import com.example.ultrasoft.data.model.user.engineer.EngineerData
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.databinding.FragmentAllUsersBinding
import com.example.ultrasoft.ui.adapters.UsersRvAdapter
import com.example.ultrasoft.utility.*
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllUsersFragment : BaseFragment<FragmentAllUsersBinding>(FragmentAllUsersBinding::inflate) {

    private val viewModel: AllUsersViewModel by viewModels()
    private var adminList: List<AdminData> = ArrayList()
    private var customerList: List<CustomerData> = ArrayList()
    private var engineerList: List<EngineerData> = ArrayList()
    private var selectedUser = ""

    override fun setUpViews() {
        binding.tb.setUpToolbar("All Users")
        binding.tbl.addTab(binding.tbl.newTab().setText("Admin"))
        binding.tbl.addTab(binding.tbl.newTab().setText("Customer"))
        binding.tbl.addTab(binding.tbl.newTab().setText("Engineer"))

        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        if (adminList.isEmpty()) {
            viewModel.callApiGetAllAdmin(appPreferences.getToken())
        }

        binding.tbl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.rv.adapter = UsersRvAdapter(adminList, requireContext()) {
                            setUpListener(it)
                        }
                    }
                    1 -> {
                        if (customerList.isEmpty()) {
                            viewModel.callApiGetAllCustomer(appPreferences.getToken())
                        } else {
                            binding.rv.adapter = UsersRvAdapter(customerList, requireContext()) {
                                setUpListener(it)
                            }
                        }
                    }
                    2 -> {
                        if (engineerList.isEmpty()) {
                            viewModel.callApiGetAllEngineer(appPreferences.getToken())
                        } else {
                            binding.rv.adapter = UsersRvAdapter(engineerList, requireContext()) {
                                setUpListener(it)
                            }
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })

    }

    override fun observeView() {
        viewModel.allAdminResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> showLoading()
                Resource.Status.SUCCESS -> {
                    hideLoading()
                    if (it.data?.status_code == 1) {
                        adminList = it.data.data
                        binding.rv.adapter = UsersRvAdapter(adminList, requireContext()) { it1 ->
                            setUpListener(it1)
                        }
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

        viewModel.allCustomerResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> showLoading()
                Resource.Status.SUCCESS -> {
                    hideLoading()
                    if (it.data?.status_code == 1) {
                        customerList = it.data.data
                        binding.rv.adapter = UsersRvAdapter(customerList, requireContext()) { it1 ->
                            setUpListener(it1)
                        }
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

        viewModel.allEngineerResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> showLoading()
                Resource.Status.SUCCESS -> {
                    hideLoading()
                    if (it.data?.status_code == 1) {
                        engineerList = it.data.data
                        binding.rv.adapter = UsersRvAdapter(engineerList, requireContext()) { it1 ->
                            setUpListener(it1)
                        }
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

        viewModel.blockResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> showLoading()
                Resource.Status.SUCCESS -> {
                    hideLoading()
                    if (it.data?.status_code == 1) {
                        binding.root.showSnackBar(it.data.message, SnackTypes.Success)
                        when (selectedUser) {
                            AppConstants.UserTypes.ADMIN.name -> viewModel.callApiGetAllAdmin(
                                appPreferences.getToken()
                            )
                            AppConstants.UserTypes.CUSTOMER.name -> viewModel.callApiGetAllCustomer(
                                appPreferences.getToken()
                            )
                            AppConstants.UserTypes.ENGINEER.name -> viewModel.callApiGetAllEngineer(
                                appPreferences.getToken()
                            )
                        }
                    } else {
                        binding.root.showSnackBar(it.data?.message, SnackTypes.Error)
                    }
                }
                Resource.Status.ERROR -> {
                    hideLoading()
                    binding.root.showSnackBar(it.data?.message, SnackTypes.Error)
                }
            }
        }
    }

    private fun setUpListener(data: Any) {
        when (data) {
            is AdminData -> {
                selectedUser = AppConstants.UserTypes.ADMIN.name
                viewModel.callApiBlockUser(
                    appPreferences.getToken(),
                    data.adminId,
                    selectedUser,
                    if (data.activeStatus == AppConstants.STATUS.ACTIVE.name) AppConstants.STATUS.INACTIVE.name else AppConstants.STATUS.ACTIVE.name
                )
            }
            is CustomerData -> {
                selectedUser = AppConstants.UserTypes.CUSTOMER.name
                viewModel.callApiBlockUser(
                    appPreferences.getToken(),
                    data.customerId,
                    selectedUser,
                    if (data.activeStatus == AppConstants.STATUS.ACTIVE.name) AppConstants.STATUS.INACTIVE.name else AppConstants.STATUS.ACTIVE.name
                )
            }
            is EngineerData -> {
                selectedUser = AppConstants.UserTypes.ENGINEER.name
                viewModel.callApiBlockUser(
                    appPreferences.getToken(),
                    data.engineerId,
                    selectedUser,
                    if (data.activeStatus == AppConstants.STATUS.ACTIVE.name) AppConstants.STATUS.INACTIVE.name else AppConstants.STATUS.ACTIVE.name
                )
            }
        }
    }
}