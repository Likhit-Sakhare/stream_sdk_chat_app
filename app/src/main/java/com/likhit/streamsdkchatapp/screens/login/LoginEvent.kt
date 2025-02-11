package com.likhit.streamsdkchatapp.screens.login

sealed class LoginEvent {
    data object ErrorInputTooShort: LoginEvent()
    data class ErrorLogin(val message: String): LoginEvent()
    data object Success: LoginEvent()
}