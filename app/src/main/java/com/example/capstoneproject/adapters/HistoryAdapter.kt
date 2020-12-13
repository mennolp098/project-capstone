package com.example.capstoneproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.R
import com.example.capstoneproject.entities.relations.GameSessionWithPlayerResultsAndGame
import com.example.capstoneproject.entities.relations.PlayerResultWithUser
import java.text.SimpleDateFormat


class HistoryAdapter(private val games: List<GameSessionWithPlayerResultsAndGame>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()
    var onItemClick: ((GameSessionWithPlayerResultsAndGame,View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_game_session, parent, false)
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
        fun bind(gameSessionWithPlayerResultsAndGame: GameSessionWithPlayerResultsAndGame) {
            val formatter = SimpleDateFormat("dd-MM-yy")
            val formattedDate = formatter.format(gameSessionWithPlayerResultsAndGame.gameSessionWithGame.gameSession.createdAt)
            itemView.findViewById<TextView>(R.id.tvGameSessionTitle).text = itemView.resources.getString(R.string.game_session_title, gameSessionWithPlayerResultsAndGame.gameSessionWithGame.game.name, formattedDate)
            val viewManager: LinearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            val playerResultListAdapter =
                PlayerResultListAdapter(gameSessionWithPlayerResultsAndGame.playerResults)

            viewManager.initialPrefetchItemCount = gameSessionWithPlayerResultsAndGame.playerResults.size

            val rvPlayerResult = itemView.findViewById<RecyclerView>(R.id.rvPlayerResult)
            rvPlayerResult.apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = playerResultListAdapter
            }

            playerResultListAdapter.useSmallLayout = true
            playerResultListAdapter.onItemClick = { playerResultWithUser: PlayerResultWithUser, view: View ->
                onItemClick?.invoke(games[adapterPosition], itemView)
            }

            rvPlayerResult.setRecycledViewPool(viewPool)
        }
    }
}