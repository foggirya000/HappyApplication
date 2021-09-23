package com.example.happy

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity


import com.example.happy.databinding.FragmentGratitudeBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyItemRecyclerViewAdapter(
    private val values: MutableList<GratitudeData>, private val fragActivity: FragmentActivity
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentGratitudeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        var grat1 = item.gratitude1
        var grat2 = item.gratitude2
        var grat3 = item.gratitude3
        holder.idView.text = item.date
        if(grat1 != "") grat1 = "Gratitude: $grat1 \n"
        if(grat2 != "") grat2 = "Gratitude: $grat2 \n"
        if(grat3 != "") grat3 = "Gratitude: $grat3 \n"
        holder.contentView.text = grat1 + grat2 + grat3
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentGratitudeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}