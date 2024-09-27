package com.example.readerapplication.screens.update

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.readerapplication.components.ErrorContent
import com.example.readerapplication.components.LoadingIndicator
import com.example.readerapplication.components.RatingBar
import com.example.readerapplication.components.ReaderTopAppBar
import com.example.readerapplication.components.RoundedButton
import com.example.readerapplication.components.ShimmerLoadingPicture
import com.example.readerapplication.model.MBook
import com.example.readerapplication.navigation.ReaderScreens
import com.example.readerapplication.utils.formatDate
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BookUpdateScreen(
    navController: NavController,
    bookId: String,
    viewModel: BookUpdateViewModel = hiltViewModel()
) {
    val bookState = viewModel.data
    LaunchedEffect(key1 = true) {
        viewModel.getBookFromDatabase(bookId.trim())
    }

    Scaffold(topBar = {
        ReaderTopAppBar(
            title = "Update Book",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController
        )
    }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier.padding(top = 3.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = CenterHorizontally
            ) {
                if (bookState.value.loading == true) {
                    LoadingIndicator()
                } else if ((bookState.value.data != null)) {
                    CardListItem(book = bookState.value.data!!)
                    Spacer(Modifier.height(16.dp))
                    ShowSimpleForm(book = bookState.value.data!!, navController)
                } else {
                    ErrorContent(errorMessage = "Error finding the book in your library") {
                        navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                    }
                }
            }
        }
    }
}


