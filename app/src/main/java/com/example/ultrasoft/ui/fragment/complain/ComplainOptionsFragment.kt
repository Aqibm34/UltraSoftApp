package com.example.ultrasoft.ui.fragment.complain

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ultrasoft.R
import com.example.ultrasoft.base.BaseFragment
import com.example.ultrasoft.databinding.FragmentComplainOptionsBinding
import com.example.ultrasoft.utility.AppConstants
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ComplainOptionsFragment :
    BaseFragment<FragmentComplainOptionsBinding>(FragmentComplainOptionsBinding::inflate) {

    private val args:ComplainOptionsFragmentArgs by navArgs()
    override fun setUpViews() {
        binding.tb.setUpToolbar()
        if(appPreferences.getRole() == AppConstants.UserTypes.ADMIN.name||appPreferences.getRole() == AppConstants.UserTypes.ENGINEER.name){
            binding.tvCreate.visibility = View.GONE
        }
        binding.tvCreate.setOnClickListener { findNavController().navigate(R.id.action_complainOptionsFragment_to_createComplainFragment) }
        binding.tvList.setOnClickListener { findNavController().navigate(ComplainOptionsFragmentDirections.actionComplainOptionsFragmentToAllComplainFragment(args.countData,0)) }
    }

}