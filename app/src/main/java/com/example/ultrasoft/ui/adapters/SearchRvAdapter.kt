package com.example.ultrasoft.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ultrasoft.databinding.LayoutSearchItemBinding

class SearchRvAdapter(
    private var list: List<Any>,
    private val context: Context,
    private val listener: (data: Any) -> Unit
) : RecyclerView.Adapter<SearchRvAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LayoutSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: MutableList<Any>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutSearchItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (list[position]) {
//            is DisResultSet -> {
//                val data = (list[position] as DisResultSet)
//                holder.binding.tvText.text = String.format(
//                    "%s(%s)", data.DISTRIBUTORNAME.trim().capitalizeWords(), data.DISTRIBUTORCODE.trim()
//                )
//            }
//            is RetResultSet -> {
//                val data = (list[position] as RetResultSet)
//                holder.binding.tvText.text = String.format(
//                    "%s(%s)", data.RETAILERNAME.trim().capitalizeWords(), data.RETAILERUNIQUENAME.trim()
//                )
//            }
//
//            is EmpData -> {
//                val data = (list[position] as EmpData)
//                holder.binding.tvText.text = String.format(
//                    "%s(%s)", data.employeeName.trim().capitalizeWords(), data.employeeCode.trim()
//                )
//            }
        }
        holder.binding.tvText.setOnClickListener {
            listener(list[position])
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}