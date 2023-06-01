package com.example.ultrasoft.ui.fragment.user.create

import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ultrasoft.R
import com.example.ultrasoft.base.BaseFragment
import com.example.ultrasoft.data.model.user.admin.CreateAdminRequest
import com.example.ultrasoft.data.model.user.customer.CreateCustomerRequest
import com.example.ultrasoft.data.model.user.engineer.CreateEngineerRequest
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.databinding.FragmentCreateUserBinding
import com.example.ultrasoft.utility.AppConstants
import com.example.ultrasoft.utility.clearError
import com.example.ultrasoft.utility.isValidEmail
import com.example.ultrasoft.utility.showAlert
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateUserFragment :
    BaseFragment<FragmentCreateUserBinding>(FragmentCreateUserBinding::inflate) {

    private val viewModel: CreateUserViewModel by viewModels()

    override fun setUpViews() {
        binding.tb.setUpToolbar("Create Users")
        binding.rgUser.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbAdmin -> {
                    binding.tilDesc.visibility = View.GONE
                    enableButton()
                    validation()
                }
                R.id.rbCustomer -> {
                    binding.tilDesc.visibility = View.GONE
                    enableButton()
                    validation()
                }
                R.id.rbEngineer -> {
                    binding.tilDesc.visibility = View.VISIBLE
                    enableButton()
                    validation()
                }
            }
        }
        binding.btnSubmit.setOnClickListener {
            if (binding.rbAdmin.isChecked) {
                viewModel.callApiCreateAdmin(
                    appPreferences.getToken(),
                    CreateAdminRequest(
                        binding.tilEmail.editText?.text.toString(),
                        binding.tilName.editText?.text.toString(),
                        binding.tilMobile.editText?.text.toString()
                    )
                )
            } else if (binding.rbCustomer.isChecked) {
                viewModel.callApiCreateCustomer(
                    appPreferences.getToken(),
                    CreateCustomerRequest(
                        binding.tilEmail.editText?.text.toString(),
                        binding.tilName.editText?.text.toString(),
                        binding.tilMobile.editText?.text.toString()
                    )
                )
            } else {
                viewModel.callApiCreateEngineer(
                    appPreferences.getToken(),
                    CreateEngineerRequest(
                        binding.tilEmail.editText?.text.toString(),
                        binding.tilName.editText?.text.toString(),
                        binding.tilMobile.editText?.text.toString(),
                        binding.tilDesc.editText?.text.toString()
                    )
                )
            }
        }
        validation()
    }

    private fun validation() {
        binding.tilName.editText?.doOnTextChanged { _, _, _, _ ->
            if (validateUserName()) {
                binding.tilName.clearError()
                enableButton()
            } else {
                binding.btnSubmit.isEnabled = false
                binding.tilName.error = resources.getString(R.string.enter_valid_name)
            }
        }

        binding.tilMobile.editText?.doOnTextChanged { _, _, _, _ ->
            if (validateMobile()) {
                binding.tilMobile.clearError()
                enableButton()
            } else {
                binding.btnSubmit.isEnabled = false
                binding.tilMobile.error = resources.getString(R.string.enter_valid_mobile)
            }
        }

        binding.tilEmail.editText?.doOnTextChanged { _, _, _, _ ->
            if (validateEmail()) {
                binding.tilEmail.clearError()
                enableButton()
            } else {
                binding.btnSubmit.isEnabled = false
                binding.tilEmail.error = resources.getString(R.string.enter_valid_email)
            }
        }

        if (binding.tilDesc.visibility == View.VISIBLE) {
            binding.tilDesc.editText?.doOnTextChanged { _, _, _, _ ->
                if (validateDesc()) {
                    binding.tilDesc.clearError()
                    enableButton()
                } else {
                    binding.btnSubmit.isEnabled = false
                    binding.tilDesc.error = resources.getString(R.string.enter_valid_email)
                }
            }
        }
    }

    private fun enableButton() {
        binding.btnSubmit.isEnabled = validateUserName()
                && validateMobile()
                && validateEmail()
                && if (binding.tilDesc.visibility == View.VISIBLE) validateDesc() else true
    }

    private fun validateUserName() = binding.tilName.editText?.text.toString().isNotEmpty()
            && binding.tilName.editText?.text.toString().length > 2

    private fun validateMobile() = binding.tilMobile.editText?.text.toString().isNotEmpty()
            && binding.tilMobile.editText?.text.toString().length == 10

    private fun validateEmail() = binding.tilEmail.editText?.text.toString().isValidEmail()

    private fun validateDesc() = binding.tilDesc.editText?.text.toString().isNotEmpty()
            && binding.tilDesc.editText?.text.toString().length > 3


    private fun clearForm() {
        binding.tilName.editText?.text?.clear()
        binding.tilMobile.editText?.text?.clear()
        binding.tilEmail.editText?.text?.clear()
        binding.tilDesc.editText?.text?.clear()
        binding.tilName.clearError()
        binding.tilMobile.clearError()
        binding.tilEmail.clearError()
        binding.tilDesc.clearError()
    }

    override fun observeView() {
        viewModel.createAdminResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> showLoading()
                Resource.Status.SUCCESS -> {
                    hideLoading()
                    if (it.data?.status_code == 1) {
                        showAlert(it.data.message, AppConstants.AlertType.SUCCESS) { clearForm() }
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

        viewModel.createCustomerResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> showLoading()
                Resource.Status.SUCCESS -> {
                    hideLoading()
                    if (it.data?.status_code == 1) {
                        showAlert(it.data.message, AppConstants.AlertType.SUCCESS) { clearForm() }
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

        viewModel.createEngineerResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> showLoading()
                Resource.Status.SUCCESS -> {
                    hideLoading()
                    if (it.data?.status_code == 1) {
                        showAlert(it.data.message, AppConstants.AlertType.SUCCESS) { clearForm() }
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