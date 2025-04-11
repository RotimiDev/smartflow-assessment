package com.example.smartflowassessment.ui.screens.itemdetail

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
class ItemDetailViewModel
    @Inject
    constructor(
        private val repository: ItemRepository,
    ) : ViewModel() {
        private val _item = MutableStateFlow<Item?>(null)
        val item: StateFlow<Item?> = _item.asStateFlow()

        fun loadItem(id: Int) {
            viewModelScope.launch {
                _item.value = repository.getItemById(id)
            }
        }

        fun deleteItem(
            onDeleted: () -> Unit,
            onError: () -> Unit = {},
        ) {
            viewModelScope.launch {
                val deleted = repository.deleteItem(item.value ?: return@launch)
                if (deleted) {
                    onDeleted()
                } else {
                    onError()
                }
            }
        }
    }
