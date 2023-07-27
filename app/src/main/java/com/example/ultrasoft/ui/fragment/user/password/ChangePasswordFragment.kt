package com.example.ultrasoft.ui.fragment.user.password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ultrasoft.R
import com.example.ultrasoft.base.BaseFragment
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.databinding.FragmentChangePasswordBinding
import com.example.ultrasoft.utility.AppConstants
import com.example.ultrasoft.utility.clearError
import com.example.ultrasoft.utility.showAlert
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment :
    BaseFragment<FragmentChangePasswordBinding>(FragmentChangePasswordBinding::inflate) {

    private val viewModel: ChangePasswordViewModel by viewModels()
    private var isCurrentPasswordValidated = false
    private var isNewPasswordValidated = false

    override fun setUpViews() {
        binding.tb.setUpToolbar(resources.getString(R.string.change_password))

        binding.tilCurrentPassword.editText?.doOnTextChanged { _, _, _, _ ->
            validateCurrentPassword()
        }
        binding.tilNewPassword.editText?.doOnTextChanged { _, _, _, _ ->
            validateNewPassword()
        }

        binding.btnSubmit.setOnClickListener {
            val url = when (appPreferences.getRole()) {
                AppConstants.UserTypes.ADMIN.name -> AppConstants.ADMIN_CHANGE_PASS_URL
                AppConstants.UserTypes.CUSTOMER.name -> AppConstants.CUST_CHANGE_PASS_URL
                AppConstants.UserTypes.ENGINEER.name -> AppConstants.ENG_CHANGE_PASS_URL
                else -> ""
            }
            viewModel.callApiChangePassword(
                url,
                appPreferences.getToken(),
                binding.tilCurrentPassword.editText?.text.toString(),
                binding.tilNewPassword.editText?.text.toString()
            )
        }
    }

    private fun validateCurrentPassword() {
        if (binding.tilCurrentPassword.editText?.text.toString().isEmpty()) {
            binding.tilCurrentPassword.error = "Enter Current Password"
            isCurrentPasswordValidated = false
        } else if (binding.tilCurrentPassword.editText?.text.toString().length < 8 || binding.tilCurrentPassword.editText?.text.toString().length > 16) {
            binding.tilCurrentPassword.error =
                "Password length should be in between 8 to 16 characters"
            isCurrentPasswordValidated = false
        } else {
            binding.tilCurrentPassword.clearError()
            isCurrentPasswordValidated = true
        }
        binding.btnSubmit.isEnabled = isCurrentPasswordValidated && isNewPasswordValidated
    }

    private fun validateNewPassword() {
        if (binding.tilNewPassword.editText?.text.toString().isEmpty()) {
            binding.tilNewPassword.error = "Enter New Password"
            isNewPasswordValidated = false
        } else if (binding.tilNewPassword.editText?.text.toString() == binding.tilCurrentPassword.editText?.text.toString()) {
            binding.tilNewPassword.error = "Current and New Password should not be same"
            isNewPasswordValidated = false
        } else if (binding.tilNewPassword.editText?.text.toString().length < 8 || binding.tilNewPassword.editText?.text.toString().length > 16) {
            binding.tilNewPassword.error =
                "New Password length should be in between 8 to 16 characters"
            isNewPasswordValidated = false
        } else {
            binding.tilNewPassword.clearError()
            isNewPasswordValidated = true
        }
        binding.btnSubmit.isEnabled = isCurrentPasswordValidated && isNewPasswordValidated
    }

    override fun observeView() {
        viewModel.changePasswordResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> showLoading()
                Resource.Status.SUCCESS -> {
                    hideLoading()
                    if (it.data?.status_code == 1) {
                        showAlert(it.data.message, AppConstants.AlertType.SUCCESS) {
                            findNavController().popBackStack()
                        }
                    }else{
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