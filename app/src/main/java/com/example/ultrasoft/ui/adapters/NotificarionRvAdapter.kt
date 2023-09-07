package com.example.ultrasoft.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ultrasoft.R
import com.example.ultrasoft.data.model.NotificationData
import com.example.ultrasoft.data.model.user.admin.AdminData
import com.example.ultrasoft.data.model.user.customer.CustomerData
import com.example.ultrasoft.data.model.user.engineer.EngineerData
import com.example.ultrasoft.databinding.LayoutNotificaionListItemBinding
import com.example.ultrasoft.databinding.UsersListItemBinding
import com.example.ultrasoft.utility.AppConstants
import com.example.ultrasoft.utility.Utils

class NotificarionRvAdapter(
    private var list: MutableList<NotificationData>,
    private val context: Context,
    private val listener: (data: Int) -> Unit
) : RecyclerView.Adapter<NotificarionRvAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LayoutNotificaionListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: MutableList<NotificationData>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutNotificaionListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvTitle.text = list[position].title
        holder.binding.tvText.text = list[position].body
        Utils.loadPicture(context, list[position].image, holder.binding.iv, R.drawable.ic_info)
        holder.binding.tvDelete.setOnClickListener {
            listener(position)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }
}