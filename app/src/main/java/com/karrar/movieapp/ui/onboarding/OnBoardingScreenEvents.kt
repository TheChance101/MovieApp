package com.karrar.movieapp.ui.onboarding

sealed class OnBoardingScreenEvents {
    data object NavigateToLoginScreen : OnBoardingScreenEvents()
}