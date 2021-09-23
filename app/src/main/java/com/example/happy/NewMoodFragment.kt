package com.example.happy

import android.content.Intent
import android.location.Location
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.moodfit.placeholder.singletonDB
import com.google.android.gms.maps.GoogleMap


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewMoodFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewMoodFragment : DialogFragment() {

    interface NewMoodFragmentListener {
        fun passNewMoodEntry(data: MoodEntry)
    }

    private lateinit var mMap: GoogleMap
    private lateinit var location: Location

    var listener: NewMoodFragmentListener? = null
    lateinit var dbman: singletonDB.happyDB
    var mood: String = "Happy"
    var lat: Double = 0.0
    var lng: Double = 0.0

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var mood_intensity = 1
        dbman = singletonDB.DBinstance!!



        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_new_mood, container, false)

        // prompt camera to take picture
        view.findViewById<ImageView>(R.id.add_picture_image_view).setOnClickListener() {
            activity?.let {
                val intent = Intent(it, MapsActivity::class.java)
                it.startActivityForResult(intent,1)
            }
        }

        val spinner = view.findViewById<Spinner>(R.id.mood_description_spinner)
        ArrayAdapter.createFromResource(
            context!!, R.array.moods_array,
            android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }



        // Rate our intensity
        val intensity_number_picker = view.findViewById<NumberPicker>(R.id.intensity_picker)
        intensity_number_picker.minValue = 1
        intensity_number_picker.maxValue = 10
        intensity_number_picker.setOnValueChangedListener() {intensity_number_picker, oldVal, newVal ->
            mood_intensity = intensity_number_picker.getValue()
        }


        // submit button - create new mood entry and pass to the calling activity
        view.findViewById<Button>(R.id.add_new_mood_button).setOnClickListener() {

            var newMood = MoodEntry(view.findViewById<Spinner>(R.id.mood_description_spinner).selectedItem.toString(),
                mood_intensity, view.findViewById<EditText>(R.id.mood_notes).text.toString(), 0.0, 0.0)
            listener?.passNewMoodEntry(newMood)
        }


        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewMoodFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewMoodFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



}