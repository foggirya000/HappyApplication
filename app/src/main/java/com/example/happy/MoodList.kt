package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.moodfit.placeholder.singletonDB

class MoodEntry {
    val mood: String
    val intensity: Int
    val mood_notes: String
    var mood_lat: Double = 0.0
    var mood_long: Double = 0.0

    constructor(thisMood: String, thisIntensity: Int, thisNotes: String, mood_lat: Double, mood_long: Double) {
        mood = thisMood
        intensity = thisIntensity
        mood_notes = thisNotes
        this.mood_lat = mood_lat
        this.mood_long =mood_long
    }
}

class MoodList : AppCompatActivity() , NewMoodFragment.NewMoodFragmentListener {

    lateinit var dbman: singletonDB.happyDB
    lateinit var moodFragment: NewMoodFragment
    lateinit var moodListFragment: MoodDataListFragment
    var lat: Double = 0.0
    var lng: Double = 0.0
    var complete = false

    companion object obj {
        var moodEntryList: MutableList<MoodEntry> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_list)
        dbman = singletonDB.DBinstance!!


        moodEntryList = dbman.readAllMoodRows() as MutableList<MoodEntry>

        moodListFragment = supportFragmentManager.findFragmentById(R.id.moodfragment) as MoodDataListFragment
        moodListFragment.setMyList(moodEntryList)


        findViewById<ImageView>(R.id.add_image).setOnClickListener() {
            moodFragment = NewMoodFragment.newInstance("s1", "s2")
            moodFragment.show(supportFragmentManager, "show fragment")
            moodFragment.listener = this
        }
    }

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }

    // override finish() to return the complete boolean back to the MainActivity
    override fun finish() {
        if(complete && !(intent.extras?.getBoolean("COMPLETE")!!)){
            var mainIntent = Intent(this, MainActivity::class.java)
            mainIntent.putExtra("COMPLETE", "mood")
            setResult(RESULT_OK, mainIntent);
        }
        super.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            lat = data!!.getDoubleExtra("LAT", 0.0)
            lng = data!!.getDoubleExtra("LNG", 0.0)
        }
    }

    override fun passNewMoodEntry(data: MoodEntry) {
        data.mood_lat = lat
        data.mood_long = lng
        moodEntryList.add(data)
        moodFragment.dismiss()
        moodListFragment.setMyList(moodEntryList)
        dbman.insertMood(data)
        complete = true
    }
}