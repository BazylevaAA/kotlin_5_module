package com.example.kotlin_5_prak

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class DiaryViewModel(app: Application) : AndroidViewModel(app) {

    private val filesDir = app.filesDir
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    // Список записей
    private val _entries = MutableStateFlow<List<DiaryEntry>>(emptyList())
    val entries = _entries.asStateFlow()

    init {
        // Грузим все записи один раз при старте
        loadAllEntries()
    }

    // Сканируем filesDir и строим список только при запуске
    private fun loadAllEntries() {
        val files = filesDir.listFiles { f -> f.extension == "txt" } ?: return
        _entries.value = files
            .sortedByDescending { it.lastModified() }
            .map { it.toEntry() }
    }

    // Сохранить новую запись (или перезаписать существующую)
    fun save(fileName: String?, title: String, body: String): String {
        // Если fileName = null — новая запись, иначе редактируем
        val name = fileName ?: "diary_${System.currentTimeMillis()}.txt"
        val file = File(filesDir, name)

        // Формат файла
        file.writeText("$title\n$body")

        val newEntry = file.toEntry()

        if (fileName == null) {
            // Добавляем в начало списка
            _entries.value = listOf(newEntry) + _entries.value
        } else {
            // Обновляем
            _entries.value = _entries.value.map { if (it.fileName == name) newEntry else it }
        }

        return name
    }

    // Удаляем файл и убираем из списка по имени — без пересканирования
    fun delete(fileName: String) {
        File(filesDir, fileName).delete()
        _entries.value = _entries.value.filter { it.fileName != fileName }
    }

    // Читаем полное содержимое записи для экрана редактирования
    fun readEntry(fileName: String): Pair<String, String> {
        val lines = File(filesDir, fileName).readLines()
        val title = lines.firstOrNull() ?: ""
        val body = lines.drop(1).joinToString("\n")
        return Pair(title, body)
    }

    // Конвертируем
    private fun File.toEntry(): DiaryEntry {
        val lines = readLines()
        val title = lines.firstOrNull() ?: "Без заголовка"
        val body = lines.drop(1).joinToString(" ")
        return DiaryEntry(
            fileName = name,
            title = title.ifBlank { "Без заголовка" },
            preview = body.take(40).let { if (body.length > 40) "$it..." else it },
            date = dateFormat.format(Date(lastModified()))
        )
    }
}