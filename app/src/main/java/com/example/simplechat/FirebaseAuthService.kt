package com.example.simplechat

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthService :AuthService{
    private val mAuth = FirebaseAuth.getInstance()
    override fun signInWithEmailAndPassword(
        email: String,
        password: String,
        callback: (Boolean) -> Unit
    ) {
        mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{task->
                callback(task.isSuccessful)
            }
    }
}