package com.example.ultrasoft.ui.module.popupmenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ultrasoft.R


class GridMenuAdapter : RecyclerView.Adapter<GridMenuAdapter.MenuViewHolder>() {

    var listener: GridMenuListener? = null

    private val menus = arrayListOf(
        Menu(
            "Gallery",
            R.drawable.ic_gallery
        ),
        Menu(
            "Video",
            R.drawable.ic_camera
        ),
        Menu(
            "Image",
            R.drawable.ic_camera
        ),
    )

    interface GridMenuListener {
        fun dismissPopup()
        fun onItemClick(name: String)
    }

    private val data = ArrayList<Menu>().apply {
        addAll(menus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder.create(
            parent
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(data[position], listener)
    }

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            menu: Menu,
            listener: GridMenuListener?
        ) {
            with(itemView) {
                val tvTitle: TextView = findViewById(R.id.tvTitle)
                val ivIcon: ImageView = findViewById(R.id.ivIcon)
                tvTitle.text = menu.name
                ivIcon.setImageDrawable(ContextCompat.getDrawable(context, menu.drawable))
                itemView.setOnClickListener {
                    listener?.onItemClick(menu.name)
                    listener?.dismissPopup()
                }
            }
        }

        companion object {
            val LAYOUT = R.layout.item_menu

            fun create(parent: ViewGroup): MenuViewHolder {
                return MenuViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        LAYOUT,
                        parent,
                        false
                    )
                )
            }
        }
    }

    data class Menu(val name: String, @DrawableRes val drawable: Int) {

    }
}