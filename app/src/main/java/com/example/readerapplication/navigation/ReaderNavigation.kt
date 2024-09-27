package com.example.readerapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.readerapplication.screens.SplashScreen
import com.example.readerapplication.screens.detials.BookDetailsScreen
import com.example.readerapplication.screens.home.HomeScreen
import com.example.readerapplication.screens.login.LoginScreen
import com.example.readerapplication.screens.search.BookSearchScreen
import com.example.readerapplication.screens.stats.StatsScreen
import com.example.readerapplication.screens.update.BookUpdateScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name) {

        composable(ReaderScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(ReaderScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }
        composable(ReaderScreens.ReaderHomeScreen.name) {
            HomeScreen(navController = navController)
        }
        composable(ReaderScreens.SearchScreen.name) {
            BookSearchScreen(navController = navController)
        }
        composable(
            ReaderScreens.DetailScreen.name + "/{bookId}",
            arguments = listOf(navArgument("bookId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let {
                BookDetailsScreen(navController = navController, bookId = it.toString())
            }
        }

        val updateName = ReaderScreens.UpdateScreen.name
        composable("$updateName/{bookItemId}",
            arguments = listOf(navArgument("bookItemId") {
                type = NavType.StringType
            })) { navBackStackEntry ->

            navBackStackEntry.arguments?.getString("bookItemId").let {
                BookUpdateScreen(navController = navController, bookId = it.toString())
            }
        }

        composable(ReaderScreens.ReaderStatsScreen.name) {
         StatsScreen(navController = navController)
        }
    }
}