@ExperimentalComposeUiApi
@Composable
fun ShowSimpleForm(
    book: MBook,
    navController: NavController
) {
    val context = LocalContext.current

    val notesText = rememberSaveable { mutableStateOf(book.notes ?: "No thoughts") }
    val isStartedReading = rememberSaveable { mutableStateOf(book.startedReading != null) }
    val isFinishedReading = rememberSaveable { mutableStateOf(book.finishedReading != null) }
    val ratingVal = rememberSaveable { mutableIntStateOf(book.rating?.toInt() ?: 0) }
    var bookUpdatedSuccessfully by remember { mutableStateOf(false) }

    SimpleForm(valueState = notesText) { note ->
        notesText.value = note
    }

    Row(
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        TextButton(
            onClick = { isStartedReading.value = !isStartedReading.value },
            enabled = book.startedReading == null
        ) {
            if (book.startedReading == null) {
                if (!isStartedReading.value) {
                    Text(
                        text = "Start Reading",
                        modifier = Modifier.alpha(0.6f),
                        color = Color.Red.copy(alpha = 0.5f)
                    )
                } else {
                    Text(
                        text = "Started Reading!",
                        modifier = Modifier.alpha(0.6f),
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                }

            } else {
                Text("Started on: ${formatDate(book.startedReading!!)}")
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        TextButton(
            onClick = { isFinishedReading.value = !isFinishedReading.value },
            enabled = book.finishedReading == null
        ) {
            if (book.finishedReading == null) {
                if (!isFinishedReading.value) {
                    Text(
                        text = "Mark as Read", modifier = Modifier.alpha(0.6f),
                        color = Color.Red.copy(alpha = 0.5f)
                    )
                } else {
                    Text(
                        text = "Finished Reading!",
                        modifier = Modifier.alpha(0.6f),
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                }
            } else {
                Text(text = "Finished on: ${formatDate(book.finishedReading!!)}")
            }

        }

    }
    Text(text = "Rating", modifier = Modifier.padding(bottom = 3.dp))
    book.rating?.toInt().let {
        RatingBar(rating = it!!) { rating ->
            ratingVal.intValue = rating
        }

    }

    Spacer(modifier = Modifier.padding(bottom = 15.dp))
    Row {
        val changedNotes = book.notes != notesText.value
        val changedRating = book.rating?.toInt() != ratingVal.intValue
        val isFinishedTimeStamp =
            if (isFinishedReading.value) Timestamp.now() else book.finishedReading
        val isStartedTimeStamp =
            if (isStartedReading.value) Timestamp.now() else book.startedReading

        val bookUpdate =
            changedNotes || changedRating || isStartedReading.value || isFinishedReading.value

        val bookToUpdate = hashMapOf(
            "finishedReading" to isFinishedTimeStamp,
            "startedReading" to isStartedTimeStamp,
            "rating" to ratingVal.intValue,
            "notes" to notesText.value
        ).toMap()
        Log.d("Bookbookbook", bookToUpdate.toString())

        RoundedButton(label = "Update") {
            if (bookUpdate) {
                FirebaseFirestore.getInstance()
                    .collection("books")
                    .document(book.id!!)
                    .update(bookToUpdate)
                    .addOnCompleteListener {
                        bookUpdatedSuccessfully = true
                        Log.d("Bookbookbook", bookToUpdate.toString())
                    }.addOnFailureListener {
                        Log.d("ErrorBook", "Error updating document", it)
                    }
            }
        }
        if (bookUpdatedSuccessfully) {
            ShowToast(context, "Book updated successfully!")
            navController.navigate(ReaderScreens.ReaderHomeScreen.name)
            bookUpdatedSuccessfully = false
        }
        Spacer(modifier = Modifier.width(100.dp))
        val openDialog = remember {
            mutableStateOf(false)
        }
        if (openDialog.value) {
            ShowAlertDialog(
                message = "Are you sure you want to delete this book?", openDialog
            ) {
                FirebaseFirestore.getInstance()
                    .collection("books")
                    .document(book.id!!)
                    .delete()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            openDialog.value = false
                            navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                        }
                    }
            }
        }
        RoundedButton("Delete") {
            openDialog.value = true
        }
    }
}


@Composable
fun ShowToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


@Composable
fun CardListItem(
    book: MBook,
) {
    Surface(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = CircleShape,
        shadowElevation = 4.dp
    ) {
        Card(
            modifier = Modifier
                .padding(
                    start = 4.dp, end = 4.dp, top = 4.dp, bottom = 8.dp
                )
                .clip(RoundedCornerShape(20.dp)),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.Transparent)
            ) {
                ShimmerLoadingPicture(book.photoUrl.toString())
                Spacer(Modifier.width(8.dp))
                Column(modifier = Modifier.background(Color.Transparent)) {
                    Text(
                        text = book.title.toString(),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                            .width(120.dp),
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = book.authors.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(
                            start = 8.dp,
                            end = 8.dp,
                            top = 2.dp,
                            bottom = 0.dp
                        )
                    )

                    Text(
                        text = book.publishedDate.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(
                            start = 8.dp,
                            end = 8.dp,
                            top = 0.dp,
                            bottom = 8.dp
                        )
                    )

                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalComposeUiApi
@Composable
fun SimpleForm(
    valueState: MutableState<String>,
    onSearch: (String) -> Unit
) {
    Column {
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(valueState.value) { valueState.value.trim().isNotEmpty() }


        OutlinedTextField(
            value = valueState.value,
            onValueChange = { valueState.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(3.dp)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            label = { Text("Enter Your thoughts") },
            enabled = true,
            textStyle = TextStyle(fontWeight = FontWeight.Normal),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (valid) {
                        onSearch(valueState.value.trim())
                        keyboardController?.hide()
                    }
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Red.copy(alpha = 0.5f),
                focusedLabelColor = Color.Red.copy(alpha = 0.5f),
                containerColor = Color.White
            ),
        )
    }
}

@Composable
fun ShowAlertDialog(
    message: String,
    openDialog: MutableState<Boolean>,
    onYesPressed: () -> Unit
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "Delete Book") },
            text = { Text(text = message) },
            confirmButton = {
                TextButton(onClick = {
                    onYesPressed.invoke()
                    openDialog.value = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text("No")
                }
            }
        )
    }
}

