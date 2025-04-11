package com.example.smartflowassessment.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.smartflowassessment.ui.navigation.AppNavHost
import com.example.smartflowassessment.ui.theme.SmartFlowAssessmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartFlowAssessmentTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}
