package com.example.happy

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import java.time.LocalDate
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DateFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)

    // This dialog fragment is responsible for taking user input for setting the date and time for the gratitude
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.date_picker, container, false)

        // get the TimePicker
        var time = v.findViewById<TimePicker>(R.id.timePicker)
        var chosenDate = LocalDate.now()

        // initialize the DatePicker and retrieve the input date
        val today = Calendar.getInstance()
        var date = v.findViewById<DatePicker>(R.id.datePicker)
        date.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, monthOfYear, dayOfMonth ->
            chosenDate = LocalDate.of(year, monthOfYear+1, dayOfMonth)
        }

        // when the user presses the ok button, pass the selected date and time back to the datePicker method in GratitudeJournal
        var okButton = v.findViewById<Button>(R.id.okB)
        okButton?.setOnClickListener {
            Log.d("DEBUG", "OK button pressed")
            Log.d("DEBUG", "Date: $chosenDate Time: ${time.hour}:${time.minute}")
            var min = "${time.minute}"
            if(time.minute < 10) min = "0" + time.minute
            (activity as? GratitudeJournal)?.datePicker("Date: $chosenDate ${time.hour}:$min")
        }

        // Inflate the layout for this fragment
        return v
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EnterWorkoutDialogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}