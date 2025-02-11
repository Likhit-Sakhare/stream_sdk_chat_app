package com.likhit.streamsdkchatapp.screens.channel_list

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.likhit.streamsdkchatapp.navigation.Routes
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.channels.SearchMode
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.channels.ChannelViewModelFactory
import io.getstream.chat.android.models.Filters

@Composable
fun ChannelListScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: ChannelListViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    var showDialog by remember {
        mutableStateOf(false)
    }
    var showCreateChannelDialog by remember {
        mutableStateOf(false)
    }

    val channelListEvent = viewModel.channelListEvent

    LaunchedEffect(key1 = Unit) {
        channelListEvent.collect { event ->
            when(event){
                is ChannelListEvent.ErrorCreateChannel -> {
                    Toast.makeText(
                        context,
                        "Error: ${event.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                ChannelListEvent.SuccessCreateChannel -> {
                    Toast.makeText(
                        context,
                        "Channel created",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is ChannelListEvent.ErrorLogout -> {
                    Toast.makeText(
                        context,
                        "Error: ${event.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                ChannelListEvent.SuccessLogout -> {
                    Toast.makeText(
                        context,
                        "User logout",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    if(showCreateChannelDialog){
        CreateChannelDialog(
            onDismiss = { channelName ->
                viewModel.createChannel(channelName)
                showCreateChannelDialog = false
            }
        )
    }

    if(showDialog){
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { activity?.finish() }) {
                    Text(text = "Exit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = "Cancel")
                }
            },
            title = {
                Text(text = "Exit App")
            },
            text = {
                Text(text = "Are you sure you want to exit?")
            }
        )
    }

    val filters = Filters.`in`(
        fieldName = "type",
        values = listOf("gaming", "messaging", "commerce", "team", "livestream")
    )
    val factory = ChannelViewModelFactory(
        filters = filters,
        channelLimit = 30
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) {
        ChatTheme{
            ChannelsScreen(
                viewModelFactory = factory,
                searchMode = SearchMode.Channels,
                title = "Channel List",
                isShowingHeader = true,
                onChannelClick = { channel ->
                    val cid = channel.cid
                    if(cid.isNullOrEmpty()){
                        Log.e("ChannelClick", "Channel CID is empty")
                    }else{
                        Log.e("ChannelClick", "Navigating to channel with cid: $cid")
                        val route = Routes.MessagesList.route.replace("{message_id}", cid)
                        navController.navigate(route)
                    }
                },
                onBackPressed = {
                    showDialog = true
                },
                onHeaderActionClick = {
                    showCreateChannelDialog = true
                },
                onHeaderAvatarClick = {
                    viewModel.logout()
                    navController.navigate(Routes.Login.route)
                }
            )
        }
    }
}