package com.example.smartflowassessment.ui.screens.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartflowassessment.data.model.CategorySummary
import com.example.smartflowassessment.data.model.Item
import com.example.smartflowassessment.data.model.MonthlyTrend
import com.example.smartflowassessment.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repository: ItemRepository,
) : ViewModel() {
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items.asStateFlow()

    private val _monthlyTrends = MutableStateFlow<List<MonthlyTrend>>(emptyList())
    val monthlyTrends: StateFlow<List<MonthlyTrend>> = _monthlyTrends.asStateFlow()

    private val _categoryData = MutableStateFlow<Map<String, CategorySummary>>(emptyMap())
    val categoryData: StateFlow<Map<String, CategorySummary>> = _categoryData.asStateFlow()

    private val _lowStockItems = MutableStateFlow<List<Item>>(emptyList())
    val lowStockItems: StateFlow<List<Item>> = _lowStockItems.asStateFlow()

    private val lowStockThreshold = 10

    init {
        loadItems()
        loadMonthlyTrends()
    }

    private fun loadItems() {
        viewModelScope.launch {
            val itemsList = repository.getItems()
            _items.value = itemsList

            _categoryData.value = itemsList
                .groupBy { it.category }
                .mapValues { entry ->
                    CategorySummary(
                        itemCount = entry.value.size,
                        totalStock = entry.value.sumOf { it.stock },
                        averagePrice = entry.value.map { it.price }.average().toFloat()
                    )
                }

            _lowStockItems.value = itemsList.filter { it.stock < lowStockThreshold }
        }
    }

    private fun loadMonthlyTrends() {
        viewModelScope.launch {
            try {
                _monthlyTrends.value = listOf(
                    MonthlyTrend("Jan", 42, 3500f),
                    MonthlyTrend("Feb", 55, 4200f),
                    MonthlyTrend("Mar", 34, 2800f),
                    MonthlyTrend("Apr", 48, 3900f),
                    MonthlyTrend("May", 66, 5100f),
                    MonthlyTrend("Jun", 52, 4400f),
                    MonthlyTrend("Jul", 59, 4800f),
                    MonthlyTrend("Aug", 62, 5000f)
                )
            } catch (e: Exception) {
                _monthlyTrends.value = emptyList()
            }
        }
    }

    fun refreshData() {
        loadItems()
        loadMonthlyTrends()
    }

    fun searchItems(query: String) {
        viewModelScope.launch {
            val allItems = repository.getItems()
            if (query.isEmpty()) {
                _items.value = allItems
            } else {
                _items.value = allItems.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.category.contains(query, ignoreCase = true)
                }
            }
        }
    }
}
