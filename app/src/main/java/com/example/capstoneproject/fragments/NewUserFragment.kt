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
import com.example.capstoneproject.entities.User
import com.example.capstoneproject.extensions.observeOnce
import com.example.capstoneproject.room.UserRepository
import com.example.capstoneproject.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class NewUserFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels()

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
        observeUserViewModel()
    }

    private fun observeUserViewModel() {
        userViewModel.error.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        })

        userViewModel.success.observeOnce(viewLifecycleOwner, Observer {     success ->
            findNavController().navigate(R.id.action_newUserFragment_to_mainFragment)
        })
    }
    /**
     * Create listeners for this view.
     */
    private fun setListeners(view: View) {
        view.findViewById<Button>(R.id.btnStartNewGame).setOnClickListener {
            onNavButtonPressed(view)
        }
    }

    /**
     * On nav button pressed check if name is filled in and navigate towards main view.
     */
    private fun onNavButtonPressed(view: View) {
        val fullNameEditText: EditText = view.findViewById(R.id.etInputField)
        val fullName: String = fullNameEditText.text.toString()

        userViewModel.createUser(fullName, true, resources.getColor(R.color.colorPlayerOwner))
    }
}