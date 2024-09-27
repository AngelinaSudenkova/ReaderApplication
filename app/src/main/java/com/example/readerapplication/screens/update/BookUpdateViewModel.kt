package com.example.readerapplication.screens.update

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readerapplication.data.DataOrException
import com.example.readerapplication.model.MBook
import com.example.readerapplication.repository.BookFireRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookUpdateViewModel @Inject constructor(
    private val repositoryImpl: BookFireRepositoryImpl
) : ViewModel() {

    val data: MutableState<DataOrException<MBook, Boolean, Exception>> =
        mutableStateOf(DataOrException(null, true, Exception("")))

    fun getBookFromDatabase(bookId: String) {
        viewModelScope.launch {
            try {
                data.value.loading = true
                data.value = repositoryImpl.getBookFromDatabase(bookId)
                if (data.value.data != null) data.value.loading = false
            } catch (e: FirebaseFirestoreException) {
                data.value.e = e
                data.value.loading = false
            }
        }
    }
}