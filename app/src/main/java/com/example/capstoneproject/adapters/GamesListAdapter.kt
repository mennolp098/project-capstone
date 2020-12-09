package com.example.capstoneproject.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.R
import com.example.capstoneproject.models.Game
import com.example.capstoneproject.models.User
import kotlin.random.Random


class GamesListAdapter(private val games: List<Game>) : RecyclerView.Adapter<GamesListAdapter.ViewHolder>() {
    var onItemClick: ((Game,View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        )
    }

    override fun getItemCount(): Int = games.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(games[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(games[adapterPosition], itemView)
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