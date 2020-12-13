package com.example.capstoneproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.R
import com.example.capstoneproject.adapters.PlayerListAdapter
import com.example.capstoneproject.dialogs.ConfirmDialog
import com.example.capstoneproject.dialogs.TextDialog
import com.example.capstoneproject.entities.User
import com.example.capstoneproject.room.UserRepository
import com.example.capstoneproject.utils.ColorUtils
import com.example.capstoneproject.viewmodels.UserViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_manage_players.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * create an instance of this fragment.
 */
class ManagePlayersFragment : Fragment(),
                                TextDialog.TextDialogListener,
                                ConfirmDialog.ConfirmDialogListener    {
    private val playersList = arrayListOf<User>()
    private val playerListAdapter =
        PlayerListAdapter(playersList)
    private var currentSelectedPlayer: User? = null
    private var currentSelectedPlayerListPosition: Int? = null
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_players, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeUsers()
        initRv()
        setListeners(view)
    }

    private fun observeUsers() {
        userViewModel.userList.observe(viewLifecycleOwner, Observer {
                users  ->
            users?.let {
                playersList.clear()
                playersList.addAll(users)
                playerListAdapter.notifyDataSetChanged()
            }
        })
        userViewModel.error.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        })

        userViewModel.success.observe(viewLifecycleOwner, Observer {     success ->
            //Nothing to do here..
        })
    }


    private fun setListeners(view: View) {
        view.findViewById<FloatingActionButton>(R.id.fbAdd).setOnClickListener{
            onAddPlayerButtonPressed()
        }
    }

    private fun onAddPlayerButtonPressed() {
        showCreatePlayerDialog()
    }

    private fun showCreatePlayerDialog() {
        val newFragment = TextDialog()
        newFragment.setParentFragment(this)
        newFragment.setHintText(resources.getString(R.string.new_player_hint))
        newFragment.setInputFieldHintText(resources.getString(R.string.full_name))
        newFragment.show(childFragmentManager, "create-profile-dialog")
    }

    override fun onTextDialogPositiveClick(dialog: DialogFragment, text: String) {
        createNewPlayer(text)
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        removeSelectedPlayer(this.requireView())
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        // Cancel button is pressed
    }


    private fun initRv() {
        viewManager = GridLayoutManager(activity, 2)

        playerListAdapter.onItemClick = { player, view, position ->
            if(!player.isAppOwner) {
                showRemovePlayerDialog(player, position)
            }
        }

        rvPlayers.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = playerListAdapter
        }
    }

    private fun showRemovePlayerDialog(player: User, position: Int) {
        currentSelectedPlayer = player
        currentSelectedPlayerListPosition = position

        val newFragment = ConfirmDialog()
        newFragment.setParentFragment(this)
        newFragment.setHintText(resources.getString(R.string.remove_player))
        newFragment.show(childFragmentManager, "remove-profile-dialog")
    }

    private fun removeSelectedPlayer(view: View) {
        val user = currentSelectedPlayer
        val position = currentSelectedPlayerListPosition
        playerListAdapter.remove(position!!)
        val snackbar: Snackbar =
            Snackbar.make(view, user?.fullName.toString() + " is deleted", Snackbar.LENGTH_LONG)
        snackbar.setAction(
            "Undo"
        ) {
            // Remove from adapter only so we won't query room db
            playerListAdapter.add(user, position)
        }
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar, event: Int) {
                if (event == DISMISS_EVENT_TIMEOUT) {
                    if (user != null) {
                        userViewModel.delete(user)
                    }
                }
            }
        })
        snackbar.show()
    }

    /**
     * Save first user in app.
     */
    private fun createNewPlayer(fullName: String) {
        val randomColor = ColorUtils().getRandomAndroidColor(resources)
        userViewModel.createUser(fullName, false, randomColor)
    }
}