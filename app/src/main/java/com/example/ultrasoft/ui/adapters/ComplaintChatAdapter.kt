package com.example.ultrasoft.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ultrasoft.data.model.complain.AssignedByAdminData
import com.example.ultrasoft.data.model.complain.AssignedToEngineerData
import com.example.ultrasoft.data.model.complain.Chat
import com.example.ultrasoft.data.model.complain.CreatedByCustomerData
import com.example.ultrasoft.databinding.ComplaintChatLayoutBinding
import com.example.ultrasoft.databinding.ComplaintChatLayoutUserBinding
import com.example.ultrasoft.utility.AppConstants
import com.example.ultrasoft.utility.Utils
import com.example.ultrasoft.utility.logE

class ComplaintChatAdapter(
    private var list: MutableList<Chat>,
    private var customerData: CreatedByCustomerData,
    private var engineerData: AssignedToEngineerData?,
    private var adminData: AssignedByAdminData?,
    private val context: Context,
    private val listener: (item: Chat) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = ComplaintsListAdapter::class.java.simpleName

    inner class ViewHolder(val binding: ComplaintChatLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ViewHolderUser(val binding: ComplaintChatLayoutUserBinding) :
        RecyclerView.ViewHolder(binding.root)


    fun updateList(chat: MutableList<Chat>) {
        list = chat
        notifyItemInserted(list.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {
                return ViewHolder(
                    ComplaintChatLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            1 -> {
                return ViewHolderUser(
                    ComplaintChatLayoutUserBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                throw Exception("Invalid ViewHolder Chat View Type")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            when (holder.itemViewType) {
                0 -> {
                    (holder as ViewHolder)
                    Utils.loadPicture(
                        context, AppConstants.ATTACHMENT_URL + list[position].attachment,
                        holder.binding.ivAttachment
                    )

                    holder.binding.tvUser.text = customerData.customerName
                    engineerData?.let {

                        if (it.engineerName.isEmpty() && it.engineerId.isEmpty()) {
                            holder.binding.tvResolvedBy.visibility = View.GONE
                        } else {
                            holder.binding.tvResolvedBy.visibility = View.VISIBLE
                            holder.binding.tvResolvedBy.text = String.format(
                                "%s ~ %s@ultrasoft.in",
                                it.engineerName,
                                it.engineerId.substring(0, it.engineerId.indexOf("@"))
                            )
                        }
                    }

//                    holder.binding.tvDate.text = list[position].createdDate.let {
//                        Utils.parseDateWithTimeZone(
//                            it
//                        )
//                    }
                    holder.binding.tvDescription.text = list[position].remark
                    holder.binding.ivAttachment.visibility =
                        if (list[position].attachment.isEmpty()) View.GONE else View.VISIBLE
                    holder.binding.ivAttachment.setOnClickListener {
                        listener(list[position])
                    }

                }

                1 -> {
                    (holder as ViewHolderUser)
                    Utils.loadPicture(
                        context,
                        AppConstants.ATTACHMENT_URL + list[position].attachment,
                        holder.binding.ivAttachment
                    )

                    holder.binding.tvUser.text = customerData.customerName
                    engineerData?.let {
                        if (it.engineerName.isEmpty() && it.engineerId.isEmpty()) {
                            holder.binding.tvResolvedBy.visibility = View.GONE
                        } else {
                            holder.binding.tvResolvedBy.visibility = View.VISIBLE
                            holder.binding.tvResolvedBy.text = String.format(
                                "%s ~ %s",
                                it.engineerName,
                                it.engineerId
                            )
                        }
                    }
//                    holder.binding.tvDate.text = list[position].createdDate.let {
//                        Utils.parseDateWithTimeZone(
//                            it
//                        )
//                    }
                    holder.binding.tvDescription.text = list[position].remark
                    holder.binding.ivAttachment.visibility =
                        if (list[position].attachment.isEmpty()) View.GONE else View.VISIBLE
                    holder.binding.ivAttachment.setOnClickListener {
                        listener(list[position])
                    }
                }

                else -> {
                    throw Exception("Invalid Chat View Type")
                }
            }


        } catch (e: Exception) {
            logE(TAG, e.message)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].role == AppConstants.UserTypes.ADMIN.name
            || list[position].role == AppConstants.UserTypes.ENGINEER.name
        ) {
            0
        } else {
            1
        }
    }
}