package com.example.kotlin_5_prak

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoGalleryScreen(viewModel: PhotoViewModel) {
    val context = LocalContext.current
    val photos by viewModel.photos.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Файл для текущего фото
    var currentPhotoFile by remember { mutableStateOf<File?>(null) }

    // Лаунчер камеры
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            currentPhotoFile?.let { viewModel.onPhotoCaptured(it) }
        }
    }


    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    fun openCamera() {
        val file = viewModel.createPhotoFile()
        currentPhotoFile = file
        // FileProvider нужен чтобы передать URI камере без прямого доступа к файлу
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        cameraLauncher.launch(uri)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Мои фото") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { openCamera() }) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Сделать фото")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        if (photos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("У вас пока нет фото", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { openCamera() }) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Сделать первое фото")
                    }
                }
            }
        } else {
            // Сетка фото
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(photos, key = { it.absolutePath }) { file ->
                    PhotoItem(
                        file = file,
                        onExport = { viewModel.exportToGallery(context, file) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PhotoItem(file: File, onExport: () -> Unit) {
    var menuExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.aspectRatio(1f)) {
        // Загружаем фото через Coil
        Image(
            painter = rememberAsyncImagePainter(file),
            contentDescription = file.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Кнопка меню
        Box(modifier = Modifier.align(Alignment.TopEnd)) {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "Меню",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Экспорт в галерею") },
                    onClick = {
                        menuExpanded = false
                        onExport()
                    }
                )
            }
        }
    }
}