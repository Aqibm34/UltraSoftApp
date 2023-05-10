package com.example.ultrasoft.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.ultrasoft.R
import com.example.ultrasoft.utility.AppPreferences

abstract class BaseActivity<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) :
    AppCompatActivity() {

    private var _binding: VB? = null
    val binding
        get() = _binding as VB

    val appPreferences: AppPreferences
        get() = AppPreferences.getInstance(this)

    private var dialogLoading: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater.invoke(layoutInflater)
        if (_binding == null) {
            throw IllegalArgumentException("Binding cannot be null.")
        }
        installTheme()
        setContentView(binding.root)
        setUpViews()
        observeView()
    }

    open fun installTheme(){}

    open fun setUpViews() {}
    open fun observeView() {}

    fun showLoading(context: Context) {
        if (dialogLoading == null) {
            dialogLoading = Dialog(context)
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
        } catch (e: java.lang.Exception) {
            // Handle or log or ignore
        } finally {
            dialogLoading = null
        }
    }


}
