package com.example.capstoneproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.capstoneproject.R
import com.example.capstoneproject.models.User
import com.example.capstoneproject.room.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class NewUserFragment : Fragment() {
    private lateinit var userRepository: UserRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners(view);
        userRepository = UserRepository(requireContext())
    }
    /**
     * Create listeners for this view.
     */
    private fun setListeners(view: View) {
        view.findViewById<Button>(R.id.btn_nav).setOnClickListener {
            onNavButtonPressed(view)
        }
    }

    /**
     * On nav button pressed check if name is filled in and navigate towards main view.
     */
    private fun onNavButtonPressed(view: View) {
        val fullNameEditText: EditText = view.findViewById(R.id.et_full_name)
        val fullName: String = fullNameEditText.text.toString()
        if(fullName.isNotEmpty()) {
            saveUser(fullName);

            findNavController().navigate(R.id.action_newUserFragment_to_mainFragment)
        }
    }

    /**
     * Save first user in app.
     */
    private fun saveUser(fullName: String) {
        mainScope.launch {
            val user = User(
                uid = 0,
                fullName = fullName,
                isAppOwner = true
            )
            withContext(Dispatchers.IO) {
                userRepository.create(user)
            }
        }
    }
}