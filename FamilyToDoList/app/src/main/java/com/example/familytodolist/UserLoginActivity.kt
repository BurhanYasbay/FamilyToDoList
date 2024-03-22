package com.example.familytodolist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class UserLoginActivity : AppCompatActivity() {

    private lateinit var firestoreDB: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_login_activity)

        firestoreDB = FirebaseFirestore.getInstance()

        val loginButton = findViewById<Button>(R.id.button5)
        val emailEditText = findViewById<EditText>(R.id.editTextText2)
        val passwordEditText = findViewById<EditText>(R.id.editTextTextPassword3)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            } else {
                // Veritabanından kullanıcıyı sorgula
                firestoreDB.collection("users")
                    .whereEqualTo("email", email)
                    .whereEqualTo("password", password)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            // Giriş başarılı, FamilyTask sayfasını aç
                            val userDocument = documents.documents[0]
                            val familyName = userDocument.getString("familyName")
                            val userType = userDocument.getString("userType")

                            // Kullanıcı adını companion object'e kaydet
                            val username = userDocument.getString("username")
                            UserData.username = username

                            // familyName'i sonraki sayfalara aktar
                            val intent = Intent(this, UserListActivity::class.java)
                            intent.putExtra("familyName", familyName)
                            intent.putExtra("userType", userType)
                            startActivity(intent)
                        } else {
                            // Kullanıcı adı veya şifre yanlış
                            Toast.makeText(this, "E-posta veya şifre yanlış", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Hata durumunda kullanıcıya bilgi ver
                        Toast.makeText(this, "Hata: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    companion object UserData {
        var username: String? = null
            var familyName: String? = null

    }

}


