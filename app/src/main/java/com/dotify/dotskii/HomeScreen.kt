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
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName"))
                startActivity(intent)
            }
        }

        saveButton.setOnClickListener {
            val prefs = getSharedPreferences("dotskii_prefs", MODE_PRIVATE).edit()
            prefs.putInt("r", rInput.text.toString().toIntOrNull() ?: 0)
            prefs.putInt("g", gInput.text.toString().toIntOrNull() ?: 255)
            prefs.putInt("b", bInput.text.toString().toIntOrNull() ?: 255)
            prefs.putInt("size", sizeInput.text.toString().toIntOrNull() ?: 10)
            prefs.apply()
        }
    }
}
