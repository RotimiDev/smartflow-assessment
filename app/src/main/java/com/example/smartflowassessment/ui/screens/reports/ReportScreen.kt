package com.example.smartflowassessment.ui.screens.reports

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.smartflowassessment.data.model.Item
import com.example.smartflowassessment.ui.components.BottomNav
import com.example.smartflowassessment.ui.components.ChartComponent
import com.example.smartflowassessment.ui.components.LowStockWarnings
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
    var currentTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabTitles = listOf("Stock Overview", "Trends", "Categories")

    Scaffold(
        topBar = {
            TopBar(
                title = "Reports",
                showBack = true,
                onBackClick = { navController.popBackStack() },
            )
        },
        bottomBar = { BottomNav(navController) },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            OfflineIndicator()

            TabRow(
                selectedTabIndex = currentTabIndex,
                modifier = Modifier.fillMaxWidth(),
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = currentTabIndex == index,
                        onClick = { currentTabIndex = index },
                        text = { Text(title) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (items.isEmpty()) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Box(modifier = Modifier.weight(1f)) {
                    when (currentTabIndex) {
                        0 -> StockOverviewScreen(items)
                        1 -> TrendsScreen(viewModel)
                        2 -> CategoriesScreen(items)
                    }
                }
            }
        }
    }
}

@Composable
fun StockOverviewScreen(items: List<Item>) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Current Stock Levels",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp),
        )

        ChartComponent(
            entries = items.map { it.stock.toFloat() },
            labels = items.map { it.title },
            chartType = ChartType.PIE,
            modifier = Modifier.height(300.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        LowStockWarnings(items)
    }
}

@Composable
fun TrendsScreen(viewModel: ReportsViewModel) {
    val monthlyTrends by viewModel.monthlyTrends.collectAsState()

    if (monthlyTrends.isEmpty()) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val quantityValues = monthlyTrends.map { it.quantity.toFloat() }
    val revenueValues = monthlyTrends.map { it.revenue }
    val months = monthlyTrends.map { it.month }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Monthly Item Quantity",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp),
        )

        ChartComponent(
            entries = quantityValues,
            labels = months,
            chartType = ChartType.BAR,
            modifier = Modifier.height(300.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Monthly Revenue",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp),
        )

        ChartComponent(
            entries = revenueValues,
            labels = months,
            chartType = ChartType.LINE,
            modifier = Modifier.height(300.dp),
        )
    }
}

@Composable
fun CategoriesScreen(items: List<Item>) {
    val categoryCounts =
        items
            .groupBy { it.category }
            .mapValues { it.value.size.toFloat() }

    val categoryTotals =
        items
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.stock }.toFloat() }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Items by Category",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp),
        )

        ChartComponent(
            entries = categoryCounts.values.toList(),
            labels = categoryCounts.keys.toList(),
            chartType = ChartType.HORIZONTAL_BAR,
            modifier = Modifier.height(250.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Stock by Category",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp),
        )

        ChartComponent(
            entries = categoryTotals.values.toList(),
            labels = categoryTotals.keys.toList(),
            chartType = ChartType.DONUT,
            modifier = Modifier.height(250.dp),
        )
    }
}
