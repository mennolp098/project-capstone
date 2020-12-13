package com.example.capstoneproject.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.R
import com.example.capstoneproject.adapters.GamesListAdapter
import com.example.capstoneproject.adapters.PlayerListAdapter
import com.example.capstoneproject.entities.Game
import com.example.capstoneproject.entities.User
import com.example.capstoneproject.room.GameRepository
import com.example.capstoneproject.room.UserRepository
import com.example.capstoneproject.viewmodels.GameViewModel
import com.example.capstoneproject.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.fragment_manage_games.*
import kotlinx.android.synthetic.main.fragment_manage_players.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * A simple [Fragment] subclass.
 * Use the [StartGameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartGameFragment : Fragment() {
    private val playersList = arrayListOf<User>()
    private val playerListAdapter =
        PlayerListAdapter(playersList)
    private var gamesList = arrayListOf<Game>()
    private val gamesListAdapter =
        GamesListAdapter(gamesList)
    private lateinit var playerViewManager: RecyclerView.LayoutManager
    private lateinit var gameViewManager: RecyclerView.LayoutManager

    private val gameViewModel: GameViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeUsers()
        observeGames()
        initRvs()
        setListeners(view)
    }

    private fun setListeners(view: View) {
        view.findViewById<Button>(R.id.btnManagePlayers).setOnClickListener{
            findNavController().navigate(R.id.action_startGameFragment_to_managePlayersFragment)
        }
    }

    private fun observeGames() {
        gameViewModel.gamesList.observe(viewLifecycleOwner, Observer {
                games  ->
            games?.let {
                gamesList.clear()
                gamesList.addAll(games)
                gamesListAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun observeUsers() {
        userViewModel.userList.observe(viewLifecycleOwner, Observer {
                users  ->
            users?.let {
                playersList.clear()
                playersList.addAll(users)
                playerListAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun initRvs() {
        gameViewManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        playerViewManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        playerListAdapter.isSelectionEnabled = true
        playerListAdapter.onItemClick = { player, view, position ->
            onPlayerClicked(player, view)
        }

        gamesListAdapter.onItemClick = { game, view, position ->
            onGameClicked(game, view)
        }

        rvPlayers.apply {
            setHasFixedSize(true)
            layoutManager = gameViewManager
            adapter = playerListAdapter
        }

        rvGames.apply {
            setHasFixedSize(true)
            layoutManager = playerViewManager
            adapter = gamesListAdapter
        }
    }

    private fun onPlayerClicked(player: User, view: View) {
        player.isSelected = !player.isSelected
        playerListAdapter.notifyDataSetChanged()

        updatePlayer(player)
    }

    private fun updatePlayer(player: User) {
        userViewModel.update(player)
    }

    private fun onGameClicked(game: Game, view: View) {
        val playerIds = ArrayList<Int>();
        playersList.forEach { player ->
            if(player.isSelected || player.isAppOwner) {
                playerIds.add(player.userUid!!)
            }
        }
        val bundle = bundleOf("game_id" to game.gameUid, "player_ids" to playerIds)
        findNavController().navigate(R.id.action_startGameFragment_to_startGameRulesFragment, bundle)
    }
}