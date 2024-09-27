package com.example.readerapplication.screens.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readerapplication.data.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import com.example.readerapplication.repository.FirebaseAuthRepositoryImpl
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepositoryImpl
) : ViewModel() {

    private val _loadingState = mutableStateOf<LoadingState>(LoadingState.Success())
    val loadingState: State<LoadingState> = _loadingState

    fun signInEmailAndPassword(email: String, password: String, navigateTo: () -> Unit) {
        viewModelScope.launch {
            try {
                _loadingState.value = LoadingState.Loading()
                val result = authRepository.loginWithEmailAndPassword(email, password)
                if (result?.user != null) {
                    _loadingState.value = LoadingState.Success("Success")
                    navigateTo()
                } else {
                    _loadingState.value = LoadingState.Failed("User not found")
                }

            } catch (e: Exception) {
                _loadingState.value = LoadingState.Failed(e.message)
            }
        }
    }


    fun createUserWithEmailAndPassword(email: String, password: String, navigateTo: () -> Unit) {
        viewModelScope.launch {
            try {
                if (_loadingState.value != LoadingState.Loading()) {
                    _loadingState.value = LoadingState.Loading()
                    val result = authRepository.createUserWithEmailAndPassword(email, password)
                    if (result?.user != null) {
                        _loadingState.value = LoadingState.Success("Success")
                        navigateTo()
                    } else {
                        _loadingState.value = LoadingState.Failed(result.toString())
                    }
                }
            } catch (e: Exception) {
                _loadingState.value = LoadingState.Failed(e.message)
            }
        }
    }

    fun logOut(navigateTo: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            navigateTo()
        }
    }


}