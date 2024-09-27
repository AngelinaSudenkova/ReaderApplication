package com.example.readerapplication.network

import com.example.readerapplication.model.BookList
import com.example.readerapplication.model.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BookApi {
    @GET("volumes")
    suspend fun getAllBooks(@Query("q") query: String): BookList

    @GET("volumes/{bookId}")
    suspend fun getBookInfo(@Path("bookId") bookId: String): Item

}