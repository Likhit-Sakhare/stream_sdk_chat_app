package com.likhit.streamsdkchatapp.screens.channel_list

sealed class ChannelListEvent {
    data class ErrorCreateChannel(val message: String): ChannelListEvent()
    data object SuccessCreateChannel: ChannelListEvent()
    data class ErrorLogout(val message: String): ChannelListEvent()
    data object SuccessLogout: ChannelListEvent()
}