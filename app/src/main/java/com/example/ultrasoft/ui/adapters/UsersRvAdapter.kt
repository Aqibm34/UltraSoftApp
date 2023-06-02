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
import com.example.ultrasoft.data.model.user.admin.AdminData
import com.example.ultrasoft.data.model.user.customer.CustomerData
import com.example.ultrasoft.data.model.user.engineer.EngineerData
import com.example.ultrasoft.databinding.UsersListItemBinding
import com.example.ultrasoft.utility.AppConstants

class UsersRvAdapter(
    private var list: List<Any>,
    private val context: Context,
    private val listener: (data: Any) -> Unit
) : RecyclerView.Adapter<UsersRvAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: UsersListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: MutableList<Any>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            UsersListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (list[position]) {
            is AdminData -> {
                val data = list[position] as AdminData
                holder.binding.tvId.text = String.format("Id: %s", data.adminId)
                holder.binding.tvName.text = data.name
                holder.binding.tvPhone.text = data.phoneNumber
                holder.binding.tvEmail.text = data.emailId
                holder.binding.tvStatus.text = data.activeStatus
                setUpActiveViews(data.activeStatus, holder.binding.tvStatus, holder.binding.tvBlock)
                holder.binding.tvDesc.visibility = View.GONE
                holder.binding.tvBlock.setOnClickListener {
                    listener(data)
                }
            }
            is CustomerData -> {
                val data = list[position] as CustomerData
                holder.binding.tvId.text = String.format("Id: %s", data.customerId)
                holder.binding.tvName.text = data.customerName
                holder.binding.tvPhone.text = data.customerMobile
                holder.binding.tvEmail.text = data.customerEmail
                holder.binding.tvStatus.text = data.activeStatus
                setUpActiveViews(data.activeStatus, holder.binding.tvStatus, holder.binding.tvBlock)
                holder.binding.tvDesc.visibility = View.GONE
                holder.binding.tvBlock.setOnClickListener {
                    listener(data)
                }
            }
            is EngineerData -> {
                val data = list[position] as EngineerData
                holder.binding.tvId.text = String.format("Id: %s", data.engineerId)
                holder.binding.tvName.text = data.engineerName
                holder.binding.tvPhone.text = data.engineerMobile
                holder.binding.tvEmail.text = data.engineerEmailId
                holder.binding.tvStatus.text = data.activeStatus
                setUpActiveViews(data.activeStatus, holder.binding.tvStatus, holder.binding.tvBlock)
                holder.binding.tvDesc.text = data.engineerWorkDiscription
                holder.binding.tvDesc.visibility = View.VISIBLE
                holder.binding.tvBlock.setOnClickListener {
                    listener(data)
                }
            }
        }
    }

    private fun setUpActiveViews(status: String, tv: TextView, tvBlock: TextView) {
        if (status == AppConstants.STATUS.ACTIVE.name) {
            tv.setTextColor(ContextCompat.getColor(context, R.color.green))
            tvBlock.backgroundTintList = ContextCompat.getColorStateList(context, R.color.red)
        } else {
            tv.setTextColor(ContextCompat.getColor(context, R.color.red))
            tvBlock.backgroundTintList = ContextCompat.getColorStateList(context, R.color.green)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }
}