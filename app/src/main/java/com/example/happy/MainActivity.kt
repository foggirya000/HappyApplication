package com.example.happy



import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.moodfit.placeholder.singletonDB
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    /* The jArray stores an array of inspirational quotes from an API
        quote is the saved quote displayed in the top TextView
        the three complete booleans represent the user's completion of the daily tasks
        dailyGoals is the textView counter that represents how many tasks have been completed
     */
    var jArray = JSONArray()
    var quote: String? = ""
    var yoga_complete = false
    var gratitude_complete = false
    var mood_complete = false
    lateinit var dailyGoals: TextView
    lateinit var dbman: singletonDB.happyDB



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // load the custom toolbar
        setSupportActionBar(findViewById(R.id.toolbar))

        // necessary for youtube launcher
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // initialze the singleton database
        singletonDB.intializeDB(this)
        dbman = singletonDB.DBinstance!!

        var quoteB = findViewById<Button>(R.id.quoteB)
        dailyGoals = findViewById(R.id.happy_rating)


        var happy_rating = dbman.readRating()
        if(happy_rating.toInt() == -1){
            dbman.insertGoals("0", "0", "0")
            happy_rating = "0"
        }
        dailyGoals.text = happy_rating
        if (dailyGoals.text.toString().toInt() == 3) {
            findViewById<ImageView>(R.id.imageView).setColorFilter(Color.parseColor("#ff6b6b"))
        }

        // load the global vars from the bundle
        savedInstanceState?.let {
            jArray = JSONArray(savedInstanceState.getString("jarray"))
            quote = savedInstanceState.getString("quote")
            var tmp = savedInstanceState.getBooleanArray("completed")
            mood_complete = tmp!![0]
            gratitude_complete = tmp!![1]
            yoga_complete = tmp!![2]


            var happy_rating = savedInstanceState.getString("happy_rating")

            runOnUiThread() {
                findViewById<TextView>(R.id.quote).text = quote
                findViewById<TextView>(R.id.happy_rating).text = happy_rating
            }
        } ?: run {
            getQuote()
        }

        // load a new quote from the api
        quoteB.setOnClickListener() {
            setQuote()
        }


        // Gratitude activity will pass in the completion boolean and get a result back
        findViewById<ImageView>(R.id.mood).setOnClickListener() {
            val intent = Intent(this, MoodList::class.java)
            with (intent) {
                putExtra("COMPLETE",mood_complete)
            }
            startActivityForResult(intent, 1)
        }

        findViewById<ImageView>(R.id.gratitude).setOnClickListener() {
            val intent = Intent(this, GratitudeJournal::class.java)
            with (intent) {
                putExtra("COMPLETE", gratitude_complete)
            }
            startActivityForResult(intent,1)
        }

        // Yoga activity will pass in the completion boolean and get a result back
        findViewById<ImageView>(R.id.yoga).setOnClickListener() {
            val intent = Intent(this, YogaList::class.java)
            with (intent) {
                putExtra("COMPLETE", yoga_complete)
            }
            startActivityForResult(intent, 1)
        }
    }

    //When the options button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.setting){
            var settingsFragment = SettingsFragment.newInstance("param1", "param2")
            settingsFragment.show(supportFragmentManager, "toolbar settings Frag")
        }
        return super.onOptionsItemSelected(item)
    }

        // function saves all global variables that are necessary for loading into a bundle
    override fun onSaveInstanceState(outState: Bundle) {
        var completeArray = booleanArrayOf(mood_complete,gratitude_complete,yoga_complete)
        var happyRating = findViewById<TextView>(R.id.happy_rating).text.toString()
        outState?.run {
            putString("happy_rating", happyRating)
            putString("quote", quote)
            putString("jarray", jArray.toString())
            putBooleanArray("completed",completeArray)
        }
        super.onSaveInstanceState(outState)
    }


    // function to call ZenQuotes API and update TextView R.id.initial_prompt with a random
    // inspirational quote
    fun getQuote() {
        val url = URL("https://zenquotes.io/api/quotes")
        doAsync {
            var text = url.readText()
            jArray = JSONArray(text)
            setQuote()
        }
    }
    // takes the array of quotes and grabs a random quote to set the TextView
    fun setQuote() {
        doAsync {
            var obj = JSONObject(jArray[Random.nextInt(0,49)].toString())
            quote = "\"${obj.get("q")}\" \n-- ${obj.get("a")}"
            runOnUiThread() {
                findViewById<TextView>(R.id.quote).text = quote
            }
        }
    }

    //Updates the goals completed count
    fun updateGoals(){
        dailyGoals.text = dbman.readRating()
    }

    // onActivityResult will take the feedback from the activities and increment the daily completion
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
               var completed = data?.getStringExtra("COMPLETE")
                when (completed) {
                    "yoga" -> {
                        yoga_complete = true
                        dbman.yogaCompleted()
                    }
                    "gratitude" -> {
                        gratitude_complete = true
                        dbman.gratitudeCompleted()
                    }
                    "mood" -> {
                        mood_complete = true
                        dbman.moodCompleted()
                    }
                }
                updateGoals()
                if (dailyGoals.text.toString().toInt() == 3) {
                    findViewById<ImageView>(R.id.imageView).setColorFilter(Color.parseColor("#ff6b6b"))
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = getMenuInflater()
        inflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}
