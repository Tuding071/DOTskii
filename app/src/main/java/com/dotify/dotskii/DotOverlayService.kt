package com.dotify.dotskii

import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager

class DotOverlayService : Service() {

    private lateinit var dotView: DotView
    private lateinit var windowManager: WindowManager
    private lateinit var prefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        prefs = getSharedPreferences("dotskii_prefs", MODE_PRIVATE)

        dotView = DotView(this)
        applyPreferences()

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.CENTER

        windowManager.addView(dotView, params)
    }

    private fun applyPreferences() {
        val r = prefs.getInt("r", 0)
        val g = prefs.getInt("g", 255)
        val b = prefs.getInt("b", 255)
        val size = prefs.getInt("size", 10)

        dotView.updateColor(r, g, b)
        dotView.updateSize(size)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(dotView)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
