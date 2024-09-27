package com.example.readerapplication.repository

import android.util.Log
import com.example.readerapplication.data.DataOrException
import com.example.readerapplication.model.MBook
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BookFireRepositoryImpl @Inject constructor(
    val bookFireApi: Query
) : BookFireRepository {

    override suspend fun getAllBooksFromDatabase(): DataOrException<List<MBook>, Boolean, Exception> {
        val dataOrException = DataOrException<List<MBook>, Boolean, Exception>()
        try {
            dataOrException.loading = true
            dataOrException.data = bookFireApi.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(MBook::class.java)!!
            }
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false
        } catch (exception: FirebaseFirestoreException) {
            dataOrException.e = exception
        }
        return dataOrException
    }

    override suspend fun getBookFromDatabase(bookId: String): DataOrException<MBook, Boolean, Exception> {
        val dataOrException = DataOrException<MBook, Boolean, Exception>()
        try {
            dataOrException.loading = true
            val documentSnapshot = bookFireApi.whereEqualTo("id", bookId).get().await()
            dataOrException.data =
                documentSnapshot.documents.firstOrNull()?.toObject(MBook::class.java)
            dataOrException.loading = false
        } catch (exception: FirebaseFirestoreException) {
            dataOrException.e = exception
            dataOrException.loading = false
        }
        return dataOrException
    }
}