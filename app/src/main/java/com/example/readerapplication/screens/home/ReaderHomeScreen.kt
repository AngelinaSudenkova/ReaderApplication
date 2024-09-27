package com.example.readerapplication.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.readerapplication.components.FABContent
import com.example.readerapplication.components.ReaderTopAppBar
import com.example.readerapplication.components.RowOfBooks
import com.example.readerapplication.components.TitleSection
import com.example.readerapplication.components.UserIcon
import com.example.readerapplication.model.MBook
import com.example.readerapplication.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: ReaderHomeViewModel = hiltViewModel()
) {
    Text(text = " HomeScreen")
    Scaffold(topBar = { ReaderTopAppBar("The Reader", navController = navController) },
        floatingActionButton = {
            FABContent {
                navController.navigate(ReaderScreens.SearchScreen.name)
            }
        }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            HomeScreenContent(navController, viewModel)
        }
    }

}


@Composable
fun HomeScreenContent(navController: NavHostController, viewModel: ReaderHomeViewModel) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val nickname = currentUser?.email?.split("@")?.get(0)

    var listOfMBooks = emptyList<MBook>()

    var screenEntered by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = screenEntered) {
        viewModel.updateData()
    }

    DisposableEffect(Unit) {
        screenEntered = !screenEntered
        onDispose { }
    }


    if (!viewModel.data.value.data.isNullOrEmpty()) {
        listOfMBooks = viewModel.data.value.data!!.toList().filter { mBook ->
            mBook.userId == currentUser?.uid.toString()
        }
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 13.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            TitleSection(label = "You're reading \n" + " those books right now...")
            UserIcon(nickname.toString()) {
                navController.navigate(ReaderScreens.ReaderStatsScreen.name)
            }
        }
        RowOfBooks(
            listOfMBooks.filter { mBook -> mBook.startedReading != null && mBook.finishedReading == null },
            navController = navController
        )

        Spacer(modifier = Modifier.height(8.dp))
        TitleSection(label = "Reading List")

        RowOfBooks(
            listOfMBooks.filter { mBook -> mBook.startedReading == null && mBook.finishedReading == null },
            navController = navController, text = "Add a book to start reading"
        )

    }
}



