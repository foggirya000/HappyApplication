package com.example.happy

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class YogaList : AppCompatActivity() {

    // complete boolean will become true on video play
    var complete = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // find the ListView and set the custom adapter
        setContentView(R.layout.activity_yoga_list)
        var listView = findViewById<ListView>(R.id.yoga_listview)
        listView.adapter =CustomArrayAdapter(this, R.layout.yoga_poses, YOGALIST, this)
        getYoga()
    }

    // method to set the completion boolean to true
    fun yogaComplete(){
        this.complete = true
        Log.d("DEBUG","yoga completed was set to true")
    }

    // on finish call, return the completed state of the yoga activity
    override fun finish() {
        Log.d("DEBUG","complete = $complete and has not been completed = ${!(intent.extras?.getBoolean("COMPLETE")!!)}")
        if(complete && !(intent.extras?.getBoolean("COMPLETE")!!)){
            Log.d("DEBUG", "completed the daily goal: yoga")
            var mainIntent = Intent(this, MainActivity::class.java)
            mainIntent.putExtra("COMPLETE", "yoga")
            setResult(RESULT_OK, mainIntent);
        }
        super.finish()
    }

    /*  getYoga will read from a downloaded api json file that contains the names and img sources for each pose.
        The names are parsed and then concatenated into one string.
        The image names consist of the english name with no spaces and special chars
     */
    fun getYoga() {
        var jsonString = ""
        try {
            jsonString = assets.open("yoga_api.json").bufferedReader().use {it.readText()}
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            this.finish()
        }

        val array = JSONArray(jsonString)
        var pose: JSONObject

        // loop through the json array and set the image/pose name
        for(i in 0..array.length()-1) {
            var yoga = YogaData()
            pose = array.getJSONObject(i)
            val re = "[^A-Za-z0-9 ]".toRegex()
            yoga.image = pose.getString("english_name").lowercase()
            yoga.image = re.replace(yoga.image,"").replace(" ","")
            yoga.poseName = "Sanskrit: " + pose.getString("sanskrit_name") + "\nEnglish: " + pose.getString("english_name")
            YOGALIST.add(yoga)
        }
    }

    // companion object used for the adapter to display the data
    companion object {
        var YOGALIST: MutableList<YogaData> = mutableListOf()
    }

    // yoga data object class
    class YogaData {
        var poseName = ""
        var image = ""
    }
}