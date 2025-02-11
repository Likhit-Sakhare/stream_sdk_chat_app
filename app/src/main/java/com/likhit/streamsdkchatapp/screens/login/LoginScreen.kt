package com.likhit.streamsdkchatapp.screens.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.likhit.streamsdkchatapp.utils.LoadingState
import com.likhit.streamsdkchatapp.R

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var userName by remember {
        mutableStateOf("")
    }

    val loginEvent = viewModel.loginEvent

    val loadingState by viewModel.loadingState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        loginEvent.collect { event ->
            when(event){
                LoginEvent.ErrorInputTooShort -> {
                    Toast.makeText(
                        context,
                        "Invalid: Enter more than 3 characters",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is LoginEvent.ErrorLogin -> {
                    Toast.makeText(
                        context,
                        "Error: ${event.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                LoginEvent.Success -> {
                    Toast.makeText(
                        context,
                        "Login Successful",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate("channel_list")
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(loadingState is LoadingState.Loading){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }else {
            Image(
                painter = painterResource(id = R.drawable.chat),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = userName,
                onValueChange = {
                    userName = it
                },
                label = {
                    Text(text = "Username")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(25.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    viewModel.loginUser(
                        userName = userName
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(text = "Login as Guest")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.loginUser(
                        userName = userName,
                        token = context.getString(R.string.jwt_token)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(text = "Login as User")
            }
        }
    }
}