package com.example.readerapplication.di

import com.example.readerapplication.network.BookApi
import com.example.readerapplication.repository.BookFireRepositoryImpl
import com.example.readerapplication.repository.BooksRepository
import com.example.readerapplication.repository.BooksRepositoryImpl
import com.example.readerapplication.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    fun provideBooksRepository(): BooksRepository {
        return BooksRepositoryImpl(provideBookApi())
    }

    @Singleton
    @Provides
    fun provideBookApi(): BookApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookApi::class.java)
    }

    @Singleton
    @Provides
    fun provideBookFireRepository() =
        BookFireRepositoryImpl(bookFireApi = FirebaseFirestore.getInstance().collection("books"))
}