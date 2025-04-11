package com.example.smartflowassessment.ui.screens.itemedit

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartflowassessment.data.model.Item
import com.example.smartflowassessment.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemEditViewModel
    @Inject
    constructor(
        private val repository: ItemRepository,
    ) : ViewModel() {
        var name = mutableStateOf("")
        var description = mutableStateOf("")
        var quantity = mutableStateOf("")
        var price = mutableStateOf("")
        var supplier = mutableStateOf("")
        var category = mutableStateOf("")
        var imageUrl = mutableStateOf("")
        var isEditMode = false
        private var existingItemId: Int? = null

        fun loadItem(id: Int) {
            viewModelScope.launch {
                val item = repository.getItemById(id)
                item?.let {
                    existingItemId = it.id
                    name.value = it.title
                    description.value = it.description
                    quantity.value = it.stock.toString()
                    price.value = it.price.toString()
                    supplier.value = it.brand.toString()
                    category.value = it.category
                    imageUrl.value = it.imageUrl
                    isEditMode = true
                }
            }
        }

        fun saveItem(onDone: () -> Unit) {
            viewModelScope.launch {
                val item =
                    Item(
                        id = existingItemId ?: 0,
                        title = name.value,
                        description = description.value,
                        stock = quantity.value.toIntOrNull() ?: 0,
                        price = price.value.toDoubleOrNull() ?: 0.0,
                        brand = supplier.value,
                        category = category.value,
                        imageUrl = imageUrl.value,
                    )
                repository.insertItem(item)
                Log.d("ItemEdit", "Saved item: $item")
                onDone()
            }
        }
    }
