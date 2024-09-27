package com.example.readerapplication.screens.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readerapplication.data.Resource
import com.example.readerapplication.model.BookList
import com.example.readerapplication.repository.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(
    val repository: BooksRepository
) : ViewModel() {
    private val _listOfBooks: MutableState<Resource<BookList>> =
        mutableStateOf(Resource.Init())
    val listOfBooks: MutableState<Resource<BookList>> = _listOfBooks

    fun searchBooks(query: String) {
        try {
            _listOfBooks.value = Resource.Loading(null)
            viewModelScope.launch {
                if (query.isEmpty()) {
                    return@launch
                }
                _listOfBooks.value = repository.getAllBooks(query)
            }
        } catch (e: Exception) {
            _listOfBooks.value = Resource.Error(e.message)
        }
    }
}