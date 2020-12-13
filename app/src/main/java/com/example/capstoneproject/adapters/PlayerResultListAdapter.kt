package com.example.capstoneproject.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.R
import com.example.capstoneproject.entities.PlayerResult
import com.example.capstoneproject.entities.relations.PlayerResultWithUser


class PlayerResultListAdapter(private val playerResults: List<PlayerResultWithUser>) : RecyclerView.Adapter<PlayerResultListAdapter.ViewHolder>() {
    var onItemClick: ((PlayerResultWithUser, View) -> Unit)? = null
    var useSmallLayout: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layout = R.layout.item_player_result
        if(useSmallLayout) {
            layout = R.layout.item_small_player_result
        }
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(layout, parent, false)
        )
    }

    override fun getItemCount(): Int = playerResults.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(playerResults[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(playerResults[adapterPosition], itemView)
            }
        }
        fun bind(playerResult: PlayerResultWithUser) {
            itemView.findViewById<View>(R.id.clBackground).setBackgroundTintList(ColorStateList.valueOf(playerResult.user.color))
            itemView.findViewById<TextView>(R.id.tvInitials).text = getInitialsFromFullName(playerResult.user.fullName)
            itemView.findViewById<TextView>(R.id.tvPoints).text = playerResult.playerResult.amount.toString()

            if(playerResult.playerResult.isWinner)
            {
                itemView.findViewById<TextView>(R.id.tvWinner).alpha = 1f
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