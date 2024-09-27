package com.example.readerapplication.components

import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssistWalker
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.CodeOff
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.readerapplication.R
import com.example.readerapplication.model.Item
import com.example.readerapplication.model.MBook
import com.example.readerapplication.navigation.ReaderScreens
import com.example.readerapplication.utils.Constants
import com.example.readerapplication.utils.formatDate
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = emailState.value,
        onValueChange = { emailState.value = it },
        label = { Text(text = labelId) },
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = Color.Black
        ),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Red.copy(alpha = 0.5f),
            focusedLabelColor = Color.Red.copy(alpha = 0.5f),
            containerColor = Color.White
        ),
        shape = CircleShape,
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = imeAction),
        keyboardActions = onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    passwordState: MutableState<String>,
    labelId: String = "Password",
    enabled: Boolean = true,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    val visualTransformation = if (passwordVisibility.value) VisualTransformation.None
    else PasswordVisualTransformation()

    OutlinedTextField(
        value = passwordState.value,
        onValueChange = {
            passwordState.value = it
        },
        label = { Text(text = labelId) },
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        shape = CircleShape,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Red.copy(alpha = 0.5f),
            focusedLabelColor = Color.Red.copy(alpha = 0.5f),
            containerColor = Color.White
        ),
        visualTransformation = visualTransformation,
        trailingIcon = { PasswordVisibility(passwordVisibility = passwordVisibility) },
        keyboardActions = onAction
    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(modifier = Modifier.size(24.dp), onClick = { passwordVisibility.value = !visible }) {
        Icon(
            imageVector = if (visible) Icons.Filled.Code else Icons.Filled.CodeOff,
            contentDescription = if (visible) "Hide password" else "Show password"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchInputField(
    modifier: Modifier = Modifier,
    labelId: String = "Search",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Done,
    onSearch: (String) -> Unit
) {
    val fieldState: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val isValid = remember(fieldState.value) {
        fieldState.value.trim().isNotEmpty()
    }

    val keyboardActions = KeyboardActions(
        onDone = {
            if (isValid) {
                onSearch(fieldState.value)
            }
        }
    )

    OutlinedTextField(
        value = fieldState.value,
        onValueChange = { fieldState.value = it },
        label = { Text(text = labelId) },
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = Color.Black
        ),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Red.copy(alpha = 0.5f),
            focusedLabelColor = Color.Red.copy(alpha = 0.5f),
            containerColor = Color.White
        ),
        shape = CircleShape,
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions
    )
}


@Composable
fun SubmitButton(
    textId: String,
    loading: Boolean,
    validInputs: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .width(250.dp),
        enabled = !loading && validInputs,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red.copy(alpha = 0.5f),
            disabledContentColor = Color.Red.copy(alpha = 0.5f),
            contentColor = Color.White
        )
    ) {
        if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(text = textId, modifier = Modifier.padding(5.dp))

    }

}

@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(
        onClick = { onTap() },
        shape = RoundedCornerShape(50.dp),
        containerColor = Color.Red.copy(alpha = 0.5f),
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add a Book",
            tint = Color.White
        )

    }

}

@Composable
fun RowOfBooks(
    listOfMBooks: List<MBook>,
    navController: NavController,
    text: String = "Start reading today!"
) {
    if (listOfMBooks.isEmpty()) {
        Box(modifier = Modifier
            .height(242.dp)
            .padding(horizontal = 16.dp)) {
            Text(text = text, color = Color.Red.copy(0.5f))
        }
    }
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {

        items(listOfMBooks) { book ->
            BookCard(MBook = book) {
                navController.navigate(ReaderScreens.UpdateScreen.name + "/${book.id}")
            }
        }
    }
}

@Composable
fun BookCard(
    MBook: MBook,
    onDetailsPress: () -> Unit = {}
) {

    val isStartedReading = remember {
        mutableStateOf(false)
    }

    Card(shape = RoundedCornerShape(29.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Black
        ),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .padding(16.dp)
            .height(242.dp)
            .width(202.dp)
            .clickable { onDetailsPress() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    ShimmerLoadingPicture(MBook.photoUrl)
                    Spacer(modifier = Modifier.width(16.dp))
                    val rating = MBook.rating ?: 0.0
                    BookRating(rating)
                }

                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = MBook.title!!, fontWeight = FontWeight.Bold,
                    maxLines = 2, overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = MBook.authors!!, style = MaterialTheme.typography.labelSmall
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Right,
                verticalAlignment = Alignment.Bottom
            ) {
                isStartedReading.value = MBook.startedReading != null
                RoundedButton(
                    label = if (isStartedReading.value) "Reading" else "Not Yet",
                    radius = 70
                )

            }
        }
    }
}

@Preview
@Composable
fun RoundedButton(
    label: String = "Reading",
    radius: Int = 29,
    onPress: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.clip(
            RoundedCornerShape(
                bottomEndPercent = radius,
                topStartPercent = radius
            )
        ),
        color = Color.Red.copy(0.5f)
    ) {
        Column(
            modifier = Modifier
                .width(90.dp)
                .heightIn(40.dp)
                .clickable { onPress.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 15.sp
                ),
            )
        }
    }
}

