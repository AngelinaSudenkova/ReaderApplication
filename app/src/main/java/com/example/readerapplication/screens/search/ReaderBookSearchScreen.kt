package com.example.readerapplication.screens.search

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.readerapplication.components.BookSearchCard
import com.example.readerapplication.components.ErrorContent
import com.example.readerapplication.components.LoadingIndicator
import com.example.readerapplication.components.ReaderTopAppBar
import com.example.readerapplication.components.SearchInputField
import com.example.readerapplication.data.Resource
import com.example.readerapplication.navigation.ReaderScreens


@Composable
fun BookSearchScreen(
    navController: NavController,
    viewModel: BookSearchViewModel = hiltViewModel()
) {
    val listOfBooks = viewModel.listOfBooks
    val lastBookState = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            ReaderTopAppBar(
                title = "Search Books", icon = Icons.Default.ArrowBack,
                navController = navController, showProfile = false
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            SearchInputField {
                viewModel.searchBooks(it)
                lastBookState.value = it
            }
            Spacer(modifier = Modifier.height(16.dp))

            when (listOfBooks.value) {
                is Resource.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    { LoadingIndicator() }

                }

                is Resource.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        val items = listOfBooks.value.data?.items ?: emptyList()
                        if (items.isEmpty()) {
                        } else {
                            items(items) { item ->
                                BookSearchCard(item){
                                    navController.navigate(ReaderScreens.DetailScreen.name + "/${item.id}")
                                }
                                Log.d("IMTHEBOOK", "BookSearchScreen: ${item.volumeInfo}")
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    ErrorContent(
                        errorMessage = listOfBooks.value.message ?: "Something went wrong!",
                        onRetry = { viewModel.searchBooks(lastBookState.value) }
                    )
                }
                is Resource.Init -> {}
            }
        }
    }
}




