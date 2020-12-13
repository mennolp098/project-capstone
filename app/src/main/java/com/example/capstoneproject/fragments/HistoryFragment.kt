package com.example.capstoneproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.R
import com.example.capstoneproject.adapters.HistoryAdapter
import com.example.capstoneproject.adapters.PlayerListAdapter
import com.example.capstoneproject.dialogs.ConfirmDialog
import com.example.capstoneproject.entities.User
import com.example.capstoneproject.entities.relations.GameSessionWithPlayerResultsAndGame
import com.example.capstoneproject.room.GameSessionRepository
import com.example.capstoneproject.room.UserRepository
import com.example.capstoneproject.viewmodels.GameSessionViewModel
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_manage_players.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * A simple [Fragment] subclass.
 * Use the [historyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment(),
                        ConfirmDialog.ConfirmDialogListener {

    private val gameSessionViewModel: GameSessionViewModel by viewModels()
    private val gameSessionList = arrayListOf<GameSessionWithPlayerResultsAndGame>()
    private val historyAdapter =
        HistoryAdapter(gameSessionList)
    private var currentSelectedGameSession: GameSessionWithPlayerResultsAndGame? = null
    private lateinit var viewManager: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeGameSessionModel()
        initRv()
    }

    private fun observeGameSessionModel() {
        gameSessionViewModel.gameSessionWithPlayerResultsAndGameList.observe(viewLifecycleOwner, Observer {
                games  ->
            games?.let {
                gameSessionList.clear()
                gameSessionList.addAll(games)
                historyAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun initRv() {
        viewManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)


        historyAdapter.onItemClick = { gameSession, view ->
            showOpenGameSessionDialog(gameSession)
        }

        rvHistory.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = historyAdapter
        }
    }

    private fun showOpenGameSessionDialog(gameSession: GameSessionWithPlayerResultsAndGame) {
        // Do not show continue progress on already finished game.
        if(gameSession.gameSessionWithGame.gameSession.isFinished)
        {
            return
        }

        currentSelectedGameSession = gameSession

        val newFragment = ConfirmDialog()
        newFragment.setParentFragment(this)
        newFragment.setHintText(resources.getString(R.string.open_game_session))
        newFragment.show(childFragmentManager, "open-game-session-dialog")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        val gameSessionId = currentSelectedGameSession?.gameSessionWithGame!!.gameSession.gameSessionUid
        val bundle = bundleOf("game_session_id" to gameSessionId)
        findNavController().navigate(R.id.action_historyFragment_to_gameFragment, bundle)
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        // Not needed
    }
}