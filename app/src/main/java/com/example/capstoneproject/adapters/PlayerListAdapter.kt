package com.example.capstoneproject.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.R
import com.example.capstoneproject.entities.User


class PlayerListAdapter(private val players: MutableList<User>) : RecyclerView.Adapter<PlayerListAdapter.ViewHolder>() {
    var onItemClick: ((User, View, Int) -> Unit)? = null
    var isSelectionEnabled:Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_player, parent, false)
        )
    }


    fun remove(position: Int): User? {
        if (position < itemCount && position >= 0) {
            val item: User = players.removeAt(position)
            notifyItemRemoved(position)
            return item
        }
        return null
    }

    fun add(item: User?, position: Int) {
        players.add(position, item!!)
        notifyItemInserted(position)
    }

    override fun getItemCount(): Int = players.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(players[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(players[adapterPosition], itemView, adapterPosition)
            }
        }
        fun bind(player: User) {
            itemView.setBackgroundTintList(ColorStateList.valueOf(player.color))
            itemView.findViewById<TextView>(R.id.tvInitials).text = getInitialsFromFullName(player.fullName)

            if(isSelectionEnabled && !player.isAppOwner)
            {
                itemView.findViewById<View>(R.id.vSelected).alpha = 1F
                val textView = itemView.findViewById<TextView>(R.id.tvSelected)
                textView.alpha = 1F
                if(player.isSelected) {
                    textView.text = "V"
                    textView.setTextColor(ColorStateList.valueOf(itemView.resources.getColor(R.color.green)))
                } else if(!player.isSelected) {
                    textView.text = "X"
                    textView.setTextColor(ColorStateList.valueOf(itemView.resources.getColor(R.color.red)))
                }
            }
        }
    }

    private fun getInitialsFromFullName(fullName: String?): String {
        var initials = ""
        fullName?.split(" ")?.forEach {
            initials += it[0].toUpperCase()
        }

        return initials
    }
}