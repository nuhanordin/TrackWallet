package com.example.trackwallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var textUsername: TextView
    lateinit var textEmail: TextView
    lateinit var buttonLogout: Button

    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textUsername = findViewById(R.id.username)
        textEmail = findViewById(R.id.email)
        buttonLogout = findViewById(R.id.buttonLogout)

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            textUsername.text = firebaseUser.displayName
            textEmail.text = firebaseUser.email
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        buttonLogout.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }


    }
}