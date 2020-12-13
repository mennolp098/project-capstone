package com.example.capstoneproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.R
import com.example.capstoneproject.adapters.GamesListAdapter
import com.example.capstoneproject.dialogs.ConfirmDialog
import com.example.capstoneproject.entities.Game
import com.example.capstoneproject.room.GameRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_manage_games.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 * Use the [manageGamesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ManageGamesFragment : Fragment(),
    ConfirmDialog.ConfirmDialogListener    {
    private lateinit var gameRepository: GameRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val gamesList = arrayListOf<Game>()
    private val gamesListAdapter =
        GamesListAdapter(gamesList)
    private var currentSelectedGame: Game? = null
    private var currentSelectedGamePosition: Int? = null
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_games, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameRepository = GameRepository(requireContext())
        retrieveGames()
        initRv()
        setListeners(view)
    }

    private fun setListeners(view: View) {
        view.findViewById<FloatingActionButton>(R.id.fbAdd).setOnClickListener{
            onAddGameButtonPressed()
        }
    }

    private fun onAddGameButtonPressed() {
        findNavController().navigate(R.id.action_manageGamesFragment_to_newGameFragment)
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        removeSelectedGame()
    }

    private fun removeSelectedGame()
    {
        val game = currentSelectedGame
        val position = currentSelectedGamePosition
        gamesListAdapter.remove(position!!)
        val snackbar: Snackbar =
            Snackbar.make(requireView(), game?.name.toString() + " is deleted", Snackbar.LENGTH_LONG)
        snackbar.setAction(
            "Undo"
        ) {
            gamesListAdapter.add(game, position)
        }
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar, event: Int) {
                if (event == DISMISS_EVENT_TIMEOUT) {
                    mainScope.launch {
                        withContext(Dispatchers.IO) {
                            if (game != null) {
                                gameRepository.delete(game)
                            }
                        }
                    }
                }
            }
        })
        snackbar.show()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        // Cancel button is pressed
    }


    private fun initRv() {
        viewManager = GridLayoutManager(activity, 2)

        gamesListAdapter.onItemClick = { game, view, position ->
            showRemoveGameDialog(game, position)
        }

        rvGames.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = gamesListAdapter
        }
    }

    private fun showRemoveGameDialog(game: Game, position: Int) {
        currentSelectedGame = game
        currentSelectedGamePosition = position

        val newFragment = ConfirmDialog()
        newFragment.setParentFragment(this)
        newFragment.setHintText(resources.getString(R.string.remove_game))
        newFragment.show(childFragmentManager, "remove-game-dialog")
    }

    private fun retrieveGames() {
        mainScope.launch {
            val games = withContext(Dispatchers.IO) {
                gameRepository.getAll()
            }
            this@ManageGamesFragment.gamesList.clear()
            this@ManageGamesFragment.gamesList.addAll(games)
            this@ManageGamesFragment.gamesListAdapter.notifyDataSetChanged()
        }
    }
}