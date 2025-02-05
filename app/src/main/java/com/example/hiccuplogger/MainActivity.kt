package com.example.hiccuplogger

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

import android.content.ContentValues
import android.os.Environment
import android.provider.MediaStore
import java.io.OutputStream

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MyApp() }
    }
}

@Composable
fun MyApp() {
    val context = LocalContext.current
    var logText by remember { mutableStateOf("") }
    var showLog by remember { mutableStateOf(false) } // Controls log visibility
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Button to Record Hiccup
        Button(onClick = {
            recordEntry(context, "h") // Record as hiccup
            logText = readHiccupLog(context) // Update log
        }) {
            Text("Record Hiccup")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to Record Meal
        Button(onClick = {
            recordEntry(context, "m") // Record as meal
            logText = readHiccupLog(context) // Update log
        }) {
            Text("Record Meal")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TextField for User Input
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Enter text to import") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Import Button (Append Input Text to Log)
        Button(onClick = {
            importTextToLog(context, inputText)
            logText = readHiccupLog(context) // Update log after import
            inputText = "" // Clear input
        }) {
            Text("Import Text")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Export Button (Save Log to Downloads)
        Button(onClick = { exportHiccupLog(context) }) {
            Text("Export Log")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Delete Last Line Button
        Button(onClick = {
            deleteLastLineFromLog(context)
            logText = readHiccupLog(context) // Update log after deletion
        }) {
            Text("Delete Last Line")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show Log Button
        Button(onClick = {
            logText = readHiccupLog(context) // Read the log when clicked
            showLog = !showLog // Toggle visibility
        }) {
            Text(if (showLog) "Hide Log" else "Show Log")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Conditionally Display Scrollable Log
        if (showLog) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Set a fixed height for scrolling
                    .verticalScroll(rememberScrollState()) // Enable scrolling
                    .padding(8.dp)
            ) {
                Text(text = logText)
            }
        }
    }
}

// Function to record hiccups
fun recordEntry(context: Context, entryType: String) {
    val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    val prefix = when (entryType) {
        "h" -> "h "  // Hiccup entry
        "m" -> "m "  // Meal entry
        else -> ""    // Fallback, should not happen
    }

    val logFile = File(context.filesDir, "hiccup_log.txt")
    logFile.appendText("$prefix$timestamp\n") // Append entry with correct prefix

    val toastMessage = if (entryType == "h") "Hiccup recorded!" else "Meal recorded!"
    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
}


fun exportHiccupLog(context: Context) {
    val logFile = File(context.filesDir, "hiccup_log.txt")
    if (!logFile.exists()) {
        Toast.makeText(context, "No log file found!", Toast.LENGTH_SHORT).show()
        return
    }

    // Get current date and time for file name suffix
    val timestamp = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(Date())
    val fileName = "hiccup_log_$timestamp.txt" // Example: hiccup_log_2024-02-05_15-30-00.txt
    val fileContents = logFile.readText()

    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }

    val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
    uri?.let {
        try {
            val outputStream: OutputStream? = resolver.openOutputStream(it)
            outputStream?.write(fileContents.toByteArray())
            outputStream?.close()
            Toast.makeText(context, "Log exported as $fileName in Downloads!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Export failed", Toast.LENGTH_SHORT).show()
        }
    } ?: Toast.makeText(context, "Could not create file", Toast.LENGTH_SHORT).show()
}

fun importTextToLog(context: Context, text: String) {
    if (text.isBlank()) {
        Toast.makeText(context, "Input cannot be empty!", Toast.LENGTH_SHORT).show()
        return
    }

    val logFile = File(context.filesDir, "hiccup_log.txt")
    logFile.appendText("$text\n") // Append the input text

    Toast.makeText(context, "Text imported successfully!", Toast.LENGTH_SHORT).show()
}

fun readHiccupLog(context: Context): String {
    val file = File(context.filesDir, "hiccup_log.txt")
    return if (file.exists()) file.readText() else "No records yet."
}

fun deleteLastLineFromLog(context: Context) {
    val logFile = File(context.filesDir, "hiccup_log.txt")
    if (!logFile.exists()) {
        Toast.makeText(context, "No log file found!", Toast.LENGTH_SHORT).show()
        return
    }

    val lines = logFile.readLines()
    if (lines.isEmpty()) {
        Toast.makeText(context, "Log file is already empty!", Toast.LENGTH_SHORT).show()
        return
    }

    val updatedContent = lines.dropLast(1).joinToString("\n") + "\n" // Remove last line
    logFile.writeText(updatedContent) // Write updated content back

    Toast.makeText(context, "Last entry deleted!", Toast.LENGTH_SHORT).show()
}