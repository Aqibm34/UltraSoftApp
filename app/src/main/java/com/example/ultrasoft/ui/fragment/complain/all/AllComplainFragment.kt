package com.example.ultrasoft.ui.fragment.complain.all

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ultrasoft.R
import com.example.ultrasoft.base.BaseFragment
import com.example.ultrasoft.data.model.complain.ComplainData
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.databinding.FragmentAllComplainBinding
import com.example.ultrasoft.ui.adapters.ComplaintsListAdapter
import com.example.ultrasoft.utility.AppConstants
import com.example.ultrasoft.utility.AppConstants.Companion.ATTACHMENT_URL
import com.example.ultrasoft.utility.showAlert
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllComplainFragment :
    BaseFragment<FragmentAllComplainBinding>(FragmentAllComplainBinding::inflate) {

    private val viewModel: AllComplainViewModel by viewModels()

    override fun setUpViews() {
        binding.tb.setUpToolbar("All Complaints")
        val url = when (appPreferences.getRole()) {
            AppConstants.UserTypes.ADMIN.name -> AppConstants.ADMIN_ALL_COMPLAIN_URL
            AppConstants.UserTypes.CUSTOMER.name -> AppConstants.CUST_ALL_COMPLAIN_URL
            AppConstants.UserTypes.ENGINEER.name -> AppConstants.ENG_ALL_COMPLAIN_URL
            else -> ""
        }

        binding.tbl.addTab(binding.tbl.newTab().setText(resources.getString(R.string.pending)))
        binding.tbl.addTab(binding.tbl.newTab().setText(resources.getString(R.string.resolved)))
        binding.tbl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val status = when (tab?.position) {
                    0 -> AppConstants.ComplaintStatus.PENDING.name
                    1 -> AppConstants.ComplaintStatus.RESOLVED.name
                    else -> ""
                }
                viewModel.callApiGetAllComplaint(
                    url,
                    appPreferences.getToken(),
                    status
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })

        binding.rvComplaints.layoutManager = LinearLayoutManager(requireContext())
        viewModel.callApiGetAllComplaint(
            url,
            appPreferences.getToken(),
            AppConstants.ComplaintStatus.PENDING.name
        )
    }

    override fun observeView() {
        viewModel.allComplainResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    showLoading()
                }

                Resource.Status.SUCCESS -> {
                    hideLoading()
                    if (it.data?.status_code == 1) {
                        showData(it.data.data)
                    } else {
                        showData(message = it.data?.message)
                    }
                }

                Resource.Status.ERROR -> {
                    hideLoading()
                    showData(message = it.message)
                }
            }
        }
    }

    private fun showData(data: List<ComplainData>? = null, message: String? = null) {
        if (data.isNullOrEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE
            binding.tvNoData.text = message
            binding.rvComplaints.adapter = ComplaintsListAdapter(ArrayList(),requireContext()){ _, _ ->}
            return
        }
        binding.tvNoData.visibility = View.GONE
        binding.rvComplaints.adapter =
            ComplaintsListAdapter(data, requireContext()) { item, type ->
                when (type) {
                    ComplaintsListAdapter.ClickType.CHAT -> {
                        findNavController().navigate(
                            AllComplainFragmentDirections.actionAllComplainFragmentToComplainChatFragment(
                                item
                            )
                        )
                    }

                    ComplaintsListAdapter.ClickType.IMAGE -> {
                        findNavController().navigate(
                            AllComplainFragmentDirections.actionAllComplainFragmentToPreviewFragment(
                                ATTACHMENT_URL + item.complaintChats[0].attachment
                            )
                        )
                    }
                }
            }
    }

}