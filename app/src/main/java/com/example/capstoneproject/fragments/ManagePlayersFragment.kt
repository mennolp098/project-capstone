package com.example.capstoneproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.R
import com.example.capstoneproject.adapters.PlayerListAdapter
import com.example.capstoneproject.dialogs.TextDialog
import com.example.capstoneproject.dialogs.ConfirmDialog
import com.example.capstoneproject.entities.User
import com.example.capstoneproject.room.UserRepository
import com.example.capstoneproject.utils.ColorUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
    private lateinit var userRepository: UserRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val playersList = arrayListOf<User>()
    private val playerListAdapter =
        PlayerListAdapter(playersList)
    private var currentSelectedPlayer: User? = null
    private lateinit var viewManager: RecyclerView.LayoutManager

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

        userRepository = UserRepository(requireContext())
        retrievePlayers()
        initRv()
        setListeners(view)
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
        removeSelectedPlayer()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        // Cancel button is pressed
    }


    private fun initRv() {
        viewManager = GridLayoutManager(activity, 2)

        playerListAdapter.onItemClick = { player, view ->
            showRemovePlayerDialog(player)
        }

        rvPlayers.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = playerListAdapter
        }
    }

    private fun showRemovePlayerDialog(player: User) {
        currentSelectedPlayer = player

        val newFragment = ConfirmDialog()
        newFragment.setParentFragment(this)
        newFragment.setHintText(resources.getString(R.string.remove_player))
        newFragment.show(childFragmentManager, "remove-profile-dialog")
    }

    private fun retrievePlayers() {
        mainScope.launch {
            val users = withContext(Dispatchers.IO) {
                userRepository.getAll()
            }
            this@ManagePlayersFragment.playersList.clear()
            this@ManagePlayersFragment.playersList.addAll(users)
            this@ManagePlayersFragment.playerListAdapter.notifyDataSetChanged()
        }
    }

    private fun removeSelectedPlayer() {
        val user = currentSelectedPlayer
        mainScope.launch {
            withContext(Dispatchers.IO) {
                if (user != null) {
                    userRepository.delete(user)
                }

                this@ManagePlayersFragment.retrievePlayers()
            }
        }
    }

    /**
     * Save first user in app.
     */
    private fun createNewPlayer(fullName: String) {
        mainScope.launch {
            val randomColor = ColorUtils().getRandomAndroidColor(resources)
            val user = User(
                userUid = null,
                fullName = fullName,
                isAppOwner = false,
                isSelected = false,
                color = randomColor
            )

            withContext(Dispatchers.IO) {
                userRepository.create(user)
            }

            this@ManagePlayersFragment.retrievePlayers()
        }
    }
}