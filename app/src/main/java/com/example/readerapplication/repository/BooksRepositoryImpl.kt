package com.example.readerapplication.repository

import android.util.Log
import com.example.readerapplication.data.Resource
import com.example.readerapplication.model.BookList
import com.example.readerapplication.model.Item
import com.example.readerapplication.network.BookApi
import javax.inject.Inject


class BooksRepositoryImpl @Inject constructor(
    private val api: BookApi
) : BooksRepository {
    override suspend fun getAllBooks(query: String): Resource<BookList> {
        return try {
            Resource.Loading(data = true)
            val itemList = api.getAllBooks(query)
            if (itemList.items.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = itemList)
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }

    override suspend fun getBookInfo(bookId: String): Resource<Item> {
        Log.d("BookId", bookId)
        val response = try {
            Resource.Loading(data = true)
            api.getBookInfo(bookId)

        } catch (exception: Exception) {
            return Resource.Error(message = exception.message.toString())
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }
}