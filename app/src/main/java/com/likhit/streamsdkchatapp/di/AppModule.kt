package com.likhit.streamsdkchatapp.di

import android.content.Context
import com.likhit.streamsdkchatapp.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideStatePluginFactory(@ApplicationContext context: Context) =
        StreamStatePluginFactory(
            config = StatePluginConfig(
                backgroundSyncEnabled = true,
                userPresence = true
            ),
            appContext = context
        )


    @Provides
    fun provideOfflinePluginFactory(@ApplicationContext context: Context) =
        StreamOfflinePluginFactory(
            context
        )


    @Provides
    @Singleton
    fun provideChatClient(
        @ApplicationContext context: Context,
        offlinePluginFactory: StreamOfflinePluginFactory,
        statePluginFactory: StreamStatePluginFactory,
    ): ChatClient {
        return ChatClient.Builder(
            context.getString(R.string.api_key),
            context
        ).withPlugins(offlinePluginFactory, statePluginFactory)
            .logLevel(ChatLogLevel.ALL)
            .build()
    }
}