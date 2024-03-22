package com.example.familytodolist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class CreateFamilyActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_family)

        val familyNameEditText: EditText = findViewById(R.id.editTextText)
        val familyPasswordEditText: EditText = findViewById(R.id.editTextTextPassword)
        val submitButton: Button = findViewById(R.id.button3)

        submitButton.setOnClickListener {
            val familyName = familyNameEditText.text.toString().trim()
            val familyPassword = familyPasswordEditText.text.toString().trim()

            if (familyName.isEmpty() || familyPassword.isEmpty()) {
                showToast("Please fill in all fields.")
            } else {
                checkFamilyNameExistence(familyName, familyPassword)
            }
        }
    }

    private fun checkFamilyNameExistence(familyName: String, familyPassword: String) {
        db.collection("families")
            .document(familyName)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    showToast("Family name already exists.")
                } else {
                    saveFamilyToFirestore(familyName, familyPassword)
                }
            }
            .addOnFailureListener { exception ->
                showToast("Error checking family name existence: ${exception.message}")
            }
    }

    private fun saveFamilyToFirestore(familyName: String, familyPassword: String) {
        val family = hashMapOf(
            "familyName" to familyName,
            "familyPassword" to familyPassword
        )

        db.collection("families")
            .document(familyName)
            .set(family)
            .addOnSuccessListener {
                showToast("Family registration successful.")
                val loginIntent = Intent(this@CreateFamilyActivity, LoginFamilyActivity::class.java)
                startActivity(loginIntent)
            }
            .addOnFailureListener { exception ->
                showToast("Error registering family: ${exception.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}



