package com.example.smartflowassessment.ui.screens.itemedit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.smartflowassessment.ui.components.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEditScreen(
    navController: NavController,
    itemId: Int?,
    viewModel: ItemEditViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()

    val name = viewModel.name.value
    val description = viewModel.description.value
    val quantity = viewModel.quantity.value
    val price = viewModel.price.value
    val supplier = viewModel.supplier.value
    val category = viewModel.category.value
    val imageUrl = viewModel.imageUrl.value

    LaunchedEffect(itemId) {
        itemId?.let { viewModel.loadItem(it) }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = if (viewModel.isEditMode) "Edit Item" else "Add Item",
                showBack = true,
                onBackClick = { navController.popBackStack() },
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
                    .padding(16.dp),
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { viewModel.name.value = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = description,
                onValueChange = { viewModel.description.value = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = quantity,
                onValueChange = { viewModel.quantity.value = it },
                label = { Text("Quantity") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = price,
                onValueChange = { viewModel.price.value = it },
                label = { Text("Price") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = supplier,
                onValueChange = { viewModel.supplier.value = it },
                label = { Text("Supplier") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = category,
                onValueChange = { viewModel.category.value = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { viewModel.imageUrl.value = it },
                label = { Text("Image") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.saveItem { navController.popBackStack() }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(52.dp)
                    .fillMaxWidth(),
            ) {
                Text("Save")
            }
        }
    }
}
