package com.example.happy

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

class MoodListRecyclerViewAdapter(private val dataSet: MutableList<MoodEntry>, private val moodActivity: FragmentActivity) :
    RecyclerView.Adapter<MoodListRecyclerViewAdapter.ViewHolder>() {



    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.mood_list_layout, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = dataSet[position]
        var mood = item.mood
        var mood_intensity = item.intensity
        var mood_notes = item.mood_notes
        holder.idView.text = mood_intensity.toString()
        holder.contentView.text = mood
        holder.thirdView.text = mood_notes

        holder.itemView.setOnClickListener(){
            val mapIntent = Intent(moodActivity, MapsActivity::class.java)
            with(mapIntent){
                putExtra("LAT",item.mood_lat)
                putExtra("LONG",item.mood_long)
            }
            moodActivity.startActivity(mapIntent)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int = dataSet.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.mood_item_number)
        val contentView: TextView = view.findViewById(R.id.mood_item_name)
        val thirdView: TextView = view.findViewById(R.id.mood_item_notes)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }


}