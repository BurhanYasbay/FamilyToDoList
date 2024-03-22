package com.example.familytodolist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class UserRegisterActivity : AppCompatActivity() {

    private lateinit var firestoreDB: FirebaseFirestore
    private lateinit var familyName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_register_activity)

        firestoreDB = FirebaseFirestore.getInstance()

        // MainActivity'den aile adını al
        familyName = intent.getStringExtra("familyName") ?: ""

        val registerButton = findViewById<Button>(R.id.button6)
        val usernameEditText = findViewById<EditText>(R.id.editTextText3)
        val emailEditText = findViewById<EditText>(R.id.editTextText5)
        val passwordEditText = findViewById<EditText>(R.id.editTextTextPassword4)
        val adminRadioButton = findViewById<RadioButton>(R.id.radioButton)
        val memberRadioButton = findViewById<RadioButton>(R.id.radioButton2)

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val userType = if (adminRadioButton.isChecked) "admin" else "member"

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            } else {
                // Firestore sorguları yaparak kullanıcıyı kaydetme işlemini gerçekleştirin
                firestoreDB.collection("users")
                    .whereEqualTo("username", username)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            // Kullanıcı adı benzersiz, kullanıcıyı kaydedin
                            val newUser = hashMapOf(
                                "username" to username,
                                "email" to email,
                                "password" to password,
                                "userType" to userType,
                                "familyName" to familyName // Aile adını kaydet
                            )

                            firestoreDB.collection("users")
                                .add(newUser)
                                .addOnSuccessListener { documentReference ->
                                    // Kayıt başarılı, UserLoginActivity sayfasına yönlendirin
                                    val intent = Intent(this, UserLoginActivity::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Kayıt sırasında bir hata oluştu: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            // Kullanıcı adı mevcut, kullanıcıya uyarı verin
                            Toast.makeText(this, "Bu kullanıcı adı zaten alınmış", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Sorgu sırasında bir hata oluştu: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    fun onLinkClick(view: View) {
        val intent = Intent(this, UserLoginActivity::class.java)
        startActivity(intent)
    }
}
