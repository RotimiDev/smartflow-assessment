package com.example.smartflowassessment.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartflowassessment.data.model.Item
import com.example.smartflowassessment.data.repository.ItemRepository
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel
    @Inject
    constructor(
        private val repository: ItemRepository,
    ) : ViewModel() {
        private val _items = MutableStateFlow<List<Item>>(emptyList())
        val items: StateFlow<List<Item>> = _items.asStateFlow()

        private val _chartData =
            MutableStateFlow<Pair<List<BarEntry>, List<String>>>(emptyList<BarEntry>() to emptyList())
        val chartData: StateFlow<Pair<List<BarEntry>, List<String>>> = _chartData.asStateFlow()

        init {
            loadItems()
        }

        private fun loadItems() {
            viewModelScope.launch {
                val result = repository.getItems()
                _items.value = result
                _chartData.value = result.mapIndexed { index, item ->
                    BarEntry(index.toFloat(), item.stock.toFloat())
                } to result.map { it.title }
            }
        }
    }
