package com.example.capstoneproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.R
import com.example.capstoneproject.entities.Game
import com.example.capstoneproject.entities.User


class GamesListAdapter(private val games: MutableList<Game>) : RecyclerView.Adapter<GamesListAdapter.ViewHolder>() {
    var onItemClick: ((Game,View, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        )
    }

    fun remove(position: Int): Game? {
        if (position < itemCount && position >= 0) {
            val item: Game = games.removeAt(position)
            notifyItemRemoved(position)
            return item
        }
        return null
    }

    fun add(item: Game?, position: Int) {
        games.add(position, item!!)
        notifyItemInserted(position)
    }

    override fun getItemCount(): Int = games.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(games[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(games[adapterPosition], itemView, adapterPosition)
            }
        }
        fun bind(game: Game) {
            itemView.findViewById<TextView>(R.id.tvDescription).text = getDescriptionFromEnd(game)
            itemView.findViewById<TextView>(R.id.tvTitle).text = game.name
        }
    }

    private fun getDescriptionFromEnd(game: Game): String? {
        //TODO: Create correct description
        return game.trackEnd
    }
}