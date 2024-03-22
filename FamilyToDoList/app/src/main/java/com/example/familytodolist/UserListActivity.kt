package com.example.familytodolist

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class UserListActivity : AppCompatActivity() {

    private lateinit var firestoreDB: FirebaseFirestore
    private lateinit var userListAdapter: ArrayAdapter<String>
    private lateinit var familyName: String
    private lateinit var userType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        // FamilyName'i intent extrasından al
        familyName = intent.getStringExtra("familyName") ?: ""
        userType = intent.getStringExtra("userType") ?: ""

        firestoreDB = FirebaseFirestore.getInstance()

        val userList: ListView = findViewById(R.id.user_list_view)
        userListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        userList.adapter = userListAdapter

        loadUsers()

        if (userType == "admin") {
            val textView6: TextView = findViewById(R.id.textView6)
            textView6.visibility = View.VISIBLE
        }
        // textView9 üzerine tıklama dinleyicisi ekle

    }

    private fun loadUsers() {
        firestoreDB.collection("users")
            .whereEqualTo("familyName", familyName) // Sadece belirli bir familyName'e sahip kullanıcıları al
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val username = document.getString("username")
                    val email = document.getString("email")
                    val userType = document.getString("userType")
                    val userInfo = "$username - $email - $userType"
                    userListAdapter.add(userInfo)
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors
            }
    }
    fun onTextView9Click(view: View) {
        val intent = Intent(this, FamilyTaskActivity::class.java)
        intent.putExtra("familyName", familyName)
        startActivity(intent)
    }
    fun onTextView5Click(view: View) {
        val intent = Intent(this, MyTask::class.java)
        startActivity(intent)
    }
    fun onTextView6Click(view: View) {
        val intent = Intent(this, AdminActivity::class.java)
        intent.putExtra("familyName", familyName)
        startActivity(intent)
    }

}
