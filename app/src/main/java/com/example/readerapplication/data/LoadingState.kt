package com.example.readerapplication.data

sealed class LoadingState {
    class Loading: LoadingState()
    data class Success(val message: String? = null) : LoadingState()
    data class Failed(val message: String? = null) : LoadingState()
}