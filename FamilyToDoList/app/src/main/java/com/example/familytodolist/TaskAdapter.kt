package com.example.familytodolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.example.familytodolist.model.Task

class TaskAdapter(private val taskList: MutableList<Task>) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val db = FirebaseFirestore.getInstance()
        private val title: TextView = itemView.findViewById(R.id.titleTextView)
        private val dueDate: TextView = itemView.findViewById(R.id.dueDateTextView)
        private val completed: Button = itemView.findViewById(R.id.completedButton)

        fun bind(task: Task) {
            title.text = task.title
            dueDate.text = task.dueDate

            completed.setOnClickListener {
                val title = title.text.toString()

                val collectionReference = db.collection("ToDoList")

                collectionReference
                    .whereEqualTo("title", title)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val docId = document.id
                            // Dokümanı sil
                            db.collection("ToDoList")
                                .document(docId)
                                .delete()
                                .addOnSuccessListener {
                                    // Silme işlemi başarılı olduğunda ilgili task'ı Task listesinden de kaldır
                                    taskList.removeAll { it.title == title }
                                    notifyDataSetChanged()
                                }
                                .addOnFailureListener { exception ->
                                    // Silme işlemi başarısız olduğunda uygun şekilde işlem yapın
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Firestore'dan veri alma hatası olduğunda uygun şekilde işlem yapın
                    }
            }
        }
    }
}
