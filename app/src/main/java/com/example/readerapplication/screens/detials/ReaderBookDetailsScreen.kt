package com.example.readerapplication.screens.detials

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.readerapplication.components.ErrorContent
import com.example.readerapplication.components.LoadingIndicator
import com.example.readerapplication.components.ReaderTopAppBar
import com.example.readerapplication.components.RoundedButton
import com.example.readerapplication.components.ShimmerLoadingPicture
import com.example.readerapplication.data.Resource
import com.example.readerapplication.model.Item
import com.example.readerapplication.model.MBook
import com.example.readerapplication.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun BookDetailsScreen(
    navController: NavController,
    bookId: String,
    viewModel: BookDetailsViewModel = hiltViewModel()
) {
    val bookInfoState = viewModel.book

    LaunchedEffect(key1 = bookId) {
        viewModel.searchBook(bookId)
    }

    Scaffold(topBar = {
        ReaderTopAppBar(
            title = "Book Details",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController
        )
    }) {

        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (bookInfoState.value) {
                    is Resource.Loading -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        )
                        { LoadingIndicator() }

                    }

                    is Resource.Success -> {
                        if (bookInfoState.value.data != null) {
                            ShowBookDetails(bookInfoState.value.data!!, navController)
                        }
                    }

                    is Resource.Error -> {
                        ErrorContent(
                            errorMessage = bookInfoState.value.message ?: "Something went wrong!",
                            onRetry = { navController.navigate(ReaderScreens.SearchScreen.name) }
                        )
                    }

                    is Resource.Init -> {}
                }


            }
        }
    }
}

@Composable
fun ShowBookDetails(bookInfo: Item, navController: NavController) {
    val bookData = bookInfo.volumeInfo

    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShimmerLoadingPicture(bookData.imageLinks.thumbnail)

        Text(
            text = bookData?.title.toString(),
            style = MaterialTheme.typography.labelLarge,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            maxLines = 19
        )

        Text(text = "Authors: ${bookData?.authors.toString()}")
        Text(text = "Page Count: ${bookData?.pageCount.toString()}")
        Text(
            text = "Categories: ${bookData?.categories.toString()}",
            style = MaterialTheme.typography.titleSmall,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "Published: ${bookData?.publishedDate.toString()}",
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(5.dp))

        val cleanDescription = HtmlCompat.fromHtml(
            bookData!!.description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        ).toString()
        val localDims = LocalContext.current.resources.displayMetrics
        Surface(
            modifier = Modifier
                .height(localDims.heightPixels.dp.times(0.09f))
                .padding(4.dp),
            shape = RectangleShape,
            border = BorderStroke(1.dp, Color.DarkGray)
        ) {
            LazyColumn(modifier = Modifier.padding(3.dp)) {
                item {
                    Text(text = cleanDescription)
                }

            }
        }

        //Buttons
        Row(
            modifier = Modifier.padding(top = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            RoundedButton(label = "Save") {
                //save this book to the firestore database
                val book = MBook(
                    title = bookData.title,
                    authors = bookData.authors.toString(),
                    description = bookData.description,
                    categories = bookData.categories.toString(),
                    notes = "",
                    photoUrl = bookData.imageLinks.thumbnail,
                    publishedDate = bookData.publishedDate,
                    pageCount = bookData.pageCount.toString(),
                    rating = 0.0,
                    googleBookId = bookInfo.id,
                    userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
                )

                saveToFirebase(book, navController = navController)
            }
            Spacer(modifier = Modifier.width(25.dp))
            RoundedButton(label = "Cancel") {
                navController.popBackStack()
            }

        }


    }
}

fun saveToFirebase(book: MBook, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")

    if (book.toString().isNotEmpty()) {
        dbCollection.add(book)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navController.popBackStack()
                        }
                    }.addOnFailureListener {
                        Log.w("Error", "SaveToFirebase:  Error updating doc", it)
                    }

            }
    }
}

