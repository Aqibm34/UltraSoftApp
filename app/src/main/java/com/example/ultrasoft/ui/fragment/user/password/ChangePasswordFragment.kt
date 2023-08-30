package com.example.ultrasoft.ui.fragment.user.password

import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ultrasoft.R
import com.example.ultrasoft.base.BaseFragment
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.databinding.FragmentChangePasswordBinding
import com.example.ultrasoft.utility.AppConstants
import com.example.ultrasoft.utility.clearError
import com.example.ultrasoft.utility.showAlert
import com.example.ultrasoft.utility.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment :
    BaseFragment<FragmentChangePasswordBinding>(FragmentChangePasswordBinding::inflate) {

    private val viewModel: ChangePasswordViewModel by viewModels()
    private var isCurrentPasswordValidated = false
    private var isNewPasswordValidated = false
    private val args: ChangePasswordFragmentArgs by navArgs()
    override fun setUpViews() {
        val title =
            if (args.isReset) resources.getString(R.string.forgot_password) else resources.getString(
                R.string.change_password
            )
        binding.tb.setUpToolbar(title)

        if (args.isReset) {
            binding.tilMobile.visibility = View.VISIBLE
            binding.tilCurrentPassword.visibility = View.GONE
            binding.tilNewPassword.visibility = View.GONE
            binding.tilMobile.editText?.doOnTextChanged { _, _, _, _ ->
                if (binding.tilMobile.editText?.text.toString().length < 10) {
                    binding.tilMobile.error = resources.getString(R.string.enter_valid_mobile)
                    binding.btnSubmit.isEnabled = false
                } else {
                    binding.tilMobile.clearError()
                    binding.btnSubmit.isEnabled = true
                }
            }
        }


        binding.tilCurrentPassword.editText?.doOnTextChanged { _, _, _, _ ->
            validateCurrentPassword()
        }
        binding.tilNewPassword.editText?.doOnTextChanged { _, _, _, _ ->
            validateNewPassword()
        }

        binding.btnSubmit.setOnClickListener {
            if (args.isReset) {
                requireContext().toast("Reset")
                viewModel.callApiResetPassword("", binding.tilMobile.editText?.text.toString())
            } else {
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

        viewModel.resetPasswordResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> showLoading()
                Resource.Status.SUCCESS -> {
                    hideLoading()
                    if (it.data?.status_code == 1) {
                        showAlert(it.data.message, AppConstants.AlertType.SUCCESS) {
                            findNavController().popBackStack()
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
    }

}