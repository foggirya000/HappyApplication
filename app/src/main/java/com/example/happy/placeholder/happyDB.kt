package com.example.moodfit.placeholder

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.happy.GratitudeData
import com.example.happy.MoodEntry

object singletonDB {


    var DBinstance: happyDB? = null

    //Creates the DB if it hasn't already been created
    fun intializeDB(context:Context){
        if(DBinstance == null){
            DBinstance = happyDB(context)
        }
    }


    class happyDB(context: Context) : SQLiteOpenHelper(context, "MyHappyDB", null, 1) {
        private val DATABASE_NAME = "happyDB"
        private val DATABASE_VERSION = 1
        private var instance: happyDB? = null


        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL("CREATE TABLE IF NOT EXISTS RATING(rating, mood, gratitude, yoga)")
            db?.execSQL("CREATE TABLE IF NOT EXISTS ENTRIES(date, gratitude1, gratitude2, gratitude3)")
            db?.execSQL("CREATE TABLE IF NOT EXISTS MOODS(mood, intensity, notes, lat, long)")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            TODO("Not yet implemented")
        }

        fun moodCompleted() {
            writableDatabase.execSQL("UPDATE RATING SET mood = '1'")
            updateRating()
        }

        fun gratitudeCompleted() {
            writableDatabase.execSQL("UPDATE RATING SET gratitude = '1'")
            updateRating()
        }

        fun yogaCompleted() {
            writableDatabase.execSQL("UPDATE RATING SET yoga = '1'")
            updateRating()
        }

        fun updateRating() {
            val row = readCompletedGoals()
            val rating = row.get(0).toInt() + row.get(1).toInt() + row.get(2).toInt()
            writableDatabase.execSQL("UPDATE RATING SET rating = \"${rating}\"")
        }

        fun insertGoals(mood: String, gratitude: String, yoga: String) {
            // INSERT INTO DATES VALUES("TUE MAY 11...")
            var rating = (mood.toInt() + gratitude.toInt() + yoga.toInt()).toString()
            writableDatabase.execSQL("INSERT INTO RATING VALUES(\"${rating}\", \"$mood\", \"$gratitude\", \"$yoga\")")
        }

        /*fun updateAllGoals(mood: String, gratitude: String, yoga: String) {
            var rating = (mood.toInt() + gratitude.toInt() + yoga.toInt()).toString()
            Log.d("DEBUG", "rating: ${rating}")
            writableDatabase.execSQL("UPDATE RATING SET rating = '${rating}', mood = '$mood', gratitude = '$gratitude', yoga = '$yoga'")
            Log.d("DEBUG", "Row after update ${readRating()}")
        }*/

        fun resetGoals(){
            writableDatabase.execSQL("UPDATE RATING SET rating = '0', mood = '0', gratitude = '0', yoga = '0'")
        }

        /*// Return list of all rows from the database
        fun readAllGoalRows(): List<String> {
            var result = mutableListOf<String>()
            // Get a table back from the query
            var cursor = writableDatabase.rawQuery("SELECT * FROM RATING", null)
            // Iterate over all rows of the response
            while (cursor.moveToNext()) {
                result.add(cursor.getString(0))
            }
            return result
        }*/


        fun readCompletedGoals(): List<String> {
            var cursor = writableDatabase.rawQuery("SELECT mood, gratitude, yoga from RATING", null)
            cursor.moveToFirst()
            var row = listOf<String>(cursor.getString(0), cursor.getString(1), cursor.getString(2))
            return row
        }

        fun readRating(): String {
            var cursor = writableDatabase.rawQuery("SELECT rating from RATING", null)
            cursor.moveToFirst()
            if (cursor.getCount() <= 0) {
                return "-1"
            } else {
                return cursor.getString(0)
            }
        }

        fun insertGratitude(gratitudes: GratitudeData) {
            // INSERT INTO DATES VALUES("TUE MAY 11...")
            writableDatabase.execSQL("INSERT INTO ENTRIES VALUES(\"${gratitudes.date}\", \"${gratitudes.gratitude1}\", \"${gratitudes.gratitude2}\", \"${gratitudes.gratitude3}\")")
        }

        fun insertMood(mood: MoodEntry) {
            // INSERT INTO DATES VALUES("TUE MAY 11...")
            writableDatabase.execSQL("INSERT INTO MOODS VALUES(\"${mood.mood}\", \"${mood.intensity.toString()}\", \"${mood.mood_notes}\", \"${mood.mood_lat.toString()}\", \"${mood.mood_long.toString()}\")")
        }

        // Return list of all rows from the database
        fun readAllGratitudeRows(): List<GratitudeData> {
            var result = mutableListOf<GratitudeData>()
            // Get a table back from the query
            var cursor = writableDatabase.rawQuery("SELECT * FROM ENTRIES", null)
            // Iterate over all rows of the response
            while (cursor.moveToNext()) {
                var gratitude = GratitudeData()
                gratitude.date = cursor.getString(0)
                gratitude.gratitude1 = cursor.getString(1)
                gratitude.gratitude2 = cursor.getString(2)
                gratitude.gratitude3 = cursor.getString(3)
                result.add(gratitude)
            }
            return result
        }

        fun readAllMoodRows(): List<MoodEntry> {
            var result = mutableListOf<MoodEntry>()
            // Get a table back from the query
            var cursor = writableDatabase.rawQuery("SELECT * FROM MOODS", null)
            // Iterate over all rows of the response
            while (cursor.moveToNext()) {
                var tempMood = cursor.getString(0)
                var tempIntensity = cursor.getString(1).toInt()
                var tempNotes = cursor.getString(2)
                var tmpLat = cursor.getString(3).toDouble()
                var tmpLong = cursor.getString(4).toDouble()
                var thisMood = MoodEntry(tempMood, tempIntensity, tempNotes, tmpLat, tmpLong)
                result.add(thisMood)
            }
            return result
        }

        fun deleteAllGratitude(){
            writableDatabase.execSQL("DELETE FROM ENTRIES")
        }

        fun deleteAllMoods(){
            writableDatabase.execSQL("DELETE FROM MOODS")
        }

    }
}


