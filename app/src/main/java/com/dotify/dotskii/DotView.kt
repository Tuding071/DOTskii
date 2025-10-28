package com.dotify.dotskii

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View

class DotView(context: Context) : View(context) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var dotSize = 50 // default size
    private var color = 0xFF00FFFF.toInt() // default cyan

    fun updateSize(newSize: Int) {
        dotSize = newSize
        invalidate()
    }

    fun updateColor(r: Int, g: Int, b: Int) {
        color = 0xFF shl 24 or (r shl 16) or (g shl 8) or b
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = color
        val cx = width / 2f
        val cy = height / 2f
        canvas.drawCircle(cx, cy, dotSize.toFloat(), paint)
    }
}
