package com.example.simplechat

interface AuthService {
    fun signInWithEmailAndPassword(email: String, password: String, callback: (Boolean) -> Unit)
}
