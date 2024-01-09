package com.example.trackwallet

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest

class RegisterActivity : AppCompatActivity() {

    lateinit var editFullName: EditText
    lateinit var editEmail: EditText
    lateinit var editUsername: EditText
    lateinit var editPassword: EditText
    lateinit var editPasswordConfirm: EditText
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
        setContentView(R.layout.activity_register)

        editFullName = findViewById(R.id.fullname)
        editEmail = findViewById(R.id.email)
        editUsername = findViewById(R.id.username)
        editPassword = findViewById(R.id.password)
        editPasswordConfirm = findViewById(R.id.passwordconfirm)
        buttonRegister = findViewById(R.id.buttonRegister)
        buttonLogin = findViewById(R.id.buttonLogin)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Signing up")
        progressDialog.setMessage("Please wait")

        buttonLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        buttonRegister.setOnClickListener {
            if (editFullName.text.isNotEmpty() &&
                editEmail.text.isNotEmpty() &&
                editUsername.text.isNotEmpty() &&
                editPassword.text.isNotEmpty()
            ) {

                if (editPassword.text.toString() == editPasswordConfirm.text.toString()) {
                    //launch register
                    processRegister()
                } else {
                    Toast.makeText(this, "Password must be same", LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in the fields", LENGTH_SHORT).show()
            }
        }
    }

    private fun processRegister() {
        val fullName = editFullName.text.toString()
        var email = editEmail.text.toString()
        var username = editUsername.text.toString()
        var password = editPassword.text.toString()

        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                //check if success or fail
                if (task.isSuccessful) {
                    val userUpdateProfile = userProfileChangeRequest {
                        displayName = fullName
                        displayName = username
                    }
                    val user = task.result.user
                    user!!.updateProfile(userUpdateProfile)
                        .addOnCompleteListener {
                            progressDialog.dismiss()
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        .addOnFailureListener { error2 ->
                            Toast.makeText(this, error2.localizedMessage, LENGTH_SHORT).show()
                        }
                } else {
                    progressDialog.dismiss()
                }
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, error.localizedMessage, LENGTH_SHORT).show()
            }
    }
}