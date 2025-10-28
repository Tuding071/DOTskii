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

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // get display size
        val metrics = DisplayMetrics()
        windowManager?.defaultDisplay?.getRealMetrics(metrics)
        val screenWidth = metrics.widthPixels
        val screenHeight = metrics.heightPixels

        // read prefs
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
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP or Gravity.START

        windowManager?.addView(dotView, params)
    }

    fun updateDot(color: Int, size: Int) {
        dotView?.updateColor(color)
        dotView?.updateSize(size)
    }

    override fun onDestroy() {
        super.onDestroy()
        dotView?.let { windowManager?.removeView(it) }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
