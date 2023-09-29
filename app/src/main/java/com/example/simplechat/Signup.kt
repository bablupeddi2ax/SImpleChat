package com.example.simplechat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Features:
 *     User Registration with Firebase Authentication:
 *     Uses Firebase's createUserWithEmailAndPassword method for user registration.
 *
 *     Input Validation:
 *     Checks if the name, email, and password are not empty.
 *     Validates email  and password using  regular expressions.
 *
 *     Database Interaction:
 *     Adds the user's name, email, and UID to Firebase Realtime Database under the "users" node upon successful registration.
 *
 *     Navigation:
 *     On successful registration, the user is redirected to the MainActivity.
 *
 *     Code Organization:
 *     Input validation functions (validateEmail and validatePassword) are defined separately.
 *     A sealed class ValidationResult is used to represent validation results.
 **/
class Signup : AppCompatActivity() {
    // Define variables
    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //hide action supportActionBar
        supportActionBar?.hide()

        // initialize views
        edtName = findViewById(R.id.edtName)
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnSignup = findViewById(R.id.btn_signup)

        //initialize auth and database reference
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        // set on click listener for signup button
        btnSignup.setOnClickListener{
            // remove extra spaces to avoid problems that might occur when user enter his details further
            // trim removes extra spaces at front and back of string
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString()
            val name = edtName.text.toString().trim()
            val emailValidation = validateEmail(email)
            val passwordValidation = validatePassword(password)

            // validate inputs before calling signup
            if (validateInputs(name, emailValidation, passwordValidation)) {
                // call signup method if inputs are valid
                signup(name,email, password)
            }
        }

    }

    // Handle user registration
    private fun signup(name:String,email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    // add user to db if user is created successfully
                    addUserToDb(name,email,mAuth.currentUser?.uid!!)

                    // navigate to MainActivity
                    moveTo(this@Signup,MainActivity::class.java)

                    //finish the activity so that user accidentally will not be able to redirected to signup again
                    finish()

                } else {
                    // If sign in fails, log an error message
                    Log.i("signup", "signup_failure$email$password")
                    Toast.makeText(this@Signup, "some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Adds user data to the Firebase Realtime Database
    private fun addUserToDb(name: String, email: String, uid: String) {
        mDbRef.child("users").child(uid).setValue(User(name,email,uid))
    }


    // Validates user inputs
    private fun validateInputs(name: String, emailValidation: ValidationResult, passwordValidation: ValidationResult): Boolean {
        // Validate Name
        if (name.isEmpty()) {
            showToast("Please enter your name.",this@Signup)
            return false
        }

        // Validate Email
        when (emailValidation) {
            is ValidationResult.Success -> { }
            is ValidationResult.Error -> {
                showToast(emailValidation.message,this@Signup)
                return false
            }
        }

        // Validate Password
        when (passwordValidation) {
            is ValidationResult.Success -> { }
            is ValidationResult.Error -> {
                showToast(passwordValidation.message,this@Signup)
                return false
            }
        }

        return true
    }
}
