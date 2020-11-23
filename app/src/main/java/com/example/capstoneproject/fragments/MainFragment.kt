package com.example.capstoneproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.capstoneproject.R
import com.example.capstoneproject.models.User
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
    private var owner: User? = null;

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
        showWelcomeText(view)
    }

    private fun showWelcomeText(view: View) {
        mainScope.launch {
            val user = withContext(Dispatchers.IO) {
                userRepository.getOwner()
            }
            this@MainFragment.owner = user;
        }

        view.findViewById<TextView>(R.id.tv_welcome).text = getString(R.string.welcome_user,
            owner?.fullName
        )
    }
}