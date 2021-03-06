package com.example.capstoneproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.capstoneproject.R
import com.example.capstoneproject.entities.Game
import com.example.capstoneproject.room.GameRepository
import com.example.capstoneproject.viewmodels.GameViewModel
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
    val gameViewModel:GameViewModel by viewModels()

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

        setListeners(view)
        observeGameViewModel()
    }

    private fun observeGameViewModel() {
        gameViewModel.error.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        })

        gameViewModel.success.observe(viewLifecycleOwner, Observer {     success ->
            findNavController().popBackStack()
        })
    }

    private fun setListeners(view: View) {
        view.findViewById<Button>(R.id.btnCreate).setOnClickListener{
            onCreateButtonClicked(view)
        }
        view.findViewById<Button>(R.id.btnLose).setOnClickListener{
            Toast.makeText(context, resources.getText(R.string.not_yet_implemented), Toast.LENGTH_SHORT).show()
        }
        view.findViewById<Button>(R.id.btnLetters).setOnClickListener{
            Toast.makeText(context, resources.getText(R.string.not_yet_implemented), Toast.LENGTH_SHORT).show()
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

        gameViewModel.createGame(name, trackEnd, trackAmount, trackKind)
    }
}