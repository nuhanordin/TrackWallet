package com.example.trackwallet

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var editEmail: EditText
    lateinit var editPassword: EditText
    lateinit var buttonRegister: Button
    lateinit var buttonLogin: Button
    lateinit var progressDialog: ProgressDialog

    var firebaseAuth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editEmail = findViewById(R.id.email)
        editPassword = findViewById(R.id.password)
        buttonRegister = findViewById(R.id.buttonRegister)
        buttonLogin = findViewById(R.id.buttonLogin)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Logging in")
        progressDialog.setMessage("Please wait")

        buttonLogin.setOnClickListener {
            if (editEmail.text.isNotEmpty() &&
                editPassword.text.isNotEmpty()
            ) {
                processLogin()
            } else {
                Toast.makeText(this, "Please fill in the fields", LENGTH_SHORT).show()
            }
        }
    }

    private fun processLogin() {
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()

        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                startActivity(Intent(this, MainActivity::class.java))
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, error.localizedMessage, LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                progressDialog.dismiss()
            }

    }
}