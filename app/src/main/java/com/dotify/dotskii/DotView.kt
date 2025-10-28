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

    init {
        paint.color = color
        paint.style = Paint.Style.FILL
    }

    fun setScreenSize(width: Int, height: Int) {
        screenWidth = width
        screenHeight = height
    }

    fun updateColor(newColor: Int) {
        color = newColor
        paint.color = color
        invalidate()
    }

    fun updateSize(newSize: Int) {
        radius = newSize.toFloat()
        invalidate()
        // Request new layout measurement since size changed
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Calculate desired size (diameter + some padding for safety)
        val desiredSize = (radius * 2 + 4).toInt()
        
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        
        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> minOf(desiredSize, widthSize)
            else -> desiredSize
        }
        
        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> minOf(desiredSize, heightSize)
            else -> desiredSize
        }
        
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        // Draw dot at the center of this view
        val centerX = width / 2f
        val centerY = height / 2f
        
        paint.color = color
        canvas.drawCircle(centerX, centerY, radius, paint)
    }
}
