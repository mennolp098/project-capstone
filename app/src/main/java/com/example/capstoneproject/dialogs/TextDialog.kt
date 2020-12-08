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


class TextDialog : DialogFragment() {
    // Use this instance of the interface to deliver action events
    private lateinit var listener: TextDialogListener
    private var hintText: String = "DEFAULT_STRING"
    private var inputHintText: String = "DEFAULT_STRING"

    // Interface for event listening.
    interface TextDialogListener {
        fun onTextDialogPositiveClick(dialog: DialogFragment, text: String)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }


    fun setParentFragment(fragment: Fragment)
    {
        try {
            listener = fragment as TextDialogListener
        } catch (e: ClassCastException) {
            // The parent fragment doesn't implement the interface, throw exception
            throw ClassCastException((fragment.toString() +
                    " must implement TextDialogListenerg"))
        }
    }

    fun setHintText(text: String) {
        hintText = text;
    }

    fun setInputFieldHintText(text: String) {
        inputHintText = text;
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val dialogView: View = inflater.inflate(R.layout.dialog_input, null)

            dialogView.findViewById<EditText>(R.id.etInputField).hint = hintText
            dialogView.findViewById<TextView>(R.id.tvHint).text = hintText

            builder.setView(dialogView)
                .setPositiveButton(R.string.create,
                    DialogInterface.OnClickListener { dialog, id ->
                        val input = getDialog()?.findViewById<EditText>(R.id.etInputField)?.text.toString()
                        listener.onTextDialogPositiveClick(this, input)

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
