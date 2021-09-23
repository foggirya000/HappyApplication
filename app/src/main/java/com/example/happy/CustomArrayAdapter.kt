package com.example.happy

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.youtube.player.YouTubeStandalonePlayer
import org.json.JSONObject
import java.net.URL

class CustomArrayAdapter(context: Context,
                         private val layoutResource: Int,
                         private val values: MutableList<YogaList.YogaData>,
                         private val fragActivity: FragmentActivity) : ArrayAdapter<YogaList.YogaData>(context, layoutResource, values) {

    // getView takes in a context and sets the views of the list items
    override fun getView (position: Int, convertView: View?, parent: ViewGroup) : View {

        val context = parent.context
        val view = convertView ?: LayoutInflater.from(context).inflate(layoutResource, parent, false)

        var item = values[position]

        // get the TextViews
        var pose = view.findViewById<TextView>(R.id.pose_name)
        var img = view.findViewById<ImageView>(R.id.pose_img)

        // set the images to their respective assets
        var imgcontext = img.context
        var id = imgcontext.resources.getIdentifier(item.image,"drawable",imgcontext.packageName)
        img.setImageResource(id)

        // get the name for the youtube video
        pose.text = item.poseName
        var video = pose.text.split(' ','\n')[1]

        // on item click, retrieve a "how to" video on the yoga pose and start the youtube player
        // call for yoga task completion
        view.setOnClickListener() {
            Log.d("DEBUG","${video} pose was clicked")
            var text = URL("https://www.googleapis.com/youtube/v3/search?part=snippet&q=how+to+do+${video}+yoga+pose&maxResults=1&&safeSearch=moderate&key=AIzaSyBcY7xBC8qSNeUQv39-csOZ6sWqitZ1BJw").readText()
            var youtube = JSONObject(text).getJSONArray("items").getJSONObject(0).getJSONObject("id").getString("videoId")
            var youtubeIntent = YouTubeStandalonePlayer.createVideoIntent(fragActivity, "AIzaSyDbZ4xNI7Dr9kvI0jtZ_HyjC_7A8_xG2dc", youtube)
            fragActivity.startActivity(youtubeIntent)
            (fragActivity as? YogaList)?.yogaComplete()
        }

        return view
    }
}