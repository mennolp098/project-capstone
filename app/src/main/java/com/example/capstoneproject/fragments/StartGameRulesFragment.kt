package com.example.capstoneproject.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.capstoneproject.R
import com.example.capstoneproject.models.Game
import com.example.capstoneproject.room.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ARG_GAME_ID = "game_id"
private const val ARG_PROFILE_IDS = "profile_ids"

/**
 * A simple [Fragment] subclass.
 * Use the [startGameRulesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartGameRulesFragment : Fragment() {
    private lateinit var gameRepository: GameRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private var gameID: Int? = null
    private var playerIDs: ArrayList<Int>? = null
    private var game: Game? = null

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

        gameRepository = GameRepository(requireContext())
        initialiseGameRules(view)
        setListeners(view)
    }

    private fun setListeners(view: View) {
        view.findViewById<Button>(R.id.btnCreate).setOnClickListener{
            onStartGameButtonClicked()
        }
    }

    private fun onStartGameButtonClicked() {
        //findNavController().navigate(R.id.)
    }

    private fun initialiseGameRules(view: View) {
        // Game rules aren't set
        if(gameID == null) return

        // Game rules are set
        mainScope.launch {
            val game = withContext(Dispatchers.IO) {
                gameRepository.getById(gameID!!)
            }

            this@StartGameRulesFragment.game = game
            setGameRules(view)
        }
    }

    private fun setGameRules(view: View)
    {
        val amountEditText = view.findViewById<EditText>(R.id.etAmount)
        val nameEditText = view.findViewById<EditText>(R.id.etGameName)

        amountEditText.setText(game?.trackAmount)
        disableEditText(amountEditText)

        nameEditText.setText(game?.name)
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
}