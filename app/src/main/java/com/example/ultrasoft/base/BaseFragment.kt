package com.example.ultrasoft.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.ultrasoft.R
import com.example.ultrasoft.databinding.LayoutDialogSearchBinding
import com.example.ultrasoft.databinding.ToolbarLayoutBinding
import com.example.ultrasoft.utility.AppPreferences
import com.example.ultrasoft.utility.redirectToPermissionSettings
import com.example.ultrasoft.utility.toast


abstract class BaseFragment<VB : ViewBinding>(
    private val
    bindingInflater: (inflater: LayoutInflater) -> VB
) : Fragment() {

    private var dialogLoading: Dialog? = null
//    var fusedLocationProviderClient: FusedLocationProviderClient? = null

    val appPreferences: AppPreferences
        get() = AppPreferences.getInstance(requireContext())

    private var _binding: VB? = null
    val binding: VB
        get() = _binding as VB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater)
        if (_binding == null) {
            throw IllegalArgumentException("Binding cannot be null.")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        observeView()
    }



    open fun setUpViews() {}
    open fun observeView() {}

    fun setUpToolBar(view:View){}

    val permissionsResultCallback = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        when (it) {
            true -> {
                requireContext().toast("Permissions granted.")
            }
            false -> {
                requireContext().toast("Permission denied! Allow permission from settings.")
                redirectToPermissionSettings(requireActivity())
            }
        }
    }

    fun showLoading() {
        if (dialogLoading == null) {
            dialogLoading = Dialog(requireContext())
            dialogLoading!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialogLoading!!.setContentView(R.layout.loading_layout)
            dialogLoading!!.setCancelable(false)
        }
        dialogLoading!!.show()
    }

    fun hideLoading() {
        try {
            if (dialogLoading != null && dialogLoading!!.isShowing) {
                dialogLoading!!.dismiss()
            }
        } catch (e: Exception) {
            // Handle or log or ignore
        } finally {
            dialogLoading = null
        }
    }

    fun showError(message: String) {
        requireContext().toast(message)
    }

    fun showDialogWithSearch(list: List<Any>, listener: (Any) -> Unit) {
        val dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
        val dialogBinding: LayoutDialogSearchBinding =
            LayoutDialogSearchBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(dialogBinding.root)
        dialog.show()
        dialogBinding.ivClose.setOnClickListener {
            dialog.dismiss()
        }

        var dataList = list
//        if (list[0] is EmpData) {
//            dataList = list.filter { (it as EmpData).blockStatus == "NO" }
//        }
//        dialogBinding.rv.layoutManager = LinearLayoutManager(requireContext())
//        val adapter = SearchRvAdapter(dataList, requireContext()) {
//            listener(it)
//            dialog.dismiss()
//        }
//        dialogBinding.rv.adapter = adapter

//        dialogBinding.etSearch.doOnTextChanged { text, _, _, _ ->
//            val filteredList = mutableListOf<Any>()
//            list.forEach {
//                if (it is RetResultSet) {
//                    if (it.RETAILERNAME.lowercase(Locale.getDefault()).contains(text.toString())
//                        || it.RETAILERUNIQUENAME.lowercase(Locale.getDefault())
//                            .contains(text.toString())
//                    ) {
//                        filteredList.add(it)
//                    }
//                } else if (it is DisResultSet) {
//                    if (it.DISTRIBUTORNAME.lowercase(Locale.getDefault()).contains(text.toString())
//                        || it.DISTRIBUTORCODE.lowercase(Locale.getDefault())
//                            .contains(text.toString())
//                    ) {
//                        filteredList.add(it)
//                    }
//                } else if (it is EmpData) {
//                    if (it.employeeName.lowercase(Locale.getDefault()).contains(text.toString())
//                        || it.employeeCode.lowercase(Locale.getDefault()).contains(text.toString())
//                    ) {
//                        filteredList.add(it)
//                    }
//                }
//            }
//            adapter.updateList(filteredList)
//        }
    }

//    fun setUpLocationListener(locationRequest: LocationRequest) {
//        fusedLocationProviderClient =
//            LocationServices.getFusedLocationProviderClient(requireActivity())
//        fusedLocationProviderClient?.requestLocationUpdates(
//            locationRequest,
//            locationObject,
//            Looper.myLooper()
//        )
//    }

//    private val locationObject = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            super.onLocationResult(locationResult)
//            for (location in locationResult.locations) {
//                appPreferences.setLatitude(location.latitude.toString())
//                appPreferences.setLongitude(location.longitude.toString())
//                logE(
//                    "long_lat",
//                    appPreferences.getLongitude() + " : " + appPreferences.getLatitude()
//                )
//            }
//            // Few more things we can do here:
//            // For example: Update the location of user on server
//        }
//    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
        hideLoading()
    }

    fun ToolbarLayoutBinding.setUpToolbar(title:String=""){
        this.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        this.tvTitle.text = title
    }

    private fun stopLocationUpdates() {
//        fusedLocationProviderClient?.removeLocationUpdates(locationObject)
//        fusedLocationProviderClient = null
    }
}