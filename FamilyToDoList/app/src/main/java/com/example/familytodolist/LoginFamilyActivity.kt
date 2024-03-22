package com.example.familytodolist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class LoginFamilyActivity : AppCompatActivity() {

    private lateinit var firestoreDB: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_family)

        firestoreDB = FirebaseFirestore.getInstance()

        val button4 = findViewById<Button>(R.id.button4)
        val editTextText4 = findViewById<EditText>(R.id.editTextText4)
        val editTextTextPassword2 = findViewById<EditText>(R.id.editTextTextPassword2)

        button4.setOnClickListener {
            val familyName = editTextText4.text.toString()
            val familyPassword = editTextTextPassword2.text.toString()

            loginUser(familyName, familyPassword)
        }
    }

    private fun loginUser(familyName: String, familyPassword: String) {
        firestoreDB.collection("families")
            .whereEqualTo("familyName", familyName)
            .whereEqualTo("familyPassword", familyPassword)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // Aile adını UserRegisterActivity ekranına aktar
                    val intent = Intent(this, UserRegisterActivity::class.java).apply {
                        putExtra("familyName", familyName)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Girdiğiniz bilgiler yanlış", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Hata: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
