package com.example.smartflowassessment.ui.components

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun OfflineIndicator() {
    val context = LocalContext.current
    val isOnline =
        remember {
            mutableStateOf(checkInternet(context.getSystemService(ConnectivityManager::class.java)))
        }

    if (!isOnline.value) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(Color.Red)
                    .padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text("Offline Mode", color = Color.White, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

fun checkInternet(cm: ConnectivityManager?): Boolean {
    cm ?: return false
    val network = cm.activeNetwork ?: return false
    val capabilities = cm.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
