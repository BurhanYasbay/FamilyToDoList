package com.example.familytodolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var familyName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        familyName = intent.getStringExtra("familyName") ?: ""
        val createFamilyButton: Button = findViewById(R.id.button)
        val loginButton: Button = findViewById(R.id.button2)

        createFamilyButton.setOnClickListener {

            val createFamilyIntent = Intent(this@MainActivity, CreateFamilyActivity::class.java)
            startActivity(createFamilyIntent)
        }

        loginButton.setOnClickListener {

            val loginIntent = Intent(this@MainActivity, LoginFamilyActivity::class.java)
            startActivity(loginIntent)
        }
    }
}
