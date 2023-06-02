package com.example.ultrasoft.ui.fragment.complain

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ultrasoft.R
import com.example.ultrasoft.base.BaseFragment
import com.example.ultrasoft.databinding.FragmentComplainOptionsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ComplainOptionsFragment :
    BaseFragment<FragmentComplainOptionsBinding>(FragmentComplainOptionsBinding::inflate) {

    override fun setUpViews() {
        binding.tvCreate.setOnClickListener { findNavController().navigate(R.id.action_complainOptionsFragment_to_createComplainFragment) }
        binding.tvList.setOnClickListener { findNavController().navigate(R.id.action_complainOptionsFragment_to_allComplainFragment) }
    }

}