package com.karrar.movieapp.ui.onboarding

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class OnBoardingPageTransformer : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        val rotationDegrees = if (position != 0f) position * 18f else 0f
        page.rotation = rotationDegrees

        val scaleFactor = 1f - (abs(position))
        page.scaleX = scaleFactor.coerceAtLeast(0.9f)
        page.scaleY = scaleFactor.coerceAtLeast(0.9f)
    }
}