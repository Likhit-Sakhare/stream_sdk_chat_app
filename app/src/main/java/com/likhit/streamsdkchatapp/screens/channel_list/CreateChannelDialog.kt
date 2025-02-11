package com.likhit.streamsdkchatapp.screens.channel_list

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun CreateChannelDialog(
    modifier: Modifier = Modifier,
    onDismiss: (String) -> Unit
) {

    var channelName by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = { onDismiss(channelName) },
        confirmButton = {
            Button(
                onClick = { onDismiss(channelName) }
            ) {
                Text(text = "Create Channel")
            }
        },
        title = {
            Text(text = "Enter channel name")
        },
        text = {
            OutlinedTextField(
                value = channelName,
                onValueChange = {
                    channelName = it
                }
            )
        }
    )
}