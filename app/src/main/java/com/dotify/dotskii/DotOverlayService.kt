package com.dotify.dotskii

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager

class DotOverlayService : Service() {

    private var dotView: DotView? = null
    private lateinit var windowManager: WindowManager

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val prefs = getSharedPreferences("dotskii_prefs", MODE_PRIVATE)
        val r = prefs.getInt("r", 0)
        val g = prefs.getInt("g", 255)
        val b = prefs.getInt("b", 255)
        val sizeDp = prefs.getInt("size", 10)

        val density = resources.displayMetrics.density
        val sizePx = (sizeDp * density).toInt()

        dotView = DotView(this).apply {
            setColor(r, g, b)
            setSize(sizePx)
        }

        val params = WindowManager.LayoutParams(
            sizePx,
            sizePx,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.CENTER
        windowManager.addView(dotView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        dotView?.let { windowManager.removeView(it) }
        dotView = null
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
