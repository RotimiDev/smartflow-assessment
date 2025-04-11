package com.example.smartflowassessment.ui.screens.itemedit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartflowassessment.data.model.FormState
import com.example.smartflowassessment.data.model.Item
import com.example.smartflowassessment.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemEditViewModel
    @Inject
    constructor(
        private val repository: ItemRepository,
    ) : ViewModel() {
        private val _name = MutableStateFlow("")
        val name = _name.asStateFlow()

        private val _description = MutableStateFlow("")
        val description = _description.asStateFlow()

        private val _quantity = MutableStateFlow("")
        val quantity = _quantity.asStateFlow()

        private val _price = MutableStateFlow("")
        val price = _price.asStateFlow()

        private val _supplier = MutableStateFlow("")
        val supplier = _supplier.asStateFlow()

        private val _category = MutableStateFlow("")
        val category = _category.asStateFlow()

        private val _imageUrl = MutableStateFlow("")
        val imageUrl = _imageUrl.asStateFlow()
        private val _nameError = MutableStateFlow<String?>(null)
        val nameError = _nameError.asStateFlow()

        private val _priceError = MutableStateFlow<String?>(null)
        val priceError = _priceError.asStateFlow()

        private val _quantityError = MutableStateFlow<String?>(null)
        val quantityError = _quantityError.asStateFlow()
        private val _isLoading = MutableStateFlow(false)
        val isLoading = _isLoading.asStateFlow()

        private val _formState = MutableStateFlow<FormState>(FormState.Idle)
        val formState = _formState.asStateFlow()
        var categoryDropdownExpanded = false
        var showDiscardDialog = false
        private var originalValues = ItemFormValues()

        var isEditMode = false
            private set

        private var existingItemId: Int? = null

        fun updateName(value: String) {
            _name.value = value
            validateName()
        }

        fun updateDescription(value: String) {
            _description.value = value
        }

        fun updateQuantity(value: String) {
            _quantity.value = value
            validateQuantity()
        }

        fun updatePrice(value: String) {
            _price.value = value
            validatePrice()
        }

        fun updateSupplier(value: String) {
            _supplier.value = value
        }

        fun updateCategory(value: String) {
            _category.value = value
        }

        fun updateImageUrl(value: String) {
            _imageUrl.value = value
        }

        private fun validateName() {
            _nameError.value =
                when {
                    _name.value.isBlank() -> "Name is required"
                    _name.value.length < 3 -> "Name must be at least 3 characters"
                    else -> null
                }
        }

        private fun validatePrice() {
            _priceError.value =
                when {
                    _price.value.isBlank() -> "Price is required"
                    _price.value.toDoubleOrNull() == null -> "Price must be a valid number"
                    _price.value.toDoubleOrNull()!! < 0 -> "Price cannot be negative"
                    else -> null
                }
        }

        private fun validateQuantity() {
            _quantityError.value =
                when {
                    _quantity.value.isBlank() -> "Quantity is required"
                    _quantity.value.toIntOrNull() == null -> "Quantity must be a valid number"
                    _quantity.value.toIntOrNull()!! < 0 -> "Quantity cannot be negative"
                    else -> null
                }
        }

        private fun validateForm(): Boolean {
            validateName()
            validatePrice()
            validateQuantity()

            return nameError.value == null &&
                priceError.value == null &&
                quantityError.value == null
        }

        fun loadItem(id: Int) {
            viewModelScope.launch {
                _isLoading.value = true
                try {
                    val item = repository.getItemById(id)
                    item?.let {
                        existingItemId = it.id
                        _name.value = it.title
                        _description.value = it.description
                        _quantity.value = it.stock.toString()
                        _price.value = it.price.toString()
                        _supplier.value = it.brand ?: ""
                        _category.value = it.category
                        _imageUrl.value = it.imageUrl
                        isEditMode = true

                        saveOriginalValues()
                    }
                } catch (e: Exception) {
                    Log.e("ItemEditViewModel", "Error loading item", e)
                    _formState.value = FormState.Error("Failed to load item: ${e.message}")
                } finally {
                    _isLoading.value = false
                }
            }
        }

        fun saveItem() {
            if (!validateForm()) {
                return
            }

            viewModelScope.launch {
                _isLoading.value = true
                try {
                    val item =
                        Item(
                            id = existingItemId ?: 0,
                            title = name.value,
                            description = description.value,
                            stock = quantity.value.toIntOrNull() ?: 0,
                            price = price.value.toDoubleOrNull() ?: 0.0,
                            brand = supplier.value.takeIf { it.isNotBlank() },
                            category = category.value,
                            imageUrl = imageUrl.value,
                            updatedAt = System.currentTimeMillis(),
                        )

                    repository.insertItem(item)
                    _formState.value = FormState.Success
                    Log.d("ItemEdit", "Saved item: $item")
                } catch (e: Exception) {
                    Log.e("ItemEditViewModel", "Error saving item", e)
                    _formState.value = FormState.Error("Failed to save item: ${e.message}")
                } finally {
                    _isLoading.value = false
                }
            }
        }

        fun resetForm() {
            _name.value = originalValues.name
            _description.value = originalValues.description
            _quantity.value = originalValues.quantity
            _price.value = originalValues.price
            _supplier.value = originalValues.supplier
            _category.value = originalValues.category
            _imageUrl.value = originalValues.imageUrl
            _nameError.value = null
            _priceError.value = null
            _quantityError.value = null
        }

        private fun saveOriginalValues() {
            originalValues =
                ItemFormValues(
                    name = _name.value,
                    description = _description.value,
                    quantity = _quantity.value,
                    price = _price.value,
                    supplier = _supplier.value,
                    category = _category.value,
                    imageUrl = _imageUrl.value,
                )
        }

        fun hasChanges(): Boolean =
            originalValues.name != _name.value ||
                originalValues.description != _description.value ||
                originalValues.quantity != _quantity.value ||
                originalValues.price != _price.value ||
                originalValues.supplier != _supplier.value ||
                originalValues.category != _category.value ||
                originalValues.imageUrl != _imageUrl.value

        private data class ItemFormValues(
            val name: String = "",
            val description: String = "",
            val quantity: String = "",
            val price: String = "",
            val supplier: String = "",
            val category: String = "",
            val imageUrl: String = "",
        )
    }
