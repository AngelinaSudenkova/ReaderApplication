package com.example.readerapplication.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.readerapplication.components.EmailInput
import com.example.readerapplication.components.LoadingIndicator
import com.example.readerapplication.components.PasswordInput
import com.example.readerapplication.components.SubmitButton
import com.example.readerapplication.navigation.ReaderScreens
import com.example.readerapplication.screens.ReaderLogo
import com.example.readerapplication.data.LoadingState


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel<LoginViewModel>()
) {

    val loadingState = viewModel.loadingState.value
    val showLoginForm = rememberSaveable { mutableStateOf(true) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            ReaderLogo()
            Spacer(modifier = Modifier.height(15.dp))


            if (showLoginForm.value) {
                LoginFrom() { email, password ->
                    viewModel.signInEmailAndPassword(email, password) {
                        navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                    }
                }
            } else {
                SignUpForm(){email, password ->
                    viewModel.createUserWithEmailAndPassword(email,password){
                        navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                    }

                }
            }

            when (loadingState) {
                is LoadingState.Loading -> {
                    LoadingIndicator()
                }

                is LoadingState.Success -> {
                }

                is LoadingState.Failed -> {
                    Text(text = "Error: ${loadingState.message}", color = Color.Red)
                }
            }

            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text = if (showLoginForm.value) "Sign up" else "Login"
                Text(text = "New User?")
                Text(text,
                    modifier = Modifier
                        .clickable {
                            showLoginForm.value = !showLoginForm.value

                        }
                        .padding(start = 5.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color.Red.copy(0.5f))

            }
        }
    }
}

@Composable
fun LoginFrom(
    loading: Boolean = false,
    OnDone: (String, String) -> Unit = { email, pwd -> }
) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    val modifier = Modifier
        .height(250.dp)
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())

    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        EmailInput(emailState = email)
        PasswordInput(passwordState = password, passwordVisibility = passwordVisibility)
        SubmitButton(textId = "Login", loading = loading, validInputs = valid) {
            OnDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }


}

@Composable
fun SignUpForm(
    loading: Boolean = false,
    onDone: (String, String) -> Unit = { email, pwd -> }
) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordRepeat = rememberSaveable() { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val passwordRepeatVisibility = rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value, passwordRepeat.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
                && (passwordRepeat.value.trim() == password.value.trim()
                || password.value.trim() == passwordRepeat.value.trim())
    }

    val modifier = Modifier
        .height(350.dp)
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())

    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        EmailInput(emailState = email)
        PasswordInput(passwordState = password, passwordVisibility = passwordVisibility)
        PasswordInput(
            passwordState = passwordRepeat,
            passwordVisibility = passwordRepeatVisibility,
            labelId = "Repeat password"
        )
        SubmitButton(textId = "Login", loading = loading, validInputs = valid) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

