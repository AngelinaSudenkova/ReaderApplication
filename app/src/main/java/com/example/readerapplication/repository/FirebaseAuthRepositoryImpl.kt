package com.example.readerapplication.repository

import com.example.readerapplication.model.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {


    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): AuthResult? {
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): AuthResult? {
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        if (authResult.user != null) {
            createUser(authResult.user!!.email)
        }
        return authResult
    }

    override suspend fun logout() {
        auth.signOut()
    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid

        val user = User(
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = "Life is great",
            id = null
        ).toMap()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)

    }
}