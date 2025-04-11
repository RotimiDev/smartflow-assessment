package com.example.smartflowassessment.ui.navigation

sealed class Screen(
    val route: String,
) {
    data object Dashboard : Screen("dashboard")

    data object ItemList : Screen("item_list")

    data object ItemDetail : Screen("item_detail/{itemId}") {
        fun createRoute(itemId: Int) = "item_detail/$itemId"
    }

    data object ItemEdit : Screen("item_edit")

    data object Reports : Screen("reports")
}
