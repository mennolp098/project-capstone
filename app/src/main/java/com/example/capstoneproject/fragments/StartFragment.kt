package com.example.capstoneproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.capstoneproject.R
import com.example.capstoneproject.entities.User
import com.example.capstoneproject.room.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartFragment : Fragment() {
    private lateinit var userRepository: UserRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private var owner: User? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRepository = UserRepository(requireContext())
        navigateToCorrectFragment()
    }

    private fun navigateToCorrectFragment() {
        mainScope.launch {
            val user = withContext(Dispatchers.IO) {
                userRepository.getOwner()
            }

            if(user != null) {
                findNavController().navigate(R.id.action_startFragment_to_mainFragment)
            } else {
                findNavController().navigate(R.id.action_startFragment_to_newUserFragment)
            }
        }


    }

}