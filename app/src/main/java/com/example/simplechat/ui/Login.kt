package com.example.simplechat.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.simplechat.models.LoginViewModel
import com.example.simplechat.R
import com.example.simplechat.Utils
import com.example.simplechat.ValidationResult

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
    private lateinit var utils: Utils
    private lateinit var viewModel: LoginViewModel

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
        utils = Utils()
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is ValidationResult.Success -> {
                    // Handle a successful login (e.g., navigate to the next screen)
                    utils.moveTo(this@Login,MainActivity::class.java)
                    finish()
                }
                is ValidationResult.Error -> {
                    // Handle a failed login (e.g., display an error message)
                    utils.showToast("user does not exists!",this@Login)
                }
            }
        }
        // navigate to Signup Screen (user does not have an account)
        btnSignup.setOnClickListener {
            utils.moveTo(this@Login, Signup::class.java)
        }
        // validate and login with provided credentials
        btnLogin.setOnClickListener {
            // get email and password
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            // check if they are valid
            viewModel.login(email,password)
        }
    }


}

