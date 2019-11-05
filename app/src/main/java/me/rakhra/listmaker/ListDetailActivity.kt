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
    lateinit var addTaskButton: FloatingActionButton
    private var listDetailsFragment: ListDetailsFragment = ListDetailsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_detail)
        setSupportActionBar(toolbar)
        addTaskButton = findViewById(R.id.add_task)
        addTaskButton.setOnClickListener {
            showCreateTaskDialog()
        }

        listDetailsFragment = supportFragmentManager.findFragmentById(R.id.list_details_fragment) as ListDetailsFragment
    }

    private fun showCreateTaskDialog() {
        listDetailsFragment.let {
            val taskEditText = EditText(this)
            taskEditText.inputType = InputType.TYPE_CLASS_TEXT
            AlertDialog.Builder(this).setTitle(getString(R.string.task_to_add))
                .setView(taskEditText)
                .setPositiveButton(getString(R.string.add_task)) { dialog, which ->
                    val task = taskEditText.text.toString()
                    it.addTask(task)
                    dialog.dismiss()
                }.create().show()
        }

    }

    override fun onBackPressed() {
        val bundle = Bundle()
        bundle.putParcelable(MainActivity.INTENT_LIST_KEY, listDetailsFragment.list)
        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }

}
