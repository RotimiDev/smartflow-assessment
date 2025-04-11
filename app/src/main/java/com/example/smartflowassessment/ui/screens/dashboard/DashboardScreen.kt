package com.example.smartflowassessment.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.smartflowassessment.R
import com.example.smartflowassessment.ui.components.ActivityCard
import com.example.smartflowassessment.ui.components.BottomNav
import com.example.smartflowassessment.ui.components.ChartComponent
import com.example.smartflowassessment.ui.components.MetricCard
import com.example.smartflowassessment.ui.components.OfflineIndicator
import com.example.smartflowassessment.ui.components.SalesBarChart
import com.example.smartflowassessment.ui.navigation.Screen
import com.example.smartflowassessment.utils.ChartType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    val items by viewModel.items.collectAsState()
    val salesData by viewModel.salesData.collectAsState()

    val totalItems = items.size
    val outOfStock = items.count { it.stock == 0 }
    val lowStock = items.count { it.stock in 1..5 }
    val totalValue = items.sumOf { it.price * it.stock }
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
                    text = stringResource(R.string.welcome_back),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    stringResource(R.string.key_metrics),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    item {
                        MetricCard(
                            label = stringResource(R.string.total_items),
                            value = totalItems,
                            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                        )
                    }
                    item {
                        MetricCard(
                            label = stringResource(R.string.out_of_stock),
                            value = outOfStock,
                            backgroundColor = MaterialTheme.colorScheme.errorContainer,
                        )
                    }
                    item {
                        MetricCard(
                            label = stringResource(R.string.low_stock),
                            value = lowStock,
                            backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                        )
                    }
                    item {
                        MetricCard(
                            label = stringResource(R.string.inventory_value),
                            value = "$${"%.2f".format(totalValue)}",
                            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                            isMonetary = true,
                        )
                    }
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
                        Text(stringResource(R.string.view_all_items))
                    }
                }
            }

            item {
                Text(
                    stringResource(R.string.stock_overview),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))
                val stockChartData = viewModel.stockByCategory.collectAsState()
                val (entries, labels) = stockChartData.value
                if (entries.isNotEmpty()) {
                    Text(
                        stringResource(R.string.distribution_across_categories),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    ChartComponent(
                        entries = entries,
                        labels = labels,
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

            item {
                Text(
                    stringResource(R.string.sales_performance),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (salesData.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.monthly_sales),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    SalesBarChart(
                        salesData = salesData,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .padding(vertical = 8.dp),
                    )
                } else {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                stringResource(R.string.loading_sales_data),
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    stringResource(R.string.recent_activity),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(recentActivity) { item ->
                ActivityCard(
                    item = item,
                    onClick = { navController.navigate("${Screen.ItemDetail.route}/${item.id}") },
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
