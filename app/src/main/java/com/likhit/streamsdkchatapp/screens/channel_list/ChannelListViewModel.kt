package com.likhit.streamsdkchatapp.screens.channel_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChannelListViewModel @Inject constructor(
    private val chatClient: ChatClient
): ViewModel() {

    private val _channelListEvent = MutableSharedFlow<ChannelListEvent>()
    val channelListEvent = _channelListEvent.asSharedFlow()

    fun createChannel(
        channelName: String,
        channelType: String = "messaging"
    ){
        val trimmedChannelName = channelName.trim()
        val channelId = UUID.randomUUID().toString()

        viewModelScope.launch {
            if(trimmedChannelName.isEmpty()){
                _channelListEvent.emit(
                    ChannelListEvent.ErrorCreateChannel("The channel name can't be empty")
                )
                return@launch
            }

            chatClient.createChannel(
                channelType = channelType,
                channelId = channelId,
                memberIds = emptyList(),
                extraData = mapOf(
                    "name" to trimmedChannelName
                )
            ).enqueue { result ->
                if(result.isSuccess){
                    viewModelScope.launch {
                        _channelListEvent.emit(ChannelListEvent.SuccessCreateChannel)
                    }
                }else{
                    viewModelScope.launch {
                        _channelListEvent.emit(
                            ChannelListEvent.ErrorCreateChannel(result.errorOrNull()?.message?: "Unknown error")
                        )
                    }
                }
            }
        }
    }

    fun logout(){
        chatClient.disconnect(
            flushPersistence = true
        ).enqueue { result ->
            if(result.isSuccess){
                viewModelScope.launch {
                    _channelListEvent.emit(ChannelListEvent.SuccessLogout)
                }
            }else{
                viewModelScope.launch {
                    _channelListEvent.emit(
                        ChannelListEvent.ErrorLogout(result.errorOrNull()?.message?: "Unknown error")
                    )
                }
            }
        }
    }
}