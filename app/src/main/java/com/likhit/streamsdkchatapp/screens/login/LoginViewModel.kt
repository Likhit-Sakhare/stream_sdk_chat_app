package com.likhit.streamsdkchatapp.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.likhit.streamsdkchatapp.utils.LoadingState
import com.likhit.streamsdkchatapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val client: ChatClient
): ViewModel() {

    private val _loginEvent = MutableSharedFlow<LoginEvent>()
    val loginEvent = _loginEvent.asSharedFlow()

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.FinishLoading)
    val loadingState: StateFlow<LoadingState>
        get() = _loadingState

    private fun isValidUserName(userName: String): Boolean {
        return userName.length > Constants.MIN_USERNAME_LENGTH
    }

    fun loginUser(userName: String, token: String? = null){
        val trimmedUserName = userName.trim()
        viewModelScope.launch {
            if(isValidUserName(trimmedUserName) && token != null){
                loginRegisteredUser(trimmedUserName, token)
            } else if(isValidUserName(trimmedUserName) && token == null){
                loginGuestUser(trimmedUserName)
            }else{
                _loginEvent.emit(LoginEvent.ErrorInputTooShort)
            }
        }
    }

    private fun loginRegisteredUser(
        userName: String,
        token: String
    ) {
        val user = User(
            id = userName,
            name = userName
        )

        _loadingState.value = LoadingState.Loading

        client.connectUser(
            user = user,
            token = token
        ).enqueue { result ->

            _loadingState.value = LoadingState.FinishLoading

            if(result.isSuccess){
                viewModelScope.launch {
                    _loginEvent.emit(LoginEvent.Success)
                }
            }else{
                viewModelScope.launch {
                    _loginEvent.emit(
                        LoginEvent.ErrorLogin(
                            message = result.errorOrNull()?.message ?: "Unknown Error"
                        )
                    )
                }
            }
        }
    }

    private fun loginGuestUser(
        userName: String
    ){
        _loadingState.value = LoadingState.Loading

        client.connectGuestUser(
            userId = userName,
            username = userName
        ).enqueue { result ->

            _loadingState.value = LoadingState.FinishLoading

            if(result.isSuccess){
                viewModelScope.launch {
                    _loginEvent.emit(LoginEvent.Success)
                }
            }else{
                viewModelScope.launch {
                    _loginEvent.emit(
                        LoginEvent.ErrorLogin(
                            message = result.errorOrNull()?.message ?: "Unknown Error"
                        )
                    )
                }
            }
        }
    }
}