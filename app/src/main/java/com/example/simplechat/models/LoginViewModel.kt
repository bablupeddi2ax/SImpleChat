package com.example.simplechat.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simplechat.FirebaseAuthService
import com.example.simplechat.ValidationResult

class LoginViewModel : ViewModel() {
    private val authService = FirebaseAuthService()
    val loginResult = MutableLiveData<ValidationResult>()
    // In LoginViewModel
//    val navigateToMain = MutableLiveData<Event<Unit>>()
//
//    fun loginSuccess() {
//        navigateToMain.value = Event(Unit)
//    }

    fun login(email: String, password: String) {
        if (validate(email, password)) {
            authService.signInWithEmailAndPassword(email, password) { success ->
                if (success) {
                    // Successful login
                    loginResult.postValue(ValidationResult.Success("Login successful"))
                } else {
                    // Failed login
                    loginResult.postValue(ValidationResult.Error("Invalid credentials"))
                }
            }
        } else {
            // Validation failed
            loginResult.postValue(ValidationResult.Error("Invalid email or password"))
        }
    }

    private fun validate(email: String, password: String): Boolean {
        return email.isNotBlank() && password.isNotBlank()
    }
}
