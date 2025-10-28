package com.dotify.dotskii

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DotView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var radius = 10f
    private var color = 0xFF00FFFF.toInt() // default cyan

    private var screenWidth = 0
    private var screenHeight = 0

    fun setScreenSize(width: Int, height: Int) {
        screenWidth = width
        screenHeight = height
        invalidate()
    }

    fun updateColor(newColor: Int) {
        color = newColor
        paint.color = color
        invalidate()
    }

    fun updateSize(newSize: Int) {
        radius = newSize.toFloat()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (screenWidth == 0 || screenHeight == 0) {
            // fallback: use view dimensions
            screenWidth = width
            screenHeight = height
        }
        paint.color = color
        canvas.drawCircle(screenWidth / 2f, screenHeight / 2f, radius, paint)
    }
}
