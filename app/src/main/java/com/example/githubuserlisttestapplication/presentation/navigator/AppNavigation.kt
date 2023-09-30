package com.example.githubuserlisttestapplication.presentation.navigator

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.githubuserlisttestapplication.presentation.screens.UserListScreen
import com.example.githubuserlisttestapplication.presentation.screens.UserProfileScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "githubList"
    ) {
        composable("githubList") {
            UserListScreen(onUserClick = { user ->
                // Navigate to UserProfileScreen and pass the user's ID
                navController.navigate("userProfile/${user.id}")
            })
        }
        composable(
            route = "userProfile/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("id")
            userId?.let {
                // Call UserProfileScreen and pass the user's ID as an argument
                UserProfileScreen(userId = it, navController = navController)
            }
        }
    }
}

