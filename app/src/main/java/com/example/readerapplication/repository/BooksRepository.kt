package com.example.readerapplication.repository

import com.example.readerapplication.data.Resource
import com.example.readerapplication.model.BookList
import com.example.readerapplication.model.Item
import javax.inject.Singleton

@Singleton
interface BooksRepository {
    suspend fun getAllBooks(query: String): Resource<BookList>
    suspend fun getBookInfo(bookId: String): Resource<Item>
}