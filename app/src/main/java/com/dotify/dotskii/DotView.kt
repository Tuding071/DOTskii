package com.dotify.dotskii

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View

class DotView(context: Context) : View(context) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var radius = 10f

    fun setColor(r: Int, g: Int, b: Int) {
        paint.color = android.graphics.Color.rgb(r, g, b)
        invalidate()
    }

    fun setSize(sizePx: Int) {
        radius = sizePx / 2f
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }
}
