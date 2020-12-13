package com.example.capstoneproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.capstoneproject.R
import com.example.capstoneproject.entities.User
import com.example.capstoneproject.room.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {
    private lateinit var userRepository: UserRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private lateinit var owner: User

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRepository = UserRepository(requireContext())
        getWelcomeText(view)
        setListeners(view)
    }

    /**
     * Create listeners for this view.
     */
    private fun setListeners(view: View) {
        view.findViewById<Button>(R.id.btnStartNewGame).setOnClickListener {
            onStartGameButtonClicked(view)
        }
        view.findViewById<Button>(R.id.btnManagePlayers).setOnClickListener {
            onManagePlayersButtonPressed(view)
        }
        view.findViewById<Button>(R.id.btnManageGames).setOnClickListener {
            onManageGamesButtonClicked(view)
        }
        view.findViewById<Button>(R.id.btnHistory).setOnClickListener {
            onHistoryButtonClicked(view)
        }
        view.findViewById<Button>(R.id.btnLeaderboards).setOnClickListener {
            onLeaderboardsButtonClicked(view)
        }

    }

    /**
     * Navigates towards  game fragment
     */
    private fun onStartGameButtonClicked(view: View) {
        findNavController().navigate(R.id.action_mainFragment_to_startGameFragment)
    }

    /**
     * Navigates towards player fragment
     */
    private fun onManagePlayersButtonPressed(view: View) {
        findNavController().navigate(R.id.action_mainFragment_to_managePlayersFragment)
    }

    /**
     * Navigates towards manage games fragment
     */
    private fun onManageGamesButtonClicked(view: View) {
        findNavController().navigate(R.id.action_mainFragment_to_manageGamesFragment)
    }

    /**
     * Navigates towards history fragment
     */
    private fun onHistoryButtonClicked(view: View) {
        findNavController().navigate(R.id.action_mainFragment_to_historyFragment)

    }

    /**
     * Navigates towards leaderboards fragment
     */
    private fun onLeaderboardsButtonClicked(view: View) {
        Toast.makeText(context, resources.getText(R.string.not_yet_implemented), Toast.LENGTH_SHORT).show()
        return
        findNavController().navigate(R.id.action_mainFragment_to_leaderboardsFragment)
    }

    /**
     * Retrieve correct owner to show welcome text with username.
     */
    private fun getWelcomeText(view: View) {
        mainScope.launch {
            val user = withContext(Dispatchers.IO) {
                userRepository.getOwner()
            }
            this@MainFragment.owner = user

            this@MainFragment.showWelcomeText(view)
        }
    }

    /**
     * Show welcome text/
     */
    private fun showWelcomeText(view: View) {
        view.findViewById<TextView>(R.id.tvWelcome).text = getString(R.string.welcome_user,
            owner?.fullName
        )
    }
}