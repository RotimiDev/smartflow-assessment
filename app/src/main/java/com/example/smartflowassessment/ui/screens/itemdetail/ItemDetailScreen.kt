package com.example.smartflowassessment.ui.screens.itemdetail

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.smartflowassessment.data.model.Item
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
        floatingActionButton = {
            item?.let {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.ItemEdit.route + "?itemId=${it.id}") },
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                }
            }
        },
    ) { padding ->
        when (item) {
            null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                ItemDetails(
                    item = item!!,
                    modifier = Modifier.padding(padding),
                    onDeleteClick = {
                        viewModel.deleteItem(
                            onDeleted = { navController.popBackStack() },
                            onError = {
                                Toast
                                    .makeText(
                                        context,
                                        "Failed to delete item",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                            },
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun ItemDetails(
    item: Item,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(16.dp))

                ItemInfoRow(
                    label = "Description",
                    value = item.description,
                    multiLine = true,
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    ItemInfoRow(
                        label = "Price",
                        value = "₦${item.price}",
                        modifier = Modifier.weight(1f),
                    )

                    ItemInfoRow(
                        label = "Stock",
                        value = "${item.stock} units",
                        modifier = Modifier.weight(1f),
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    ItemInfoRow(
                        label = "Brand",
                        value = item.brand ?: "N/A",
                        modifier = Modifier.weight(1f),
                    )

                    ItemInfoRow(
                        label = "Category",
                        value = item.category,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onDeleteClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete icon",
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Delete Item")
            }
        }
    }
}

@Composable
private fun ItemInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    multiLine: Boolean = false,
) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = if (multiLine) Int.MAX_VALUE else 1,
            overflow = if (multiLine) TextOverflow.Visible else TextOverflow.Ellipsis,
        )
    }
}
