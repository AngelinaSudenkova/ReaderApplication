package com.example.readerapplication.repository

import com.example.readerapplication.data.DataOrException
import com.example.readerapplication.model.MBook

interface BookFireRepository {
    suspend fun getAllBooksFromDatabase(): DataOrException<List<MBook>,Boolean, Exception>
    suspend fun getBookFromDatabase(bookId: String): DataOrException<MBook,Boolean, Exception>

}