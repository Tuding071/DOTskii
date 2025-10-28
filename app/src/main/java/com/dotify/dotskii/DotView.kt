package com.dotify.dotskii

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View

class DotView(context: Context, private val radius: Float, private val color: Int) : View(context) {

    private val paint = Paint().apply {
        isAntiAlias = true
        this.color = color
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw dot at the center of the screen
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }
}
