package com.example.smartflowassessment.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.smartflowassessment.ui.screens.dashboard.DashboardScreen
import com.example.smartflowassessment.ui.screens.itemdetail.ItemDetailScreen
import com.example.smartflowassessment.ui.screens.itemedit.ItemEditScreen
import com.example.smartflowassessment.ui.screens.itemlist.ItemListScreen
import com.example.smartflowassessment.ui.screens.reports.ReportsScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController)
        }
        composable(Screen.ItemList.route) {
            ItemListScreen(navController)
        }
        composable(
            route = Screen.ItemDetail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: return@composable
            ItemDetailScreen(navController, itemId)
        }
        composable(
            route = Screen.ItemEdit.route + "?itemId={itemId}",
            arguments =
                listOf(
                    navArgument("itemId") {
                        type = NavType.IntType
                        defaultValue = -1
                    },
                ),
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId")?.takeIf { it != -1 }
            ItemEditScreen(navController = navController, itemId = itemId)
        }
        composable(Screen.Reports.route) {
            ReportsScreen(navController)
        }
    }
}
