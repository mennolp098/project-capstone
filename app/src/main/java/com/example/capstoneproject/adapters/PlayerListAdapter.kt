package com.example.capstoneproject.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.R
import com.example.capstoneproject.models.User
import kotlin.random.Random


class PlayerListAdapter(private val players: List<User>) : RecyclerView.Adapter<PlayerListAdapter.ViewHolder>() {
    var onItemClick: ((User) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_player, parent, false)
        )
    }

    override fun getItemCount(): Int = players.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(players[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(players[adapterPosition])
            }
        }
        fun bind(player: User) {

            val androidColors: IntArray = itemView.resources.getIntArray(R.array.playercolors)
            var randomAndroidColor = androidColors[Random.nextInt(androidColors.size)]

            if(player.isAppOwner!!)
            {
                randomAndroidColor = itemView.resources.getColor(R.color.colorPlayerOwner);
            }

            itemView.setBackgroundTintList(ColorStateList.valueOf((randomAndroidColor)))
            itemView.findViewById<TextView>(R.id.tvInitials).text = getInitialsFromFullName(player.fullName);
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