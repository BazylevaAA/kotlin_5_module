package com.example.kotlin_5_prak

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PhotoViewModel(app: Application) : AndroidViewModel(app) {

    // Папка для хранения фото приложения
    private val photosDir = app.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    private val _photos = MutableStateFlow<List<File>>(emptyList())
    val photos = _photos.asStateFlow()

    // Snackbar сообщение
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()

    init {
        loadPhotos()
    }

    // Сканируем папку только при запуске и после добавления нового фото
    private fun loadPhotos() {
        val files = photosDir?.listFiles { f -> f.extension == "jpg" } ?: return
        _photos.value = files.sortedByDescending { it.lastModified() }
    }

    //файл для новой фотографии
    fun createPhotoFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return File(photosDir, "IMG_$timestamp.jpg")
    }
    fun onPhotoCaptured(file: File) {
        if (file.exists()) {
            _photos.value = listOf(file) + _photos.value
        }
    }

    // в MediaStore
    fun exportToGallery(context: Context, file: File) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyPhotoApp")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ) ?: run {
            _snackbarMessage.value = "Ошибка экспорта"
            return
        }

        context.contentResolver.openOutputStream(uri)?.use { output ->
            file.inputStream().use { input -> input.copyTo(output) }
        }

        contentValues.clear()
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
        context.contentResolver.update(uri, contentValues, null, null)

        _snackbarMessage.value = "Фото добавлено в галерею"
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}