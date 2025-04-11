package com.example.smartflowassessment.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartflowassessment.data.model.Item
import com.example.smartflowassessment.data.model.SalesDataPoint
import com.example.smartflowassessment.data.repository.ItemRepository
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: ItemRepository,
) : ViewModel() {
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items.asStateFlow()

    private val _chartData = MutableStateFlow<Pair<List<BarEntry>, List<String>>>(Pair(emptyList(), emptyList()))
    val chartData: StateFlow<Pair<List<BarEntry>, List<String>>> = _chartData

    private val _salesData = MutableStateFlow<List<SalesDataPoint>>(emptyList())
    val salesData: StateFlow<List<SalesDataPoint>> = _salesData

    private val _stockByCategory = MutableStateFlow<Pair<List<Float>, List<String>>>(Pair(emptyList(), emptyList()))
    val stockByCategory: StateFlow<Pair<List<Float>, List<String>>> = _stockByCategory

    init {
        loadItems()
        loadSalesData()
    }

    private fun loadSalesData() {
        viewModelScope.launch {
            _salesData.value = listOf(
                SalesDataPoint("Jan", 4500f),
                SalesDataPoint("Feb", 3800f),
                SalesDataPoint("Mar", 5200f),
                SalesDataPoint("Apr", 4900f),
                SalesDataPoint("May", 6100f),
                SalesDataPoint("Jun", 5400f)
            )
        }
    }

    private fun calculateStockByCategory() {
        viewModelScope.launch {
            val items = _items.value
            val categoryMap = items.groupBy { it.category }
                .mapValues { entry -> entry.value.sumOf { it.stock } }

            val totals = categoryMap.values.map { it.toFloat() }
            val labels = categoryMap.keys.toList()

            _stockByCategory.value = Pair(totals, labels)
        }
    }

    private fun loadItems() {
        viewModelScope.launch {
            val result = repository.getItems()
            _items.value = result
            _chartData.value = result.mapIndexed { index, item ->
                BarEntry(index.toFloat(), item.stock.toFloat())
            } to result.map { it.title }
            calculateStockByCategory()
        }
    }
}
