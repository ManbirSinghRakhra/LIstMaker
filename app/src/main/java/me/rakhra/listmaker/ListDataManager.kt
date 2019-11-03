package me.rakhra.listmaker

import android.content.Context

class ListDataManager(val context: Context) {

    fun saveList(list: TaskList) {
        val sharedPreferences = context.getSharedPreferences("ListMakerPreferences", Context.MODE_PRIVATE).edit()
        sharedPreferences.putStringSet(list.name, list.task.toHashSet())
        sharedPreferences.apply()
    }

    fun readLists(): ArrayList<TaskList> {
        val sharedPreferences = context.getSharedPreferences("ListMakerPreferences", Context.MODE_PRIVATE).all
        val taskLists = ArrayList<TaskList>()

        for (preferenceItem in sharedPreferences) {
            val itemsHashSet = preferenceItem.value as HashSet<String>
            val list = TaskList(preferenceItem.key, ArrayList(itemsHashSet))
            taskLists.add(list)
        }

        return taskLists
    }
}