package com.likhit.streamsdkchatapp.navigation

sealed class Routes(val route: String) {
    data object Login: Routes("login")
    data object ChannelList: Routes("channel_list")
    data object MessagesList: Routes("messages_list/{message_id}")
}