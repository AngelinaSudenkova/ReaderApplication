package com.example.readerapplication.screens.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.readerapplication.components.MBookCard
import com.example.readerapplication.components.ReaderTopAppBar
import com.example.readerapplication.model.MBook
import com.example.readerapplication.navigation.ReaderScreens
import com.example.readerapplication.screens.home.ReaderHomeViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

@Composable
fun StatsScreen(
    navController: NavController,
    viewModel: ReaderHomeViewModel = hiltViewModel()
) {
    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(topBar = {
        ReaderTopAppBar(
            title = "Stats",
            navController = navController,
            icon = Icons.Filled.ArrowBack,
            showProfile = false
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            books = if (!viewModel.data.value.data.isNullOrEmpty()) {
                viewModel.data.value.data!!.filter { mBook ->
                    (mBook.userId == currentUser?.uid)
                }
            } else {
                emptyList()

            }
            Row(horizontalArrangement = Arrangement.End,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .padding(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Sharp.Person,
                        contentDescription = "icon",
                        tint = Color.Red.copy(0.5f)
                    )
                }
                Text(
                    text = "Hi, ${
                        currentUser?.email.toString().split("@")[0].uppercase(Locale.getDefault())
                    }"
                )

            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .background(Color.White),
                elevation = CardDefaults.cardElevation(5.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                val readBooksList: List<MBook> = if (!viewModel.data.value.data.isNullOrEmpty()) {
                    books.filter { mBook ->
                        (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                    }

                } else {
                    emptyList()
                }

                val readingBooks = books.filter { mBook ->
                    (mBook.startedReading != null && mBook.finishedReading == null)
                }

                Column(
                    modifier = Modifier.padding(start = 25.dp, top = 4.dp, bottom = 4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = "Your Stats", style = MaterialTheme.typography.titleMedium,
                        color = Color.Red.copy(0.5f))
                    Text(text = "You're reading: ${readingBooks.size} books")
                    Text(text = "You've read: ${readBooksList.size} books")

                }

            }

            if (viewModel.data.value.loading == true) {
                LinearProgressIndicator()
            } else {
                Divider()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    val readBooks: List<MBook> = if (!viewModel.data.value.data.isNullOrEmpty()) {
                        viewModel.data.value.data!!.filter { mBook ->
                            (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                        }
                    } else {
                        emptyList()
                    }
                    items(items = readBooks) { book ->
                        MBookCard(book = book) { navController.navigate(ReaderScreens.UpdateScreen.name + "/${book.id}") }
                    }
                }

            }
        }

    }
}

