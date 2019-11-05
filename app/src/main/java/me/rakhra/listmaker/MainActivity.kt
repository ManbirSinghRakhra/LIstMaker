package me.rakhra.listmaker

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ListSelectionFragment.OnFragmentInteractionListener {
    private var listSelectionFragment: ListSelectionFragment = ListSelectionFragment()
    private var fragmentContainer: FrameLayout? = null
    private var largeScreen = false
    private var listFragment: ListDetailsFragment? = null

    override fun onListItemClicked(list: TaskList) {
        showListDetail(list)
    }

    companion object {
        val INTENT_LIST_KEY = "list"
        val LIST_DETAIL_REQUEST_CODE = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        add_task.setOnClickListener { view ->
           showCreateListDialog()
        }

        listSelectionFragment = supportFragmentManager.findFragmentById(R.id.list_selection_fragment) as ListSelectionFragment
        fragmentContainer = findViewById(R.id.fragment_container)
        largeScreen = fragmentContainer != null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showCreateListDialog() {
        val dialogTitle = getString(R.string.name_of_list)
        val positiveButtonTitle = getString(R.string.create_list)

        val builder = AlertDialog.Builder(this)
        val listTitleEditText = EditText(this)
        listTitleEditText.inputType = InputType.TYPE_CLASS_TEXT
        builder.setTitle(dialogTitle)
        builder.setView(listTitleEditText)

        builder.setPositiveButton(positiveButtonTitle) { dialog, which ->
            val list = TaskList(listTitleEditText.text.toString())
            listSelectionFragment.addList(list)
            dialog.dismiss()

            showListDetail(list)
        }

        builder.create().show()
    }

    private fun showListDetail(list: TaskList) {

        if(!largeScreen) {
            val listDetailIntent = Intent(this, ListDetailActivity::class.java)
            listDetailIntent.putExtra(INTENT_LIST_KEY, list)
            startActivityForResult(listDetailIntent, LIST_DETAIL_REQUEST_CODE)
        } else {
            title = list.name
            listFragment = ListDetailsFragment.newInstance(list)

            listFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, it, getString(R.string.list_fragment_tag))
                    .addToBackStack(null)
                    .commit()
            }

            add_task.setOnClickListener { view ->
                showCreateTaskDialog()
            }

        }


    }

    private fun showCreateTaskDialog() {
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT
        listFragment?.let {
            AlertDialog.Builder(this)
                .setTitle(R.string.task_to_add)
                .setView(taskEditText)
                .setPositiveButton(R.string.add_task) { dialog, which ->
                    val task = taskEditText.text.toString()
                    it.addTask(task)
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        title = resources.getString(R.string.app_name)
        listFragment?.list?.let {
            listSelectionFragment.listDataManager.saveList(it)
        }
        listFragment?.apply {
            supportFragmentManager
                .beginTransaction()
                .remove(this)
                .commit()
            listFragment = null
        }

        add_task.setOnClickListener { view ->
            showCreateListDialog()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == LIST_DETAIL_REQUEST_CODE) {
            data?.let {
                listSelectionFragment.saveList(data.getParcelableExtra(INTENT_LIST_KEY))
            }
        }
    }
}
