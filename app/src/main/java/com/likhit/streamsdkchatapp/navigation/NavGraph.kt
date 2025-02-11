package com.likhit.streamsdkchatapp.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.likhit.streamsdkchatapp.screens.channel_list.ChannelListScreen
import com.likhit.streamsdkchatapp.screens.login.LoginScreen
import com.likhit.streamsdkchatapp.screens.messages.MessageScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Login.route
    ) {
        composable(route = Routes.Login.route){
            LoginScreen(navController = navController)
        }
        composable(route = Routes.ChannelList.route){
            ChannelListScreen(navController = navController)
        }
        composable(
            route = Routes.MessagesList.route,
            arguments = listOf(
                navArgument(
                    name = "message_id"
                ){
                    type = NavType.StringType
                }
            )
        ){ backStackEntry ->
            val channelId = backStackEntry.arguments?.getString("message_id") ?: ""
            Log.d("NavGraph", "Navigated to MessageScreen with channelId: $channelId")
            if (channelId.isEmpty()) {
                Log.e("NavGraph", "Error: channelId is empty!")
            }
            MessageScreen(
                navController = navController,
                channelId = channelId
            )
        }
    }
}