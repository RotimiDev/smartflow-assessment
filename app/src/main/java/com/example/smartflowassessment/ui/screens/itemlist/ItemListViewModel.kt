package com.example.smartflowassessment.ui.screens.itemlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartflowassessment.data.model.Item
import com.example.smartflowassessment.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemListViewModel
    @Inject
    constructor(
        private val repository: ItemRepository,
    ) : ViewModel() {
        private val _items = MutableStateFlow<List<Item>>(emptyList())
        val items: StateFlow<List<Item>> = _items.asStateFlow()
        private val _isLoading = MutableStateFlow(false)
        val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

        private val _error = MutableStateFlow<String?>(null)
        val error: StateFlow<String?> = _error.asStateFlow()

        init {
            loadItems()
        }

        private fun loadItems() {
            viewModelScope.launch {
                _isLoading.value = true
                _error.value = null

                try {
                    val fetchedItems = repository.getItems()
                    _items.value = fetchedItems
                } catch (e: Exception) {
                    _error.value = "Failed to load items: ${e.message}"
                } finally {
                    _isLoading.value = false
                }
            }
        }

        fun refreshItems() {
            loadItems()
        }
    }
