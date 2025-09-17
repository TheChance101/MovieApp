package com.karrar.movieapp.ui.components.header

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.ListHeaderSectionBinding

class SectionHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ListHeaderSectionBinding = ListHeaderSectionBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    init {
        attrs?.let { attributeSet ->
            context.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.SectionHeaderView,
                0, 0
            ).apply {
                try {
                    val titleText = getString(R.styleable.SectionHeaderView_text)
                    val showSeeAll = getBoolean(R.styleable.SectionHeaderView_showSeeAll, true)

                    titleText?.let { setTitle(it) }
                    setShowSeeAll(showSeeAll)
                } finally {
                    recycle()
                }
            }
        }
    }

    fun setTitle(title: String) {
        binding.textTitle.text = title
    }

    fun setShowSeeAll(show: Boolean) {
        binding.textSeeAll.isVisible = show
    }

    fun setSeeAllClickListener(listener: () -> Unit) {
        binding.textSeeAll.setOnClickListener { listener() }
    }
}