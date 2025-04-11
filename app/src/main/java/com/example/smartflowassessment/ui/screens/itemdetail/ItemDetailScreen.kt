package com.example.smartflowassessment.ui.screens.itemdetail

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.smartflowassessment.ui.components.TopBar
import com.example.smartflowassessment.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    navController: NavController,
    itemId: Int,
    viewModel: ItemDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(true) {
        viewModel.loadItem(itemId)
    }

    val item by viewModel.item.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBar(
                title = "Item Details",
                showBack = true,
                onBackClick = { navController.popBackStack() },
            )
        },
    ) { padding ->
        item?.let {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
            ) {
                Text(it.title, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Description: ${it.description}")
                Text("Stock: ${it.stock}")
                Text("Price: ₦${it.price}")
                Text("Brand: ${it.brand ?: "N/A"}")
                Text("Category: ${it.category}")
                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        navController.navigate(Screen.ItemEdit.route + "?itemId=${it.id}")
                    }) {
                        Text("Edit")
                    }

                    OutlinedButton(
                        onClick = {
                            viewModel.deleteItem(
                                onDeleted = { navController.popBackStack() },
                                onError = {
                                    Toast
                                        .makeText(
                                            context,
                                            "Failed to delete",
                                            Toast.LENGTH_SHORT,
                                        ).show()
                                },
                            )
                        },
                    ) {
                        Text("Delete")
                    }
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
