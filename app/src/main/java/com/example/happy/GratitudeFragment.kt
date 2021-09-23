package com.example.happy

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moodfit.placeholder.PlaceholderContent

/**
 * A fragment representing a list of Items.
 */
class GratitudeFragment : Fragment() {

    /*
        This fragment takes a list from setMyList that contains the user input for each entry
        the list is then fed into the adapter after it has been set
     */
    private var columnCount = 1
    var list: MutableList<GratitudeData> = emptyList<GratitudeData>().toMutableList()
    lateinit var gratitudeAdapter: MyItemRecyclerViewAdapter

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
        val view = inflater.inflate(R.layout.fragment_gratitude_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = activity?.let{MyItemRecyclerViewAdapter(list, it)}
                gratitudeAdapter = adapter as MyItemRecyclerViewAdapter
            }
        }

        return view
    }

    // method to refresh the list on entry input (data set change)
    fun setMyList(list: MutableList<GratitudeData>) {
        this.list.clear()
        this.list.addAll(list)
        this.list.reverse()
        gratitudeAdapter.notifyDataSetChanged()

    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            GratitudeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}