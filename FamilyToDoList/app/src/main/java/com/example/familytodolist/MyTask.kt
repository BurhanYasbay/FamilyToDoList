package com.example.familytodolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.example.familytodolist.model.Task

class MyTask : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private var taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_task)

        // RecyclerView'ı bul ve ayarla
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Adapter'i oluştur ve RecyclerView'a bağla
        adapter = TaskAdapter(taskList)
        recyclerView.adapter = adapter

        // Task verilerini al ve RecyclerView'a ekle
        getTaskData()
    }

    private fun getTaskData() {
        val db = FirebaseFirestore.getInstance()
        val username = UserLoginActivity.username

        db.collection("ToDoList")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { documents ->
                taskList.clear()
                for (document in documents) {
                    val title = document.getString("title")
                    val dueDate = document.getString("dueDate")
                    if (title != null && username != null && dueDate != null) {
                        val task = Task(username,title, dueDate)
                        taskList.add(task)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Firestore'dan veri alma hatası olduğunda uygun şekilde işlem yapın
            }
    }

}



