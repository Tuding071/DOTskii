package com.dotify.dotskii

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.WindowManager

class DotOverlayService : Service() {

    private var dotView: DotView? = null
    private var windowManager: WindowManager? = null

    companion object {
        var isRunning = false
    }

    override fun onCreate() {
        super.onCreate()
        isRunning = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (dotView == null) {
            createOverlay()
        }

        intent?.let {
            if (it.hasExtra("updateColor") && it.hasExtra("updateSize")) {
                val color = it.getIntExtra("updateColor", 0xFF00FFFF.toInt())
                val size = it.getIntExtra("updateSize", 10)
                updateDot(color, size)
            }
        }

        return START_STICKY
    }

    private fun createOverlay() {
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // Get display size
        val metrics = DisplayMetrics()
        windowManager?.defaultDisplay?.getRealMetrics(metrics)
        val screenWidth = metrics.widthPixels
        val screenHeight = metrics.heightPixels

        // Read prefs
        val prefs = getSharedPreferences("dotskii_prefs", Context.MODE_PRIVATE)
        val r = prefs.getInt("r", 0)
        val g = prefs.getInt("g", 255)
        val b = prefs.getInt("b", 255)
        val size = prefs.getInt("size", 10)

        dotView = DotView(this).apply {
            setScreenSize(screenWidth, screenHeight)
            updateColor((0xFF shl 24) or (r shl 16) or (g shl 8) or b)
            updateSize(size)
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        
        // Position the dot in the absolute center of screen
        params.gravity = Gravity.CENTER
        params.x = 0
        params.y = 0

        windowManager?.addView(dotView, params)
    }

    private fun updateDot(color: Int, size: Int) {
        dotView?.let {
            it.updateColor(color)
            it.updateSize(size)
            it.invalidate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        dotView?.let { 
            windowManager?.removeView(it)
        }
        dotView = null
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
