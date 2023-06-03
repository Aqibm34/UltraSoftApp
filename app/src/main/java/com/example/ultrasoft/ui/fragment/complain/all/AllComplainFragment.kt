package com.example.ultrasoft.ui.fragment.complain.all

import com.example.ultrasoft.base.BaseFragment
import com.example.ultrasoft.databinding.FragmentAllComplainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllComplainFragment : BaseFragment<FragmentAllComplainBinding>(FragmentAllComplainBinding::inflate) {


    override fun setUpViews() {
        binding.tb.setUpToolbar()
    }

    override fun observeView() {

    }

}