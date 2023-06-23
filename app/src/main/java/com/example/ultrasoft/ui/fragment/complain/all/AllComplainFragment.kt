package com.example.ultrasoft.ui.fragment.complain.all

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ultrasoft.base.BaseFragment
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.databinding.FragmentAllComplainBinding
import com.example.ultrasoft.ui.adapters.ComplaintsListAdapter
import com.example.ultrasoft.utility.AppConstants
import com.example.ultrasoft.utility.AppConstants.Companion.ATTACHMENT_URL
import com.example.ultrasoft.utility.showAlert
import com.example.ultrasoft.utility.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllComplainFragment :
    BaseFragment<FragmentAllComplainBinding>(FragmentAllComplainBinding::inflate) {

    private val viewModel: AllComplainViewModel by viewModels()

    override fun setUpViews() {
        binding.tb.setUpToolbar("All Complaints")
        val url = if (appPreferences.getRole() == AppConstants.UserTypes.ENGINEER.name) {
            AppConstants.ENG_ALL_COMPLAIN_URL
        } else {
            AppConstants.ALL_COMPLAIN_URL
        }
        viewModel.callApiGetAllComplaint(url, appPreferences.getToken(),AppConstants.ComplaintStatus.OPEN.name)
        binding.rvComplaints.layoutManager = LinearLayoutManager(requireContext())
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
                        binding.rvComplaints.adapter =
                            ComplaintsListAdapter(it.data.data, requireContext()) { item, type ->
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
                                                ATTACHMENT_URL + item.chats[0].attachment
                                            )
                                        )
                                    }
                                }
                            }
                    } else {
                        showAlert(it.data?.message, AppConstants.AlertType.ERROR) {}
                    }
                }
                Resource.Status.ERROR -> {
                    hideLoading()
                    showAlert(it.data?.message, AppConstants.AlertType.ERROR) {}
                }
            }
        }
    }

}