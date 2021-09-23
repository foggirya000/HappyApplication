package com.example.happy

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import com.example.moodfit.placeholder.singletonDB
//import com.example.moodfit.placeholder.happyDB
import java.time.LocalDate

class GratitudeJournal : AppCompatActivity() {

    /*
       need a Gratitude and DateFragment instance to load the data
       the complete boolean will trigger upon user's first added entry to the list
     */
    lateinit var dateFragment: DateFragment
    lateinit var fragment: GratitudeFragment
    @RequiresApi(Build.VERSION_CODES.O)
    var date: String = ""
    var complete = false

//    lateinit var dbman: JournalDatabaseManager

    lateinit var dbman: singletonDB.happyDB


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gratitude_journal)

        savedInstanceState?.let {
            var savedDate = savedInstanceState.getString("date")
            runOnUiThread() {
                findViewById<TextView>(R.id.date_text).text = savedDate
                if (savedDate != null) {
                    date = savedDate
                }
            }
        }


        dbman = singletonDB.DBinstance!!


        // initialize the DialogFragment



        dateFragment = DateFragment.newInstance(
            "param1",
            "param2"
        )

        // when the done button is pressed, finish the activity
        findViewById<Button>(R.id.doneB).setOnClickListener() {
            Log.d("DEBUG","done with gratitude journal")
            this.finish()
        }

        // when the date button is pressed, load the dialog fragment
        findViewById<Button>(R.id.dateB).setOnClickListener {
            Log.d("DEBUG", "date button pressed")
            dateFragment.show(supportFragmentManager, "fragment_workout_data")
        }
        fragment = supportFragmentManager.findFragmentById(R.id.fragment) as GratitudeFragment

        // Setting list equal to list from database
        GRATITUDELIST = dbman.readAllGratitudeRows() as MutableList<GratitudeData>

        fragment.setMyList(GRATITUDELIST)

        // set add button to set the new gratitude list upon entry completion
        findViewById<Button>(R.id.addB).setOnClickListener() {
            Log.d("DEBUG", "add button pressed")
            if(date != "") {
                setDate()
                complete = true
                // Setting list equal to list from database
                GRATITUDELIST = dbman.readAllGratitudeRows() as MutableList<GratitudeData>
                fragment.setMyList(GRATITUDELIST)
                Log.d("DEBUG", "On click over")
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        var date = findViewById<TextView>(R.id.date_text).text.toString()
        outState.putString("date", date)

        super.onSaveInstanceState(outState)
    }

    // override finish() to return the complete boolean back to the MainActivity
    override fun finish() {
        if(complete && !(intent.extras?.getBoolean("COMPLETE")!!)){
            Log.d("DEBUG", "completed the daily goal: gratitude")
            var mainIntent = Intent(this, MainActivity::class.java)
            mainIntent.putExtra("COMPLETE", "gratitude")
            setResult(RESULT_OK, mainIntent);
        }
        super.finish()
    }

    override fun onBackPressed() {
        Log.d("DEBUG", "back button pressed")
        this.finish()
        super.onBackPressed()
    }

    // set the dates for the companion object list
    @RequiresApi(Build.VERSION_CODES.M)
    fun setDate() {
        var gratitude = GratitudeData()
        gratitude.date = date
        gratitude.gratitude1 = findViewById<EditText>(R.id.gratitude1).text.toString()
        gratitude.gratitude2 = findViewById<EditText>(R.id.gratitude2).text.toString()
        gratitude.gratitude3 = findViewById<EditText>(R.id.gratitude3).text.toString()

        //Insert gratitude into db
        dbman.insertGratitude(gratitude)

//        GRATITUDELIST.add(gratitude)
    }

    // set the date TextView to the user selected date and then dismiss the DialogFragment
    fun datePicker(date: String) {
        this.date = date
        findViewById<TextView>(R.id.date_text).text = date
        dateFragment.dismiss()
    }

    // companion object used for the recyclerView
    companion object {
        var GRATITUDELIST: MutableList<GratitudeData> = mutableListOf()
    }

}
