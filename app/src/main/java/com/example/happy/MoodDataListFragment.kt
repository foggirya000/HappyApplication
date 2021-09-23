package com.example.happy

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * A fragment representing a list of Items.
 */
class MoodDataListFragment : Fragment() {

    /*
        This fragment takes a list from setMyList that contains the user input for each entry
        the list is then fed into the adapter after it has been set
     */
    private var columnCount = 1
    var list: MutableList<MoodEntry> = emptyList<MoodEntry>().toMutableList()
    lateinit var moodAdapter: MoodListRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mood_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = activity?.let{MoodListRecyclerViewAdapter(list, it)}
                moodAdapter = adapter as MoodListRecyclerViewAdapter
            }
        }

        return view
    }

    // method to refresh the list on entry input (data set change)
    fun setMyList(list: MutableList<MoodEntry>) {
        this.list.clear()
        this.list.addAll(list)
        this.list.reverse()
        moodAdapter.notifyDataSetChanged()

    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            MoodDataListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}