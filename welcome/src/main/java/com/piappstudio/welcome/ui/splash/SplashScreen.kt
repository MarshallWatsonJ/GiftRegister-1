package com.piappstudio.welcome.ui.splash

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen() {
    Scaffold(modifier = Modifier
        .fillMaxSize()) {
        LazyColumn(contentPadding = it) {
            item {
                Text(text = "Welcome to splash", style = MaterialTheme.typography.headlineLarge)
            }

        }
    }
}