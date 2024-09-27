package com.example.readerapplication.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun loginWithEmailAndPassword(email: String, password: String): AuthResult?
    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult?
    suspend fun logout()
}