package com.example.smartflowassessment.ui.screens.itemedit

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.smartflowassessment.data.model.FormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEditScreen(
    navController: NavController,
    itemId: Int?,
    viewModel: ItemEditViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val name = viewModel.name.collectAsState()
    val description = viewModel.description.collectAsState()
    val quantity = viewModel.quantity.collectAsState()
    val price = viewModel.price.collectAsState()
    val supplier = viewModel.supplier.collectAsState()
    val category = viewModel.category.collectAsState()
    val imageUrl = viewModel.imageUrl.collectAsState()
    val nameError = viewModel.nameError.collectAsState()
    val priceError = viewModel.priceError.collectAsState()
    val quantityError = viewModel.quantityError.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val formState = viewModel.formState.collectAsState()

    LaunchedEffect(itemId) {
        itemId?.let {
            viewModel.loadItem(it)
        }
    }

    LaunchedEffect(formState.value) {
        when (val state = formState.value) {
            is FormState.Success -> {
                Toast
                    .makeText(
                        context,
                        "Item saved successfully",
                        Toast.LENGTH_SHORT,
                    ).show()
                navController.popBackStack()
            }

            is FormState.Error -> {
                Toast
                    .makeText(
                        context,
                        state.message ?: "Failed to save item",
                        Toast.LENGTH_SHORT,
                    ).show()
            }

            else -> {}
        }
    }

    BackHandler {
        if (viewModel.hasChanges()) {
            viewModel.showDiscardDialog = true
        } else {
            navController.popBackStack()
        }
    }

    if (viewModel.showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showDiscardDialog = false },
            title = { Text("Discard changes?") },
            text = { Text("You have unsaved changes. Are you sure you want to discard them?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.showDiscardDialog = false
                    navController.popBackStack()
                }) {
                    Text("Discard")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showDiscardDialog = false }) {
                    Text("Keep editing")
                }
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (viewModel.isEditMode) "Edit Item" else "Add Item",
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (viewModel.hasChanges()) {
                            viewModel.showDiscardDialog = true
                        } else {
                            navController.popBackStack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                actions = {
                    if (viewModel.isEditMode) {
                        IconButton(onClick = { viewModel.resetForm() }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset form",
                            )
                        }
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text(
                            text = "Item Information",
                            style = MaterialTheme.typography.titleMedium,
                        )

                        OutlinedTextField(
                            value = name.value,
                            onValueChange = { viewModel.updateName(it) },
                            label = { Text("Name") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = nameError.value != null,
                            supportingText = {
                                nameError.value?.let {
                                    Text(it, color = MaterialTheme.colorScheme.error)
                                }
                            },
                            singleLine = true,
                            keyboardOptions =
                                KeyboardOptions(
                                    imeAction = ImeAction.Next,
                                ),
                            keyboardActions =
                                KeyboardActions(
                                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                                ),
                            trailingIcon = {
                                if (name.value.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.updateName("") }) {
                                        Icon(Icons.Filled.Clear, "Clear name")
                                    }
                                }
                            },
                        )
                        OutlinedTextField(
                            value = description.value,
                            onValueChange = { viewModel.updateDescription(it) },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = nameError.value != null,
                            supportingText = {
                                nameError.value?.let {
                                    Text(it, color = MaterialTheme.colorScheme.error)
                                }
                            },
                            singleLine = true,
                            keyboardOptions =
                                KeyboardOptions(
                                    imeAction = ImeAction.Next,
                                ),
                            keyboardActions =
                                KeyboardActions(
                                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                                ),
                            trailingIcon = {
                                if (name.value.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.updateDescription("") }) {
                                        Icon(Icons.Filled.Clear, "Clear description")
                                    }
                                }
                            },
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text(
                            text = "Pricing & Stock",
                            style = MaterialTheme.typography.titleMedium,
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            OutlinedTextField(
                                value = price.value,
                                onValueChange = { viewModel.updatePrice(it) },
                                label = { Text("Price") },
                                modifier = Modifier.weight(1f),
                                isError = priceError.value != null,
                                supportingText = {
                                    priceError.value?.let {
                                        Text(it, color = MaterialTheme.colorScheme.error)
                                    }
                                },
                                leadingIcon = { Text("₦") },
                                singleLine = true,
                                keyboardOptions =
                                    KeyboardOptions(
                                        keyboardType = KeyboardType.Decimal,
                                        imeAction = ImeAction.Next,
                                    ),
                                keyboardActions =
                                    KeyboardActions(
                                        onNext = { focusManager.moveFocus(FocusDirection.Right) },
                                    ),
                                trailingIcon = {
                                    if (price.value.isNotEmpty()) {
                                        IconButton(onClick = { viewModel.updatePrice("") }) {
                                            Icon(Icons.Filled.Clear, "Clear price")
                                        }
                                    }
                                },
                            )

                            OutlinedTextField(
                                value = quantity.value,
                                onValueChange = { viewModel.updateQuantity(it) },
                                label = { Text("Quantity") },
                                modifier = Modifier.weight(1f),
                                isError = quantityError.value != null,
                                supportingText = {
                                    quantityError.value?.let {
                                        Text(it, color = MaterialTheme.colorScheme.error)
                                    }
                                },
                                singleLine = true,
                                keyboardOptions =
                                    KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Next,
                                    ),
                                keyboardActions =
                                    KeyboardActions(
                                        onNext = { focusManager.moveFocus(FocusDirection.Down) },
                                    ),
                                trailingIcon = {
                                    if (quantity.value.isNotEmpty()) {
                                        IconButton(onClick = { viewModel.updateQuantity("") }) {
                                            Icon(Icons.Filled.Clear, "Clear quantity")
                                        }
                                    }
                                },
                            )
                        }
                    }
                }

                // Categorization Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text(
                            text = "Categorization",
                            style = MaterialTheme.typography.titleMedium,
                        )

                        OutlinedTextField(
                            value = supplier.value,
                            onValueChange = { viewModel.updateSupplier(it) },
                            label = { Text("Supplier") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions =
                                KeyboardOptions(
                                    imeAction = ImeAction.Next,
                                ),
                            keyboardActions =
                                KeyboardActions(
                                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                                ),
                            trailingIcon = {
                                if (supplier.value.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.updateSupplier("") }) {
                                        Icon(Icons.Filled.Clear, "Clear supplier")
                                    }
                                }
                            },
                        )

                        val categories =
                            remember {
                                listOf("Electronics", "Clothing", "Food", "Books", "Toys", "Other")
                            }

                        ExposedDropdownMenuBox(
                            expanded = viewModel.categoryDropdownExpanded,
                            onExpandedChange = { viewModel.categoryDropdownExpanded = it },
                        ) {
                            OutlinedTextField(
                                value = category.value,
                                onValueChange = { viewModel.updateCategory(it) },
                                label = { Text("Category") },
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = viewModel.categoryDropdownExpanded,
                                    )
                                },
                                singleLine = true,
                                readOnly = false,
                            )

                            ExposedDropdownMenu(
                                expanded = viewModel.categoryDropdownExpanded,
                                onDismissRequest = { viewModel.categoryDropdownExpanded = false },
                            ) {
                                categories.forEach { option ->
                                    DropdownMenuItem(
                                        onClick = {
                                            viewModel.updateCategory(option)
                                            viewModel.categoryDropdownExpanded = false
                                        },
                                        text = { Text(option) },
                                    )
                                }
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text(
                            text = "Media",
                            style = MaterialTheme.typography.titleMedium,
                        )

                        OutlinedTextField(
                            value = imageUrl.value,
                            onValueChange = { viewModel.updateImageUrl(it) },
                            label = { Text("Image URL") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions =
                                KeyboardOptions(
                                    keyboardType = KeyboardType.Uri,
                                    imeAction = ImeAction.Done,
                                ),
                            keyboardActions =
                                KeyboardActions(
                                    onDone = {
                                        focusManager.clearFocus()
                                        keyboardController?.hide()
                                    },
                                ),
                            trailingIcon = {
                                if (imageUrl.value.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.updateImageUrl("") }) {
                                        Icon(Icons.Filled.Clear, "Clear image URL")
                                    }
                                }
                            },
                        )

                        if (imageUrl.value.isNotEmpty()) {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center,
                            ) {
                                Image(
                                    painter =
                                        rememberAsyncImagePainter(
                                            model = imageUrl.value,
                                        ),
                                    contentDescription = "Item image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit,
                                )
                            }
                        }
                    }
                }

                // Submit Button for accessibility (in addition to FAB)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        viewModel.saveItem()
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier =
                        Modifier
                            .height(52.dp)
                            .fillMaxWidth(),
                    enabled = !isLoading.value,
                ) {
                    if (isLoading.value) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    } else {
                        Text("Save")
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }

            if (isLoading.value) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
