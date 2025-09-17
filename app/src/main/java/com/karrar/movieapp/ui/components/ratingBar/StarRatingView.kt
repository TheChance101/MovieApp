package com.karrar.movieapp.ui.components.ratingBar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.karrar.movieapp.R
import androidx.core.content.withStyledAttributes


class StarRatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val stars = mutableListOf<ImageView>()

    init {
        orientation = HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.rating_stars, this, true)

        stars.add(findViewById(R.id.star1))
        stars.add(findViewById(R.id.star2))
        stars.add(findViewById(R.id.star3))
        stars.add(findViewById(R.id.star4))
        stars.add(findViewById(R.id.star5))

        if (attrs != null) {
            context.withStyledAttributes(attrs, R.styleable.StarRatingView) {
                val rating = getFloat(R.styleable.StarRatingView_rating, 0f)
                setRating(rating)
            }
        }
    }

    fun setRating(rating: Float) {
        val fullStars = rating.toInt()
        val hasHalfStar = rating - fullStars >= 0.5

        stars.forEachIndexed { index, imageView ->
            when {
                index < fullStars -> {
                    imageView.setImageResource(R.drawable.ic_star_bold)
                    imageView.setColorFilter(ContextCompat.getColor(context, R.color.additional_primary_yellow))
                }
//                index == fullStars && hasHalfStar -> {
//                    imageView.setImageResource(R.drawable.ic_star_half)
//                    imageView.setColorFilter(ContextCompat.getColor(context, R.color.additional_primary_yellow))
//                }
                else -> {
                    imageView.setImageResource(R.drawable.ic_star_outlined)
                    imageView.setColorFilter(ContextCompat.getColor(context, R.color.shade_tertiary))
                }
            }
        }
    }
}