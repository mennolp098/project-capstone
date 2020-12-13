package com.example.capstoneproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.capstoneproject.R
private const val ARG_USER_FULL_NAME = "user_full_name"
private const val ARG_POINTS_BETWEEN_FIRST_AND_SECOND = "points_between_first_and_second"

/**
 * A simple [Fragment] subclass.
 * Use the [GameEndFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameEndFragment : Fragment() {
    private var userFullName: String? = null
    private var pointsBetweenFirstAndSecond: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userFullName = it.getString(ARG_USER_FULL_NAME)
            pointsBetweenFirstAndSecond = it.getInt(ARG_POINTS_BETWEEN_FIRST_AND_SECOND)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_end, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners(view)
        showDetails(view)
    }

    private fun showDetails(view: View) {
        view.findViewById<TextView>(R.id.tvPointsBetween).text = resources.getString(R.string.points_between, pointsBetweenFirstAndSecond!!)
        view.findViewById<TextView>(R.id.tvPlayerInitials).text = getPlayerInitials(userFullName)
    }

    private fun getPlayerInitials(userFullName: String?): CharSequence? {
        var initials = ""
        userFullName?.split(" ")?.forEach {
            initials += it[0].toUpperCase()
        }

        return initials
    }

    private fun setListeners(view: View) {
        view.findViewById<Button>(R.id.btnBackToMain).setOnClickListener{
            onBackToMainButtonPressed()
        }
        view.findViewById<Button>(R.id.btnStartNewRound).setOnClickListener{
            onStartNewRoundPressed()
        }
    }

    private fun onBackToMainButtonPressed() {
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.mainFragment, false).build()
        findNavController().navigate(R.id.action_gameEndFragment_to_mainFragment, null, navOptions)
    }

    private fun onStartNewRoundPressed() {
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.startGameFragment, false).build()
        findNavController().navigate(R.id.action_gameEndFragment_to_startGameFragment, null, navOptions)
    }

}