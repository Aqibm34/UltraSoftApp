//package com.example.ultrasoft.ui.adapters
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.ultrasoft.databinding.ComplaintListItemBinding
//
//class ComplaintsListAdapter(
//    private val list: List<ComplaintData>,
//    private val context: Context,
//    private val listener: (item: ComplaintData, type: ClickType) -> Unit
//) :
//    RecyclerView.Adapter<ComplaintsListAdapter.ViewHolder>() {
//    enum class ClickType { IMAGE, CHAT }
//
//    private val mapOfServices = mapOf(
//        "FINGPAY_AADHAR_SERVICE" to "Aadhaar Pay",
//        "FINGPAY_AEPS_BALANCEINQUIRY" to "AePS Balance Enquiry Kotak",
//        "YAEPS_BALANCEINQUIRY" to "AePS Balance Enquiry YesBank",
//        "FINGPAY_AEPS_CASHWITHDRAWL" to "AePS Cash Withdrawl Kotak",
//        "YAEPS_CASHWITHDRAWL" to "AePS Cash Withdrawl YesBank",
//        "FINGPAY_AEPS_MINISTATEMENT" to "AePS Mini Statement Kotak",
//        "YAEPS_MINISTATEMENT" to "AePS Mini Statement YesBank",
//        "FDMT_IMPS" to "DMT IMPS FINO",
//        "CMS" to "CMS",
//        "FDMT_NEFT" to "DMT NEFT FINO",
//        "EKYC" to "eKYC",
//        "CF_IMPS" to "Move Bank IMPS",
//        "CF_NEFT" to "Move Bank NEFT",
//        "MATM_BALANCEINQUIRY" to "mTAM Balance Enquiry",
//        "MATM_CASHWITHDRAWL" to "mTAM Cash Withdrawl",
//        "RAZORPAY" to "RazorPay",
//        "DTH_RECHARGE" to "Recharge DTH",
//        "MOBILE_RECHARGE" to "Recharge Mobile",
//        "OTHER" to "Other",
//        //        "BBPS Electricity" to "BBPS_ELECTRICITY"to
////        "BBPS Fastag" to "BBPS_FASTAG"to
////        "BBPS GAS" to "BBPS_GAS"to
////        "BBPS Loan" to "BBPS_LOAN"to
////        "BBPS LPG" to "BBPS_LPG_GAS"to
////        "BBPS Water" to "BBPS_WATAR"to
////        "DMT NEFT Yesbank" to "YDMT_NEFT"to
////        "DMT IMPS Yesbank" to "YDMT_IMPS"to
//    )
//
//
//    inner class ViewHolder(val binding: ComplaintListItemBinding) :
//        RecyclerView.ViewHolder(binding.root)
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(
//            ComplaintListItemBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        Utils.loadPicture(
//            context,
//            AppConstants.COMPLAINT_ATTACHMENT_URL + list[position].chats[0].complainattachment,
//            holder.binding.iv
//        )
//        holder.binding.tvCreatedBy.text = String.format(
//            "Created By ~ %s (%s)",
//            list[position].createdByName.trim().capitalizeWords(),
//            list[position].createdBy
//        )
//
//        holder.binding.tvName.text = String.format(
//            "%s (%s)",
//            list[position].retdisname.trim().capitalizeWords(),
//            list[position].retdisuniqueName
//        )
//
//        holder.binding.tvCreatedOn.text =
//            String.format(
//                "Created On ~ %s", Utils.parseDateWithTimeZone(
//                    list[position].createdDate
//                )
//            )
//
//        if (list[position].staus == "INPROCESS") {
//            if (list[position].seen == "TRUE") {
//                holder.binding.tvSeen.visibility = View.VISIBLE
//                holder.binding.tvCount.visibility = View.VISIBLE
//                holder.binding.tvSeen.text = list[position].seenDate?.let {
//                    Utils.parseDateWithTimeZone(
//                        it
//                    )
//                }
//            }
//            if (list[position].chats.size > 1) {
//                holder.binding.tvCount.text = (list[position].chats.size - 1).toString()
//            }else{
//                holder.binding.tvCount.visibility = View.GONE
//            }
//        } else {
//            holder.binding.tvCount.visibility = View.GONE
//            holder.binding.tvSeen.visibility = View.GONE
//        }
//
//        holder.binding.tvService.text = mapOfServices[list[position].sevice]
//        holder.binding.tvComplainId.text =
//            String.format("Complain ID: %s", list[position].complaintId)
//        holder.binding.clRoot.setOnClickListener {
//            listener(list[position], ClickType.CHAT)
//        }
//        holder.binding.iv.setOnClickListener {
//            listener(list[position], ClickType.IMAGE)
//        }
//
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//}