package com.example.capstoneproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.capstoneproject.R
import com.example.capstoneproject.models.Game
import com.example.capstoneproject.room.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 * Use the [NewGameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewGameFragment : Fragment() {
    private lateinit var gameRepository: GameRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameRepository = GameRepository(requireContext())
        setListeners(view)
    }

    private fun setListeners(view: View) {
        view.findViewById<Button>(R.id.btnCreate).setOnClickListener{
            onCreateButtonClicked(view)
        }
    }

    /**
     * On create button clicked create game with given details.
     */
    private fun onCreateButtonClicked(view: View) {
        val name = view.findViewById<EditText>(R.id.etGameName).text.toString()
        val trackAmount = view.findViewById<EditText>(R.id.etAmount).text.toString()

        //TODO: Handle correct chosen button
        val trackKind = resources.getString(R.string.numeric)
        val trackEnd = resources.getString(R.string.win)

        val game = Game(
            uid = null,
            name = name,
            trackKind = trackKind,
            trackAmount = trackAmount,
            trackEnd = trackEnd,
            isSelected = false
        )

        saveGame(game)
    }

    /**
     * Save a new game
     */
    private fun saveGame(game: Game) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository.create(game)

                this@NewGameFragment.onGameSaved()
            }
        }
    }

    /**
     * On game saved navigate back.
     */
    private fun onGameSaved() {
        activity?.onBackPressed()
    }
}