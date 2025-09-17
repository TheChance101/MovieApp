package com.karrar.movieapp.ui.components.indicator

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.animation.ValueAnimator
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.karrar.movieapp.R
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class MovieCircularProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var strokeWidth: Float = 10f
    private var gradientColors: IntArray
    private var rotationAngle = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.BUTT
    }

    init {
        val primaryColor = ContextCompat.getColor(context, R.color.brand_primary)
        val tertiaryColor = ContextCompat.getColor(context, R.color.brand_tertiary)
        gradientColors = intArrayOf(primaryColor, tertiaryColor)

        context.withStyledAttributes(attrs, R.styleable.MovieCircularProgressBar) {
            strokeWidth = getDimension(R.styleable.MovieCircularProgressBar_strokeWidth, 10f)
        }

        startAnimation()
    }

    private fun startAnimation() {
        val animator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 1000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            addUpdateListener {
                rotationAngle = it.animatedValue as Float
                invalidate()
            }
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cx = width / 2f
        val cy = height / 2f
        val radius = (min(width, height) - strokeWidth) / 2f

        val sweepGradient = SweepGradient(cx, cy, gradientColors, null)
        paint.shader = sweepGradient
        paint.strokeWidth = strokeWidth

        canvas.save()
        canvas.rotate(rotationAngle, cx, cy)

        // Draw circle arc
        canvas.drawArc(
            cx - radius,
            cy - radius,
            cx + radius,
            cy + radius,
            0f,
            360f,
            false,
            paint
        )

        // Draw moving dot
        val angleRad = Math.toRadians(0.0)
        val dotX = cx + radius * cos(angleRad).toFloat()
        val dotY = cy + radius * sin(angleRad).toFloat()
        val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = gradientColors[0]
        }
        canvas.drawCircle(dotX, dotY, strokeWidth / 2, dotPaint)

        canvas.restore()
    }
}
