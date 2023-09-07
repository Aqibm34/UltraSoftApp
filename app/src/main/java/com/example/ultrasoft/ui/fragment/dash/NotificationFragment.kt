package com.example.ultrasoft.ui.fragment.dash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ultrasoft.R
import com.example.ultrasoft.base.BaseFragment
import com.example.ultrasoft.databinding.FragmentNotificationBinding
import com.example.ultrasoft.ui.adapters.NotificarionRvAdapter
import com.example.ultrasoft.utility.AppConstants
import com.example.ultrasoft.utility.showAlertWithButtonConfig


class NotificationFragment :
    BaseFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {
    private var adapter: NotificarionRvAdapter? = null
    override fun setUpViews() {
        binding.tb.setUpToolbar("Notifications")
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        val list = appPreferences.getNotificationList()

        adapter = NotificarionRvAdapter(list, requireContext()) {
            showAlertWithButtonConfig(
                requireContext(),
                "Delete ?",
                "Are you you want to delete this notifications",
                AppConstants.AlertType.INFO,
                resources.getString(R.string.no),
                resources.getString(R.string.yes)
            ) { res ->
                if (res == AppConstants.AlertResponseType.YES) {
                    list.removeAt(it)
                    adapter?.updateList(list)
                    appPreferences.addDataInNotificationList(list)
                }
            }

        }

        binding.tvClear.setOnClickListener {
            showAlertWithButtonConfig(
                requireContext(),
                "Delete ?",
                "Are you you want to delete all notifications",
                AppConstants.AlertType.INFO,
                resources.getString(R.string.no),
                resources.getString(R.string.yes)
            ) { res ->
                if (res == AppConstants.AlertResponseType.YES) {
                    list.clear()
                    appPreferences.addDataInNotificationList(list)
                    adapter?.updateList(list)
                }
            }

        }

        binding.rv.adapter = adapter
    }


}