package com.example.smartflowassessment.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.example.smartflowassessment.ui.components.MetricCard
import com.example.smartflowassessment.ui.components.OfflineIndicator
import com.example.smartflowassessment.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    val items by viewModel.items.collectAsState()
    val totalItems = items.size
    val outOfStock = items.count { it.stock == 0 }
    val recentActivity = items.take(5)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "") })
        },
        bottomBar = {
            BottomNav(navController)
        },
    ) { paddingValues ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            item {
                OfflineIndicator()
                Text(
                    text = "Welcome back 👋",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    MetricCard(
                        label = "Total Items",
                        value = totalItems,
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    )
                    MetricCard(
                        label = "Out of Stock",
                        value = outOfStock,
                        backgroundColor = MaterialTheme.colorScheme.errorContainer,
                    )
                }
            }

            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Button(
                        shape = RoundedCornerShape(8.dp),
                        onClick = {
                            navController.navigate(Screen.ItemList.route)
                        },
                        modifier = Modifier.height(45.dp),
                    ) {
                        Text("View All Items")
                    }
                }
            }

            item {
                Text("Stock Overview", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                val chartData = viewModel.chartData.collectAsState()
                val (entries, labels) = chartData.value
                if (entries.isNotEmpty()) {
                    ChartComponent(entries = entries, labels = labels)
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

            item {
                Text("Recent Activity", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(recentActivity) { item ->
                Text("Item: ${item.title} | Added on: ${item.updatedAt}")
            }
        }
    }
}
