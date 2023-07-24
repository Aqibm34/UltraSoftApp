package com.example.ultrasoft.ui.fragment.complain.all

import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ultrasoft.R
import com.example.ultrasoft.base.BaseFragment
import com.example.ultrasoft.data.model.complain.ComplainCountData
import com.example.ultrasoft.data.model.complain.ComplainData
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.databinding.FragmentAllComplainBinding
import com.example.ultrasoft.databinding.LayoutAssignComplainBinding
import com.example.ultrasoft.ui.adapters.ComplaintsListAdapter
import com.example.ultrasoft.utility.AppConstants
import com.example.ultrasoft.utility.AppConstants.Companion.ATTACHMENT_URL
import com.example.ultrasoft.utility.SnackTypes
import com.example.ultrasoft.utility.logE
import com.example.ultrasoft.utility.showAlertWithButtonConfig
import com.example.ultrasoft.utility.showSnackBar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllComplainFragment :
    BaseFragment<FragmentAllComplainBinding>(FragmentAllComplainBinding::inflate) {

    private val viewModel: AllComplainViewModel by viewModels()
    private var bottomSheet: BottomSheetDialog? = null
    private var engArray: Array<String>? = null
    private var selectedComplainId = ""
    private var url = ""
    private val args: AllComplainFragmentArgs by navArgs()
    private val resultCountData = ComplainCountData(0, 0, 0, 0)
    override fun setUpViews() {
        binding.tb.setUpToolbar("All Complaints")
        url = when (appPreferences.getRole()) {
            AppConstants.UserTypes.ADMIN.name -> AppConstants.ADMIN_ALL_COMPLAIN_URL
            AppConstants.UserTypes.CUSTOMER.name -> AppConstants.CUST_ALL_COMPLAIN_URL
            AppConstants.UserTypes.ENGINEER.name -> AppConstants.ENG_ALL_COMPLAIN_URL
            else -> ""
        }

        var defStatus = AppConstants.ComplaintStatus.IN_PROGRESS.name
        if (appPreferences.getRole() != AppConstants.UserTypes.ENGINEER.name) {
            defStatus = AppConstants.ComplaintStatus.UN_ASSIGNED.name
            binding.tbl.addTab(binding.tbl.newTab().setText(resources.getString(R.string.un_assn)))
            binding.tvUnAssignCount.visibility = View.VISIBLE
        }
        binding.tbl.addTab(binding.tbl.newTab().setText(resources.getString(R.string.inprocess)))
        binding.tbl.addTab(binding.tbl.newTab().setText(resources.getString(R.string.resolved)))
        binding.tbl.addTab(binding.tbl.newTab().setText(resources.getString(R.string.closed)))
        binding.tbl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val status = if (appPreferences.getRole() != AppConstants.UserTypes.ENGINEER.name) {
                    when (tab?.position) {
                        0 -> AppConstants.ComplaintStatus.UN_ASSIGNED.name
                        1 -> AppConstants.ComplaintStatus.IN_PROGRESS.name
                        2 -> AppConstants.ComplaintStatus.RESOLVED.name
                        3 -> AppConstants.ComplaintStatus.CLOSED.name
                        else -> ""
                    }
                } else {
                    when (tab?.position) {
                        0 -> AppConstants.ComplaintStatus.IN_PROGRESS.name
                        1 -> AppConstants.ComplaintStatus.RESOLVED.name
                        2 -> AppConstants.ComplaintStatus.CLOSED.name
                        else -> ""
                    }
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

        binding.tbl.getTabAt(args.status)?.select()
        binding.rvComplaints.layoutManager = LinearLayoutManager(requireContext())

        if (args.status == 0) {
            viewModel.callApiGetAllComplaint(
                url,
                appPreferences.getToken(),
                defStatus
            )
        }
    }

    private fun showData(data: List<ComplainData>? = null, message: String? = null) {
        if (appPreferences.getRole() != AppConstants.UserTypes.ENGINEER.name) {
            when (binding.tbl.selectedTabPosition) {
                0 -> resultCountData.UN_ASSIGNED = data?.size ?: 0
                1 -> resultCountData.IN_PROGRESS = data?.size ?: 0
                2 -> resultCountData.RESOLVED = data?.size ?: 0
                3 -> resultCountData.CLOSED = data?.size ?: 0
            }
        } else {
            when (binding.tbl.selectedTabPosition) {
                0 -> resultCountData.IN_PROGRESS = data?.size ?: 0
                1 -> resultCountData.RESOLVED = data?.size ?: 0
                2 -> resultCountData.CLOSED = data?.size ?: 0
            }
        }
        setCount()
        if (data.isNullOrEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE
            binding.tvNoData.text = message
            binding.rvComplaints.adapter = ComplaintsListAdapter(
                ArrayList(),
                requireContext(),
                appPreferences.getRole()
            ) { _, _ -> }
            return
        }

        binding.tvNoData.visibility = View.GONE
        binding.rvComplaints.adapter =
            ComplaintsListAdapter(data, requireContext(), appPreferences.getRole()) { item, type ->
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

                    ComplaintsListAdapter.ClickType.ASSIGN -> {
                        selectedComplainId = item.complainId
                        if (this.engArray.isNullOrEmpty()) {
                            viewModel.callApiGetAllEngineer(appPreferences.getToken())
                        }
                    }

                    ComplaintsListAdapter.ClickType.CLOSE -> {
                        setCloseOrResolve(item.complainId)
                    }
                }
            }
    }

    private fun setCount() {
        binding.tvUnAssignCount.text = String.format(
            "( %d )",
            if (resultCountData.UN_ASSIGNED == 0) args.countData.UN_ASSIGNED else resultCountData.UN_ASSIGNED
        )
        binding.tvInProcessCount.text = String.format(
            "( %d )",
            if (resultCountData.IN_PROGRESS == 0) args.countData.IN_PROGRESS else resultCountData.IN_PROGRESS
        )
        binding.tvResolveCount.text = String.format(
            "( %d )",
            if (resultCountData.RESOLVED == 0) args.countData.RESOLVED else resultCountData.RESOLVED
        )
        binding.tvCloseCount.text = String.format(
            "( %d )",
            if (resultCountData.CLOSED == 0) args.countData.CLOSED else resultCountData.CLOSED
        )
    }

    private fun assignComplain(complainId: String, array: Array<String>?) {
        try {
            bottomSheet = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
            val dBinding = LayoutAssignComplainBinding.inflate(layoutInflater)
            bottomSheet?.setContentView(dBinding.root)
            bottomSheet?.setCancelable(true)

            dBinding.tilComplainId.editText?.setText(complainId)
            array?.let {
                (dBinding.tilEngineer.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(it)
            }
            dBinding.tilEngineer.editText?.doAfterTextChanged {
                dBinding.btnAssign.isEnabled = !dBinding.tilEngineer.editText?.text.isNullOrEmpty()
            }

            dBinding.btnAssign.setOnClickListener {
                viewModel.callAdminApiAssignComplain(
                    appPreferences.getToken(),
                    complainId,
                    dBinding.tilEngineer.editText?.text.toString().split("~")[1].trim()
                )
            }
            bottomSheet?.show()

        } catch (_: Exception) {
        }
    }

    private fun setCloseOrResolve(complainId: String) {
        when (appPreferences.getRole()) {
            AppConstants.UserTypes.ENGINEER.name -> showAlertWithButtonConfig(
                requireContext(),
                "Resolve Complain ?",
                "Do you want to close this complain.",
                AppConstants.AlertType.INFO,
                "No",
                "Yes",
            ) {
                viewModel.callApiEngResolveComplaint(
                    appPreferences.getToken(), complainId
                )
            }

            AppConstants.UserTypes.ADMIN.name -> showAlertWithButtonConfig(
                requireContext(),
                "Close Complain ?",
                "Do you want to close this complain.",
                AppConstants.AlertType.INFO,
                "No",
                "Yes",
            ) {
                if (it == AppConstants.AlertResponseType.YES) {
                    viewModel.callApiAdminCloseComplaint(
                        appPreferences.getToken(), complainId
                    )
                }
            }

            AppConstants.UserTypes.CUSTOMER.name -> showAlertWithButtonConfig(
                requireContext(),
                "Close Complain ?",
                "Do you want to close this complain.",
                AppConstants.AlertType.INFO,
                "No",
                "Yes",
            ) {
                if (it == AppConstants.AlertResponseType.YES) {
                    viewModel.callApiCustomerCloseComplain(
                        appPreferences.getToken(), complainId
                    )
                }
            }
        }

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

        viewModel.allEngineerResponse.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Resource.Status.LOADING -> {
                    showLoading()
                }

                Resource.Status.SUCCESS -> {
                    hideLoading()
                    if (response.data?.status_code == 1) {
                        engArray =
                            response.data.data.map { "${it.engineerName} ~ ${it.engineerId}" }
                                .toTypedArray()
                        assignComplain(selectedComplainId, engArray)
                    } else {
                        binding.root.showSnackBar(response.data?.message, SnackTypes.Error)
                    }
                }

                Resource.Status.ERROR -> {
                    hideLoading()
                    binding.root.showSnackBar(response.message, SnackTypes.Error)
                }
            }
        }
        viewModel.assignedResponse.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Resource.Status.LOADING -> {
                    showLoading()
                }

                Resource.Status.SUCCESS -> {
                    hideLoading()
                    if (response.data?.status_code == 1) {
                        binding.root.showSnackBar(response.data.message, SnackTypes.Success)
                        bottomSheet?.dismiss()
                        viewModel.callApiGetAllComplaint(
                            url,
                            appPreferences.getToken(),
                            AppConstants.ComplaintStatus.PENDING.name
                        )
                    } else {
                        binding.root.showSnackBar(response.data?.message, SnackTypes.Error)
                    }
                }

                Resource.Status.ERROR -> {
                    hideLoading()
                    binding.root.showSnackBar(response.message, SnackTypes.Error)
                }
            }
        }

        viewModel.resolveComplainResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    showLoading()
                }

                Resource.Status.SUCCESS -> {
                    hideLoading()
                    if (it.data?.status_code == 1) {
                        binding.root.showSnackBar(it.data.message, SnackTypes.Success)
                        findNavController().popBackStack()
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

        viewModel.closeComplainResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    showLoading()
                }

                Resource.Status.SUCCESS -> {
                    hideLoading()
                    if (it.data?.status_code == 1) {
                        binding.root.showSnackBar(it.data.message, SnackTypes.Success)
                        findNavController().popBackStack()
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

}