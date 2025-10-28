package com.dotify.dotskii

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat

class DotOverlayService : Service() {

    private var dotView: DotView? = null
    private var windowManager: WindowManager? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val prefs = getSharedPreferences("dotskii_prefs", Context.MODE_PRIVATE)
        val r = prefs.getInt("r", 0)
        val g = prefs.getInt("g", 255)
        val b = prefs.getInt("b", 255)
        val size = prefs.getInt("size", 10)

        val color = android.graphics.Color.rgb(r, g, b)

        dotView = DotView(this, size.toFloat(), color)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            android.graphics.PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
        }

        windowManager?.addView(dotView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        dotView?.let { windowManager?.removeView(it) }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
