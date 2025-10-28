package com.dotify.dotskii

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class DotView(context: Context) : View(context) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val size: Int

    init {
        val prefs = context.getSharedPreferences("dotskii_prefs", Context.MODE_PRIVATE)
        val r = prefs.getInt("r", 0)
        val g = prefs.getInt("g", 255)
        val b = prefs.getInt("b", 255)
        size = prefs.getInt("size", 10)

        paint.color = Color.rgb(r, g, b)
        paint.style = Paint.Style.FILL
        layoutParams = android.view.ViewGroup.LayoutParams(size * 2, size * 2)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val radius = width / 2f
        canvas.drawCircle(radius, radius, radius, paint)
    }
}
