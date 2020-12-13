package com.example.capstoneproject.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.R
import com.example.capstoneproject.adapters.PlayerListAdapter
import com.example.capstoneproject.adapters.PlayerResultListAdapter
import com.example.capstoneproject.dialogs.ConfirmDialog
import com.example.capstoneproject.dialogs.PointsDialog
import com.example.capstoneproject.entities.Game
import com.example.capstoneproject.entities.GameSession
import com.example.capstoneproject.entities.PlayerResult
import com.example.capstoneproject.entities.User
import com.example.capstoneproject.entities.relations.PlayerResultWithUser
import com.example.capstoneproject.room.GameRepository
import com.example.capstoneproject.room.GameSessionRepository
import com.example.capstoneproject.room.PlayerResultRepository
import com.example.capstoneproject.room.UserRepository
import com.example.capstoneproject.utils.ValueStepsUtil
import kotlinx.android.synthetic.main.fragment_manage_players.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ARG_GAME_SESSION_ID = "game_session_id"

/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment(),
    PointsDialog.PointsDialogListener  {
    private lateinit var gameSessionRepository: GameSessionRepository
    private lateinit var playerResultRepository: PlayerResultRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private var gameSessionId: Int? = null
    private var gameSession: GameSession? = null
    private var game: Game? = null
    private val playersResultList = arrayListOf<PlayerResultWithUser>()
    private val playerResultListAdapter =
        PlayerResultListAdapter(playersResultList)
    private var currentSelectedPlayerResult: PlayerResultWithUser? = null
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            gameSessionId = it.getInt(ARG_GAME_SESSION_ID)
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

        gameSessionRepository = GameSessionRepository(requireContext())
        playerResultRepository = PlayerResultRepository(requireContext())

        retrieveGameSession()
        initRv()
        setListeners(view)
    }

    private fun setListeners(view: View) {

    }

    private fun initRv() {
        viewManager = GridLayoutManager(activity, 2)

        playerResultListAdapter.onItemClick = { playerResult, view ->
            showPointsDialog(playerResult)
        }

        rvPlayers.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = playerResultListAdapter
        }
    }

    private fun showPointsDialog(playerResult: PlayerResultWithUser) {
        currentSelectedPlayerResult = playerResult

        val newFragment = PointsDialog()
        newFragment.setParentFragment(this)
        newFragment.setHintText(resources.getString(R.string.add_points))
        newFragment.setDefaultValue(playerResult.playerResult.amount)
        newFragment.setValueSteps(ValueStepsUtil().getValueStepsFromAmount(game?.trackAmount!!.toInt()))
        newFragment.setMinimum(0)
        newFragment.setMaximum(game?.trackAmount!!.toInt())
        newFragment.show(childFragmentManager, "add-points-dialog")
    }

    private fun retrieveGameSession() {
        mainScope.launch {
            val gameSessionWithPlayerResultsAndGame = withContext(Dispatchers.IO) {
                gameSessionRepository.getSessionWithPlayerResultsAndGameById(gameSessionId!!)
            }

            val game = gameSessionWithPlayerResultsAndGame.gameSessionWithGame.game
            val playerResults = gameSessionWithPlayerResultsAndGame.playerResults

            this@GameFragment.gameSession = gameSessionWithPlayerResultsAndGame.gameSessionWithGame.gameSession
            this@GameFragment.game = game
            this@GameFragment.playersResultList.clear()
            this@GameFragment.playersResultList.addAll(playerResults)
            this@GameFragment.playerResultListAdapter.notifyDataSetChanged()
        }
    }

    override fun onPointsDialogPositiveClick(dialog: DialogFragment, points: Int) {
        currentSelectedPlayerResult?.playerResult!!.amount = points
        playerResultListAdapter.notifyDataSetChanged()

        if(points == game?.trackAmount!!.toInt()) {
            currentSelectedPlayerResult?.playerResult!!.isWinner = true
            onPlayerWon(currentSelectedPlayerResult?.user!!)
        }

        mainScope.launch {
            withContext(Dispatchers.IO) {
                playerResultRepository.update(currentSelectedPlayerResult?.playerResult!!)
            }
        }
    }

    private fun onPlayerWon(user: User) {
        // Set gamesession to finished
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameSession?.isFinished = true
                gameSessionRepository.update(gameSession!!)
            }
        }

        openPlayerWonView(user)
    }

    private fun openPlayerWonView(user: User) {
        val userFullName = user.fullName
        var pointsBetweenFirstAndSecond: Int = game?.trackAmount!!.toInt()
        playersResultList.forEach{
            if(it.user != user) {
                val pointsBetweenIt = game?.trackAmount!!.toInt() - it.playerResult.amount
                if (pointsBetweenIt < pointsBetweenFirstAndSecond) {
                    pointsBetweenFirstAndSecond = pointsBetweenIt
                }
            }
        }

        val bundle = bundleOf("user_full_name" to userFullName, "points_between_first_and_second" to pointsBetweenFirstAndSecond)
        findNavController().navigate(R.id.action_gameFragment_to_gameEndFragment, bundle)
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        // Nothing to do here.
    }
}