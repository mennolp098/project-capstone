package com.example.capstoneproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.capstoneproject.R
import com.example.capstoneproject.entities.GameSession
import com.example.capstoneproject.extensions.observeOnce
import com.example.capstoneproject.viewmodels.GameSessionViewModel
import com.example.capstoneproject.viewmodels.GameViewModel
import com.example.capstoneproject.viewmodels.PlayerResultViewModel


private const val ARG_GAME_ID = "game_id"
private const val ARG_PROFILE_IDS = "player_ids"

/**
 * A simple [Fragment] subclass.
 * Use the [startGameRulesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartGameRulesFragment : Fragment(),
                                GameSessionViewModel.GameSessionViewModelListener,
                                PlayerResultViewModel.PlayerResultViewModelListener {
    private var createdGameSessionUid: Int? = null
    private var gameID: Int? = null
    private var playerIDs: ArrayList<Int>? = null

    private val gameViewModel: GameViewModel by viewModels()
    private val gameSessionViewModel: GameSessionViewModel by viewModels()
    private val playerResultViewModel: PlayerResultViewModel by viewModels()

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
        return inflater.inflate(R.layout.fragment_start_game_rules, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameSessionViewModel.setParentFragment(this)
        playerResultViewModel.setParentFragment(this)

        observeGameRules(view)
        setListeners(view)
    }

    private fun setListeners(view: View) {
        view.findViewById<Button>(R.id.btnCreate).setOnClickListener{
            onStartGameButtonClicked()
        }
    }

    private fun onStartGameButtonClicked() {
        createGameSession()
    }

    private fun createPlayerResults(gameSessionId: Int) {
        playerResultViewModel.createPlayerResults(playerIDs!!, gameSessionId)
    }

    private fun createGameSession() {
        gameSessionViewModel.createGameSession(gameID!!)
    }

    private fun observeGameRules(view: View)
    {
        val amountEditText = view.findViewById<EditText>(R.id.etAmount)
        val nameEditText = view.findViewById<EditText>(R.id.etGameName)
        gameID?.let { gameid ->
            gameViewModel.getGameById(gameid).observe(viewLifecycleOwner, Observer { games ->
                games?.let { game ->
                    amountEditText.setText(game.trackAmount)
                    nameEditText.setText(game.name)
                }
            })
        }

        disableEditText(amountEditText)
        disableEditText(nameEditText)
        //TODO: end and kind not yet implemented.
    }

    private fun disableEditText(editText: EditText)
    {
        editText.setTextColor(resources.getColor(R.color.disabled))
        editText.isFocusable = false
        editText.isEnabled = false
        editText.isCursorVisible = false
        editText.keyListener = null
    }

    override fun onGameSessionCreated(gameSession: GameSession) {
        gameSession.gameSessionUid?.let {
            createdGameSessionUid = it
            createPlayerResults(it)
        }
    }

    override fun onPlayerResultsCreated() {
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.mainFragment, false).build()
        val bundle = bundleOf("game_session_id" to createdGameSessionUid)
        findNavController().navigate(
            R.id.action_startGameRulesFragment_to_gameFragment,
            bundle,
            navOptions
        )
    }
}