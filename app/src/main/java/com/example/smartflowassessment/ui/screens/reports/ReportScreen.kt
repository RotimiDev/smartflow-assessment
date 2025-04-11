package com.example.smartflowassessment.ui.screens.reports

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.smartflowassessment.ui.components.BottomNav
import com.example.smartflowassessment.ui.components.ChartComponent
import com.example.smartflowassessment.ui.components.OfflineIndicator
import com.example.smartflowassessment.ui.components.TopBar
import com.example.smartflowassessment.utils.ChartType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    navController: NavController,
    viewModel: ReportsViewModel = hiltViewModel(),
) {
    val items by viewModel.items.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                title = "Reports",
                showBack = true,
                onBackClick = {
                    navController.popBackStack()
                },
            )
        },
        bottomBar = {
            BottomNav(navController)
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
        ) {
            OfflineIndicator()
            Spacer(modifier = Modifier.height(16.dp))

            if (items.isNotEmpty()) {
                val stockValues = items.map { it.stock.toFloat() }
                val chartLabels = items.map { it.title }

                ChartComponent(
                    entries = stockValues,
                    labels = chartLabels,
                    chartType = ChartType.PIE,
                )
            } else {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