@Composable
fun ShimmerLoadingPicture(photoUrl: String?) {

    SubcomposeAsyncImage(
        model = photoUrl,
        contentDescription = "Image",
        modifier = Modifier
            .height(140.dp)
            .width(100.dp)
            .clip(RoundedCornerShape(29.dp)),
        contentScale = ContentScale.Crop,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer()
                    )
            )
        },
        error = {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Filled.ReportProblem,
                    contentDescription = "Problem Icon",
                    modifier = Modifier
                        .size(45.dp)
                        .align(Alignment.Center),
                    tint = Color.Red.copy(0.8f)
                )
            }
        }
    )
}


@Composable
fun BookRating(score: Double) {
    Surface(
        modifier = Modifier
            .height(70.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(56.dp),
        color = Color.White,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.StarBorder, contentDescription = "Start",
                modifier = Modifier.padding()
            )
            Text(text = score.toString(), style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    label: String
) {
    Surface(modifier = modifier.padding(horizontal = 16.dp)) {
        Column {
            Text(
                text = label,
                fontSize = 19.sp,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Left
            )
        }

    }
}

@Composable
fun UserIcon(
    userName: String = "???",
    navigateTo: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.AccountCircle, contentDescription = "Account Icon",
            modifier = Modifier
                .size(45.dp)
                .clickable() {
                    navigateTo()
                }, tint = Color.Red.copy(alpha = 0.2f)
        )
        Text(
            text = userName,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    navigateTo()
                },
            style = MaterialTheme.typography.titleSmall,
            color = Color.Red.copy(0.5f),
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Clip
        )
        HorizontalDivider(color = Color.Red.copy(0.5f), modifier = Modifier.width(45.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderTopAppBar(
    title: String,
    icon: ImageVector? = null,
    showProfile: Boolean = true,
    navController: NavController,
) {
    TopAppBar(title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showProfile) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Logo Icon",
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .scale(0.9f),
                    tint = Color.Red.copy(0.5f)
                )

            }
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = "arrow back",
                    tint = Color.Red.copy(alpha = 0.7f),
                    modifier = Modifier.clickable { navController.popBackStack() })
            }
            Spacer(modifier = Modifier.width(40.dp))
            Text(
                text = title,
                color = Color.Red.copy(alpha = 0.7f),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
        }
    },
        actions = {
            IconButton(onClick = {
                FirebaseAuth.getInstance()
                    .signOut().run {
                        navController.navigate(ReaderScreens.LoginScreen.name)
                    }
            }) {
                if (showProfile) Row() {
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = "Logout",
                        tint = Color.Red.copy(alpha = 0.5f)
                    )
                } else Box {}
            }
        })
}

@Composable
fun BookSearchCard(
    book: Item,
    onPress: () -> Unit
) {
    Card(shape = RoundedCornerShape(29.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Black
        ),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(181.dp)
            .clickable { onPress() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (book.volumeInfo.imageLinks == null) {
                    ShimmerLoadingPicture(Constants.NO_PHOTO_URL)
                } else {
                    ShimmerLoadingPicture(book.volumeInfo.imageLinks.thumbnail)
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            Column(
                Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = book.volumeInfo.title, overflow = TextOverflow.Ellipsis)
                Text(
                    text = "Author: ${book.volumeInfo.authors}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )

                Text(
                    text = "Date: ${book.volumeInfo.publishedDate}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black
                )

                Text(
                    text = "${book.volumeInfo.categories}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black
                )
            }

        }
    }
}

@Composable
fun LoadingIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.size(48.dp),
        color = Color.Red.copy(alpha = 0.5f)
    )
}

@Composable
fun ErrorContent(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.AssistWalker,
            contentDescription = "Error",
            tint = Color.Red,
            modifier = Modifier.size(120.dp)
        )
        Text(
            text = errorMessage,
            color = Color.Red,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(0.5f))
        ) {
            Text(text = "Retry")
        }
    }
}


@ExperimentalComposeUiApi
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    onPressRating: (Int) -> Unit
) {
    var ratingState by remember {
        mutableStateOf(rating)
    }

    var selected by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(
        targetValue = if (selected) 42.dp else 34.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )

    Row(
        modifier = Modifier.width(280.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_star),
                contentDescription = "star",
                modifier = modifier
                    .width(size)
                    .height(size)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                onPressRating(i)
                                ratingState = i
                            }

                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }
    }
}


@Composable
fun MBookCard(book: MBook, onPress: () -> Unit) {
    Card(
        shape = RoundedCornerShape(29.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Black
        ),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(181.dp)
            .clickable { onPress() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (book.photoUrl.isNullOrEmpty()) {
                    ShimmerLoadingPicture(Constants.NO_PHOTO_URL)
                } else {
                    ShimmerLoadingPicture(book.photoUrl)
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            Column(
                Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = book.title.orEmpty(), overflow = TextOverflow.Ellipsis)

                Text(
                    text = "Author: ${book.authors.orEmpty()}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Started Reading: ${
                        book.startedReading?.let { formatDate(it) } ?: "Not started"
                    }",
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black
                )

                Text(
                    text = "Finished Reading: ${
                        book.finishedReading?.let { formatDate(it) } ?: "Not finished"
                    }",
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black
                )
            }
        }
    }
}
