package com.example.familytodolist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivity : AppCompatActivity() {

    private lateinit var familyName: String

    private lateinit var editTextTitle: EditText
    private lateinit var editTextDueDate: EditText
    private lateinit var editTextFamilyName: EditText

    private lateinit var firestoreDB: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        familyName = intent.getStringExtra("familyName") ?: ""

        firestoreDB = FirebaseFirestore.getInstance()

        editTextTitle = findViewById(R.id.editTextText7)
        editTextDueDate = findViewById(R.id.editTextText8)
        editTextFamilyName = findViewById(R.id.editTextText6)

        val buttonSubmit: Button = findViewById(R.id.button7)
        buttonSubmit.setOnClickListener {
            val title = editTextTitle.text.toString()
            val dueDate = editTextDueDate.text.toString()
            val name = editTextFamilyName.text.toString()

            if (title.isEmpty() || dueDate.isEmpty() || name.isEmpty()) {
                // Eğer bir EditText boşsa, uyarı ver
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (name == "family") {
                // "family" ise önceki işlemi koru
                addTaskToDatabase(title, dueDate, familyName)
            } else {
                // "family" değilse, databasedeki users tablosundan "familyName" isimli veri familyName'e eşit olan username değerlerini ara
                firestoreDB.collection("users")
                    .whereEqualTo("familyName", familyName)
                    .whereEqualTo("username", name)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (querySnapshot.isEmpty) {
                            // Eğer eşleşen bir kullanıcı yoksa, hata mesajı göster
                            Toast.makeText(this, "Invalid familyName", Toast.LENGTH_SHORT).show()
                        } else {
                            // Eğer eşleşen kullanıcı varsa, ToDoList'e veriyi ekle
                            for (document in querySnapshot.documents) {
                                val username = document.getString("username") ?: ""
                                val taskMap = hashMapOf(
                                    "completed" to "No",
                                    "dueDate" to dueDate,
                                    "title" to title,
                                    "username" to username
                                )

                                firestoreDB.collection("ToDoList")
                                    .add(taskMap)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Task added.", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Error occurred while adding task", Toast.LENGTH_SHORT).show()
                                        e.printStackTrace()
                                    }
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error occurred while retrieving data", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
            }
        }
    }

    private fun addTaskToDatabase(title: String, dueDate: String, familyName: String) {
        // Firestore veritabanına veri ekle
        val taskMap2 = hashMapOf(
            "completed" to "No",
            "dueDate" to dueDate,
            "familyName" to familyName,
            "title" to title
        )

        firestoreDB.collection("FamilyToDoList")
            .add(taskMap2)
            .addOnSuccessListener {
                Toast.makeText(this, "Task added.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error occurred while adding task", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
    }
}
