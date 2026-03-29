package com.example.kotlin_5_prak

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.core.app.ActivityCompat

class MainActivity : ComponentActivity() {

    private val viewModel: PhotoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
        setContent {
            MaterialTheme {
                PhotoGalleryScreen(viewModel)
            }
        }
    }
}