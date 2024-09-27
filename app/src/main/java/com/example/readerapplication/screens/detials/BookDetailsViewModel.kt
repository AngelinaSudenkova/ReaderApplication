package com.example.readerapplication.screens.detials

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readerapplication.data.Resource
import com.example.readerapplication.model.BookList
import com.example.readerapplication.model.Item
import com.example.readerapplication.repository.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel@Inject constructor(
    val repository: BooksRepository
) : ViewModel() {
    private val _book: MutableState<Resource<Item>> =
        mutableStateOf(Resource.Init())
    val book: MutableState<Resource<Item>> = _book

    fun searchBook(query: String) {
        try {
            _book.value = Resource.Loading(null)
            viewModelScope.launch {
                if (query.isEmpty()) {
                    return@launch
                }
                _book.value = repository.getBookInfo(query)
            }
        } catch (e: Exception) {
            _book.value = Resource.Error(e.message)
        }
    }
}