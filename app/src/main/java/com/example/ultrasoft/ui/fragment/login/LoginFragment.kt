package com.example.ultrasoft.ui.fragment.login

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ultrasoft.BuildConfig
import com.example.ultrasoft.R
import com.example.ultrasoft.base.BaseFragment
import com.example.ultrasoft.data.model.login.LoginRequest
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.databinding.FragmentLoginBinding
import com.example.ultrasoft.utility.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()
    override fun setUpViews() {
        saveFcmToken()
        validation()
        binding.btnLogin.setOnClickListener {
            viewModel.callApiLogin(
                LoginRequest(
                    binding.tilUserName.editText?.text.toString(),
                    binding.tilPassword.editText?.text.toString(),
                    getCheckedRole()
                )
                ,appPreferences.getFcmToken())
        }
        binding.tvForgot.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToChangePasswordFragment(
                    true,
                    getCheckedRole()
                )
            )
        }

        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU)
        if(!hasNotificationPermission(requireContext())){
            permissionsResultCallback.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        subscribeFcmTopic()
    }

    private fun getCheckedRole(): String {
        val role =
            binding.rgRole.findViewById<RadioButton>(binding.rgRole.checkedRadioButtonId).text.toString()
        return if (role == resources.getString(R.string.support_staff)) {
            resources.getString(R.string.engineer).uppercase()
        } else {
            role.uppercase()
        }
    }

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
        viewModel.loginResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> showLoading()
                Resource.Status.SUCCESS -> {
                    hideLoading()
                    if (it.data?.status_code == 1) {
                        appPreferences.setToken(it.data.data.authToken)
                        appPreferences.setName(it.data.data.name.replace("\n", ""))
                        appPreferences.setRole(it.data.data.role)
                        findNavController().navigate(R.id.action_loginFragment_to_dashBoardFragment)
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
    private fun saveFcmToken() {
//        if (AppConstants.FCM_TOKEN_REFRESHED != "NA") {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task: Task<String?> ->
                if (!task.isSuccessful) {
                    logE(
                        "FCM", "Fetching FCM registration token failed :" + task.exception
                    )
                    return@OnCompleteListener
                }
                val token = task.result
                logE("FCM Token", token)
                appPreferences.setFcmToken(token)
            })
        }
//    }
    private fun subscribeFcmTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("ulAll")
            .addOnCompleteListener { task: Task<Void?> ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscribe failed"
                }
                logE("FCM TOPIC", msg)
            }
    }

}


