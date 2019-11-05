package me.rakhra.listmaker

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ListDetailsFragment : Fragment() {
    lateinit var list: TaskList
    lateinit var listItemsRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        list = activity?.intent?.getParcelableExtra<TaskList>(MainActivity.INTENT_LIST_KEY)
            ?: arguments?.getParcelable(MainActivity.INTENT_LIST_KEY) ?: TaskList("No List")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_details, container, false)
        view?.let {
            listItemsRecyclerView = it.findViewById(R.id.list_item_recyclerview)
            listItemsRecyclerView.adapter = ListItemsRecyclerViewAdapter(list)
            listItemsRecyclerView.layoutManager = LinearLayoutManager(activity)
        }
        return view
    }


    fun addTask(item: String) {
        list.task.add(item)
        val listItemsRecyclerViewAdapter =
            listItemsRecyclerView.adapter as ListItemsRecyclerViewAdapter
        listItemsRecyclerViewAdapter.list = list
        listItemsRecyclerViewAdapter.notifyDataSetChanged()
    }


    companion object {
        @JvmStatic
        fun newInstance(list: TaskList) =
            ListDetailsFragment().apply {
                val bundle = Bundle().apply {
                    this.putParcelable(MainActivity.INTENT_LIST_KEY, list)
                }
                this.arguments = bundle
            }
    }
}
