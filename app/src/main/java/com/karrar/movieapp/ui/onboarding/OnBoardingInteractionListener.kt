package com.karrar.movieapp.ui.onboarding

import com.karrar.movieapp.ui.base.BaseInteractionListener

interface OnBoardingInteractionListener : BaseInteractionListener {
    fun onPageChanged(pageIndex: Int)
    fun onClickPreviousButton()
    fun onClickNextButton()
    fun onClickGetStartedButton()
}