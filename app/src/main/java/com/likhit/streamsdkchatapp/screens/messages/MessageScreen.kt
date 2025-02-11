package com.likhit.streamsdkchatapp.screens.messages

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamShapes
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory

@Composable
fun MessageScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    channelId: String
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, bottom = 16.dp)
            .imePadding()
    ) {
        ChatTheme(
            shapes = StreamShapes.defaultShapes().copy(
                avatar = RoundedCornerShape(16.dp),
                attachment = RoundedCornerShape(16.dp),
                myMessageBubble = RoundedCornerShape(16.dp),
                otherMessageBubble = RoundedCornerShape(16.dp)
            )
        ) {
            MessagesScreen(
                viewModelFactory = MessagesViewModelFactory(
                    context = context,
                    channelId = channelId
                ),
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
    }
}