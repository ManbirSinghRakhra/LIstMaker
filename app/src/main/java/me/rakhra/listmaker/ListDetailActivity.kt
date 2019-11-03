package me.rakhra.listmaker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_list_detail.*

class ListDetailActivity : AppCompatActivity() {

    lateinit var list: TaskList
    lateinit var listItemsRecyclerView: RecyclerView
    lateinit var addTaskButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_detail)
        setSupportActionBar(toolbar)
        list = intent.getParcelableExtra(MainActivity.INTENT_LIST_KEY)
        title = list.name

        listItemsRecyclerView = findViewById(R.id.list_item_recyclerview)
        listItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        listItemsRecyclerView.adapter = ListItemsRecyclerViewAdapter(list)
        addTaskButton = findViewById(R.id.add_task)
        addTaskButton.setOnClickListener {
            showCreateTaskDialog()
        }
    }

    private fun showCreateTaskDialog() {
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT
        AlertDialog.Builder(this).setTitle(getString(R.string.task_to_add))
            .setView(taskEditText)
            .setPositiveButton(getString(R.string.add_task), {dialog, which ->
                val task = taskEditText.text.toString()
                list.task.add(task)
                val recyclerAdapter = listItemsRecyclerView.adapter as ListItemsRecyclerViewAdapter
                recyclerAdapter.notifyItemInserted(list.task.size)
                dialog.dismiss()
            }).create().show()
    }

    override fun onBackPressed() {
        val bundle = Bundle()
        bundle.putParcelable(MainActivity.INTENT_LIST_KEY, list)
        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }

}