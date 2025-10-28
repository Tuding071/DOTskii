package com.dotify.dotskii

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class HomeScreen : AppCompatActivity() {

    private lateinit var rInput: EditText
    private lateinit var gInput: EditText
    private lateinit var bInput: EditText
    private lateinit var sizeInput: EditText
    private lateinit var toggleButton: Button
    private lateinit var saveButton: Button

    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_home)

        rInput = findViewById(R.id.rInput)
        gInput = findViewById(R.id.gInput)
        bInput = findViewById(R.id.bInput)
        sizeInput = findViewById(R.id.sizeInput)
        toggleButton = findViewById(R.id.toggleButton)
        saveButton = findViewById(R.id.saveButton)

        // Load saved values
        val prefs = getSharedPreferences("dotskii_prefs", MODE_PRIVATE)
        rInput.setText(prefs.getInt("r", 0).toString())
        gInput.setText(prefs.getInt("g", 255).toString())
        bInput.setText(prefs.getInt("b", 255).toString())
        sizeInput.setText(prefs.getInt("size", 10).toString())

        toggleButton.setOnClickListener {
            if (Settings.canDrawOverlays(this)) {
                if (!isRunning) {
                    startService(Intent(this, DotOverlayService::class.java))
                    toggleButton.text = "Stop"
                } else {
                    stopService(Intent(this, DotOverlayService::class.java))
                    toggleButton.text = "Start"
                }
                isRunning = !isRunning
            } else {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
            }
        }

        saveButton.setOnClickListener {
            val rVal = rInput.text.toString().toIntOrNull() ?: 0
            val gVal = gInput.text.toString().toIntOrNull() ?: 255
            val bVal = bInput.text.toString().toIntOrNull() ?: 255
            val sizeVal = sizeInput.text.toString().toIntOrNull() ?: 10

            val prefsEditor = getSharedPreferences("dotskii_prefs", MODE_PRIVATE).edit()
            prefsEditor.putInt("r", rVal)
            prefsEditor.putInt("g", gVal)
            prefsEditor.putInt("b", bVal)
            prefsEditor.putInt("size", sizeVal)
            prefsEditor.apply()

            // Update overlay immediately if running
            if (isRunning) {
                val color = (0xFF shl 24) or (rVal shl 16) or (gVal shl 8) or bVal
                Intent(this, DotOverlayService::class.java).also { intent ->
                    intent.putExtra("updateColor", color)
                    intent.putExtra("updateSize", sizeVal)
                    startService(intent)
                }
            }
        }
    }
}
