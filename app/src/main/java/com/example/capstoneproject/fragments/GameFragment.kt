package com.example.capstoneproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.R
import com.example.capstoneproject.adapters.PlayerListAdapter
import com.example.capstoneproject.models.Game
import com.example.capstoneproject.models.User
import com.example.capstoneproject.room.GameRepository
import com.example.capstoneproject.room.UserRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_manage_players.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ARG_GAME_ID = "game_id"
private const val ARG_PROFILE_IDS = "profile_ids"

/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment() {
    private lateinit var gameRepository: GameRepository
    private lateinit var userRepository: UserRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private var gameID: Int? = null
    private var playerIDs: ArrayList<Int>? = null
    private var game: Game? = null
    private val playersList = arrayListOf<User>()
    private val playerListAdapter =
        PlayerListAdapter(playersList)
    private var currentSelectedPlayer: User? = null
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            gameID = it.getInt(ARG_GAME_ID)
            playerIDs = it.getIntegerArrayList(ARG_PROFILE_IDS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRepository = UserRepository(requireContext())
        gameRepository = GameRepository(requireContext())
        retrieveGameRules()
        retrievePlayers()
        initRv()
        setListeners(view)
    }

    private fun setListeners(view: View) {

    }

    private fun initRv() {
        viewManager = GridLayoutManager(activity, 2)

        playerListAdapter.onItemClick = { player, view ->
            showPointsDialog(player)
        }

        rvPlayers.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = playerListAdapter
        }
    }

    private fun showPointsDialog(player: User) {
        currentSelectedPlayer = player
    }

    private fun retrievePlayers() {
        mainScope.launch {
            val users = withContext(Dispatchers.IO) {
                userRepository.getAll()
            }
            this@GameFragment.playersList.clear()
            this@GameFragment.playersList.addAll(users)
            this@GameFragment.playerListAdapter.notifyDataSetChanged()
        }
    }

    private fun retrieveGameRules() {
        // Game rules are set
        mainScope.launch {
            val game = withContext(Dispatchers.IO) {
                gameRepository.getById(gameID!!)
            }

            this@GameFragment.game = game
        }
    }
}