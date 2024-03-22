package com.example.familytodolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familytodolist.model.Task
import com.google.firebase.firestore.FirebaseFirestore

class FamilyTaskActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private var taskList = mutableListOf<Task>()
    private lateinit var familyName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.family_task_activity)

        familyName = intent.getStringExtra("familyName") ?: ""

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

        if (familyName != null) {
            val collectionReference = db.collection("FamilyToDoList")
                .whereEqualTo("familyName", familyName)

            collectionReference.get()
                .addOnSuccessListener { documents ->
                    taskList.clear()
                    for (document in documents) {
                        val title = document.getString("title")
                        val username = document.getString("familyName")
                        val dueDate = document.getString("dueDate")

                        if (title != null && username != null && dueDate != null) {
                            val task = Task(username, title, dueDate)
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
}
