package com.example.capstoneproject.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.capstoneproject.R


class ConfirmDialog : DialogFragment() {
    // Use this instance of the interface to deliver action events
    internal lateinit var listener: ConfirmDialogListener
    private lateinit var hintText: String

    // Interface for event listening.
    interface ConfirmDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }


    fun setParentFragment(fragment: Fragment)
    {
        try {
            listener = fragment as ConfirmDialogListener
        } catch (e: ClassCastException) {
            // The parent fragment doesn't implement the interface, throw exception
            throw ClassCastException((fragment.toString() +
                    " must implement ConfirmDialogListener"))
        }
    }

    fun setHintText(text: String) {
        hintText = text;
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val dialogView: View = inflater.inflate(R.layout.dialog_confirm, null)

            dialogView.findViewById<TextView>(R.id.tvHint).text = hintText
            builder.setView(dialogView)
                .setPositiveButton(R.string.confirm,
                    DialogInterface.OnClickListener { dialog, id ->
                        val fullName = getDialog()?.findViewById<EditText>(R.id.etInputField)?.text.toString()
                        listener.onDialogPositiveClick(this)
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onDialogNegativeClick(this)
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
