package com.petid.petid.ui.component

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.petid.petid.R

/**
 * 사용자 서명
 */
class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val path = Path()

    private val paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        strokeWidth = 5f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(context, R.color.white))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
    }

    var onDrawDetected: ((doDraw: Boolean) -> Unit)? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                onDrawDetected?.invoke(true)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                onDrawDetected?.invoke(true)
            }
            MotionEvent.ACTION_UP -> {
                // Do nothing
            }
        }
        invalidate()
        return true
    }

    fun clear() {
        path.reset()
        invalidate()
    }
}
