package com.example.readerapplication.model

data class User(
    val id: String?,
    val userId: String,
    val displayName: String,
    val avatarUrl: String,
    val quote: String,
) {

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "userId" to userId,
            "displayName" to displayName,
            "avatarUrl" to avatarUrl,
            "quote" to quote
        )
    }
}
