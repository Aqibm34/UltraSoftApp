package com.example.ultrasoft.ui.fragment.login

import android.widget.RadioButton
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.example.ultrasoft.R
import com.example.ultrasoft.base.BaseFragment
import com.example.ultrasoft.databinding.FragmentLoginBinding
import com.example.ultrasoft.utility.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()
    override fun setUpViews() {
        validation()
        binding.btnLogin.setOnClickListener {
            requireContext().toast("Login")
            viewModel.login(
                binding.tilUserName.editText?.text.toString(),
                binding.tilPassword.editText?.text.toString(),
                getCheckedRole()
            )
        }
    }

    private fun getCheckedRole() =
        binding.rgRole.findViewById<RadioButton>(binding.rgRole.checkedRadioButtonId).text.toString()


    private fun validation() {
        binding.tilUserName.editText?.doOnTextChanged { _, _, _, _ ->
            if (validateUserName()) {
                binding.tilUserName.error = null
                enableButton()
            } else {
                disableButton()
                binding.tilUserName.error =
                    resources.getString(R.string.please_enter_valid_username)
            }
        }

        binding.tilPassword.editText?.doOnTextChanged { _, _, _, _ ->
            if (validatePassword()) {
                binding.tilPassword.error = null
                enableButton()
            } else {
                disableButton()
                binding.tilPassword.error =
                    resources.getString(R.string.please_enter_valid_password)
            }
        }
    }

    private fun validateUserName() = binding.tilUserName.editText?.text.toString().isNotEmpty()
    private fun validatePassword() = binding.tilPassword.editText?.text.toString().isNotEmpty()

    private fun enableButton() {
        binding.btnLogin.isEnabled = validateUserName() && validatePassword()
    }

    private fun disableButton() {
        binding.btnLogin.isEnabled = false
    }

    override fun observeView() {

    }


}