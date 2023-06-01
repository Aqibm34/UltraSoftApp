package com.example.ultrasoft.ui.fragment.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ultrasoft.R
import com.example.ultrasoft.base.BaseFragment
import com.example.ultrasoft.databinding.FragmentUsersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersFragment : BaseFragment<FragmentUsersBinding>(FragmentUsersBinding::inflate) {

    override fun setUpViews() {
        binding.tb.setUpToolbar()
        binding.tvCreate.setOnClickListener { findNavController().navigate(R.id.action_usersFragment_to_createUserFragment) }
        binding.tvList.setOnClickListener { findNavController().navigate(R.id.action_usersFragment_to_allUsersFragment) }
    }


}