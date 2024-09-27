package com.example.readerapplication.model

data class BookList(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)