package com.example.capstoneproject.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.capstoneproject.R


class PointsDialog : DialogFragment() {
    // Use this instance of the interface to deliver action events
    private lateinit var listener: PointsDialogListener
    private var hintText: String = "DEFAULT_STRING"
    private var defaultValue: Int = 0
    private var buttonSteps: Array<Int> = arrayOf(0,0,0,0,0,0)
    private var points: Int = 0
    private var minimum: Int = 0
    private var maximum: Int = 0

    // Interface for event listening.
    interface PointsDialogListener {
        fun onPointsDialogPositiveClick(dialog: DialogFragment, points: Int)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }


    fun setParentFragment(fragment: Fragment)
    {
        try {
            listener = fragment as PointsDialogListener
        } catch (e: ClassCastException) {
            // The parent fragment doesn't implement the interface, throw exception
            throw ClassCastException((fragment.toString() +
                    " must implement PointsDialogListener"))
        }
    }

    fun setHintText(text: String) {
        hintText = text
    }

    fun setDefaultValue(points: Int)
    {
        this.points = points
        defaultValue = points
    }

    fun setMinimum(minimum: Int)
    {
        this.minimum = minimum
    }

    fun setMaximum(maximum: Int)
    {
        this.maximum = maximum
    }

    fun setValueSteps(steps: Array<Int>) {
        if(steps.size != 6)
        {
            Log.e("Project", "Steps are not filled in correctly!")
            return
        }
        this.buttonSteps = steps
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val dialogView: View = inflater.inflate(R.layout.dialog_points, null)

            initDialogView(dialogView)

            builder.setView(dialogView)
                .setPositiveButton(R.string.create,
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onPointsDialogPositiveClick(this, points)

                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onDialogNegativeClick(this)
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun initDialogView(dialogView: View) {
        val outputText = getNegativeOrPositiveText(defaultValue)
        dialogView.findViewById<TextView>(R.id.tvPointsOutput).text = outputText
        dialogView.findViewById<TextView>(R.id.tvHint).text = hintText
        dialogView.findViewById<Button>(R.id.btnMinusOne).text = getNegativeOrPositiveText(buttonSteps[0])
        dialogView.findViewById<Button>(R.id.btnMinusTwo).text = getNegativeOrPositiveText(buttonSteps[1])
        dialogView.findViewById<Button>(R.id.btnMinusThree).text = getNegativeOrPositiveText(buttonSteps[2])
        dialogView.findViewById<Button>(R.id.btnPlusOne).text = getNegativeOrPositiveText(buttonSteps[3])
        dialogView.findViewById<Button>(R.id.btnPlusTwo).text = getNegativeOrPositiveText(buttonSteps[4])
        dialogView.findViewById<Button>(R.id.btnPlusThree).text = getNegativeOrPositiveText(buttonSteps[5])

        setListeners(dialogView)
    }

    private fun setListeners(dialogView: View) {
        dialogView.findViewById<Button>(R.id.btnMinusOne).setOnClickListener{
            addPoints(buttonSteps[0], dialogView)
        }
        dialogView.findViewById<Button>(R.id.btnMinusTwo).setOnClickListener{
            addPoints(buttonSteps[1], dialogView)
        }
        dialogView.findViewById<Button>(R.id.btnMinusThree).setOnClickListener{
            addPoints(buttonSteps[2], dialogView)
        }
        dialogView.findViewById<Button>(R.id.btnReset).setOnClickListener{
            resetPoints(dialogView)
        }
        dialogView.findViewById<Button>(R.id.btnPlusOne).setOnClickListener{
            addPoints(buttonSteps[3], dialogView)
        }
        dialogView.findViewById<Button>(R.id.btnPlusTwo).setOnClickListener{
            addPoints(buttonSteps[4], dialogView)
        }
        dialogView.findViewById<Button>(R.id.btnPlusThree).setOnClickListener{
            addPoints(buttonSteps[5], dialogView)
        }
    }

    private fun resetPoints(dialogView: View) {
        points = defaultValue

        val outputText = getNegativeOrPositiveText(points)
        dialogView.findViewById<TextView>(R.id.tvPointsOutput).text = outputText
    }

    private fun addPoints(i: Int, dialogView: View) {
        points += i

        if(points > maximum)
        {
            points = maximum
        } else if(points < minimum)
        {
            points = minimum
        }

        val outputText = getNegativeOrPositiveText(points)
        dialogView.findViewById<TextView>(R.id.tvPointsOutput).text = outputText
    }

    private fun getNegativeOrPositiveText(value: Int): String
    {
        return if(value > 0) {
            "+ $value"
        } else {
            "$value"
        }
    }
}
