package com.example.simplechat

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * Features
 *     User Authentication (login used for existing users) with Firebase
 *     method: signInWithEmailAndPassword from firebase docs
 *     ref: https://firebase.google.com/docs/auth/android/start?hl=en&authuser=0
 *
 *     Input Validation
 *     checks if email and password ar not empty
 *     shows toast if any of them is empty
 *     shows toast if user does not exists with provided credentials
 *
 *     Navigation
 *         on successful validation user is navigated to next screen (MainActivity)
 *         otherwise a toast is shown
* */
class Login : AppCompatActivity() {
    // define variables
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignup: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //hide action bar
        supportActionBar?.hide()

        // initialize views
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btn_login)
        btnSignup = findViewById(R.id.btn_signup)

        // initialize firebase auth
        mAuth = FirebaseAuth.getInstance()

        // navigate to Signup Screen (user does not have an account)
        btnSignup.setOnClickListener {
            moveTo(this@Login,Signup::class.java)
        }

        // validate and login with provided credentials
        btnLogin.setOnClickListener {
            // get email and password
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            // check if they are valid
            if(!validate(email,password)) {
                showToast("enter details correctly",this@Login)
            }else{
                login(email, password)
            }
        }

    }
    // login method
    private fun login(email: String, password: String) {
        // firebase sign in method
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // navigate to main activity
                    moveTo(this@Login, MainActivity::class.java)
                    // to make login screen  no longer active in the background finish()
                    finish()
                } else {
                    // show toast if task is not successful
                    showToast("user does not exists!", this@Login)
                }
            }
    }

    //method for validation return true if both email and password are not blank
    private fun validate(email: String, password: String):Boolean {
        return email.isNotBlank() && password.isNotBlank()
    }

}

