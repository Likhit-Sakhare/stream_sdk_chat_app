package com.likhit.streamsdkchatapp.utils

sealed class LoadingState {
    data object Loading: LoadingState()
    data object FinishLoading: LoadingState()
}