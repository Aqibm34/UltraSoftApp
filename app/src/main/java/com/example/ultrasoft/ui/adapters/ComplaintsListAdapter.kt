package com.example.ultrasoft.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ultrasoft.data.model.complain.ComplainData
import com.example.ultrasoft.databinding.ComplaintListItemBinding
import com.example.ultrasoft.utility.AppConstants.Companion.ATTACHMENT_URL
import com.example.ultrasoft.utility.Utils
import com.example.ultrasoft.utility.capitalizeWords

class ComplaintsListAdapter(
    private val list: List<ComplainData>,
    private val context: Context,
    private val listener: (item: ComplainData, type: ClickType) -> Unit
) :
    RecyclerView.Adapter<ComplaintsListAdapter.ViewHolder>() {
    enum class ClickType { IMAGE, CHAT }

    inner class ViewHolder(val binding: ComplaintListItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ComplaintListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Utils.loadPicture(
            context,
            ATTACHMENT_URL + list[position].complaintChats[0].attachment,
            holder.binding.iv
        )
        holder.binding.tvCreatedBy.text = String.format(
            "Created By ~ %s (%s)",
            list[position].createdByCUstomer.customerName.trim().capitalizeWords(),
            list[position].createdByCUstomer.customerId
        )

        list[position].assignedByAdmin?.let {
            holder.binding.tvName.text = String.format(
                "%s (%s)",
                it.name?.trim()?.capitalizeWords(),
                it.adminId
            )
        }


        holder.binding.tvCreatedOn.text =
            String.format(
                "Created On ~ %s", Utils.parseDateWithTimeZone(
                    list[position].createdDate
                )
            )

        if (list[position].status == "OPEN") {
//            if (list[position].seen == "TRUE") {
//                holder.binding.tvSeen.visibility = View.VISIBLE
//                holder.binding.tvCount.visibility = View.VISIBLE
//                holder.binding.tvSeen.text = list[position].seenDate?.let {
//                    Utils.parseDateWithTimeZone(
//                        it
//                    )
//                }
//            }
            if (list[position].complaintChats.size > 1) {
                holder.binding.tvCount.text = (list[position].complaintChats.size - 1).toString()
            } else {
                holder.binding.tvCount.visibility = View.GONE
            }
        } else {
            holder.binding.tvCount.visibility = View.GONE
            holder.binding.tvSeen.visibility = View.GONE
        }

        holder.binding.tvService.text = String.format("Asset Category: %s",list[position].assetCategory.assetCategoryName)
        holder.binding.tvComplainId.text =
            String.format("Complain ID: %s", list[position].complainId)
        holder.binding.clRoot.setOnClickListener {
            listener(list[position], ClickType.CHAT)
        }
        holder.binding.iv.setOnClickListener {
            listener(list[position], ClickType.IMAGE)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}