package com.karrar.movieapp.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.ActivityOnBoardingBinding
import com.karrar.movieapp.ui.base.BaseActivity
import com.karrar.movieapp.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity<ActivityOnBoardingBinding>() {

    override val layoutIdActivity: Int = R.layout.activity_on_boarding
    override val viewModel: OnBoardingViewModel by viewModels()

    private lateinit var pagerAdapter: OnBoardingPagerAdapter
    private lateinit var pageTransformer: OnBoardingPageTransformer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPrefs = getSharedPreferences(ON_BOARDING_PREFS, MODE_PRIVATE)
        val isOnboardingCompleted = sharedPrefs.getBoolean(ON_BOARDING_COMPLETED, false)
        if (isOnboardingCompleted) {
            navigateToMain()
            return
        }

        setupViewPager()
        setupObservers()
        setupClickListeners()
    }

    private fun setupViewPager() {
        pagerAdapter = OnBoardingPagerAdapter(emptyList(), viewModel)
        pageTransformer = OnBoardingPageTransformer()

        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.isUserInputEnabled = true
        binding.viewPager.setPageTransformer(pageTransformer)
        binding.viewPager.offscreenPageLimit = 1

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.onPageChanged(position)
            }
        })
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                updateUI(state)
            }
        }

        lifecycleScope.launch {
            viewModel.uiEffect.collect { event ->
                when (event) {
                    is OnBoardingScreenEvents.NavigateToLoginScreen -> {
                        navigateToMain()
                    }
                }
            }
        }
    }

    private fun updateUI(state: OnBoardingState) {
        pagerAdapter.updatePages(state.pages)
        if (binding.viewPager.currentItem != state.currentPage) {
            binding.viewPager.setCurrentItem(state.currentPage, true)
        }
        if (state.pages.isNotEmpty() && state.currentPage < state.pages.size) {
            val currentPage = state.pages[state.currentPage]
            binding.titleText.text = currentPage.title.asString(this)
            binding.descriptionText.text = currentPage.description.asString(this)
        }
        binding.previousButton.visibility =
            if (state.currentPage == 0) View.INVISIBLE else View.VISIBLE

        if (state.currentPage == state.pages.lastIndex) {
            binding.nextButton.visibility = View.GONE
            binding.getStartedButton.visibility = View.VISIBLE
        } else {
            binding.nextButton.visibility = View.VISIBLE
            binding.getStartedButton.visibility = View.GONE
        }
    }

    private fun setupClickListeners() {
        binding.previousButton.setOnClickListener {
            viewModel.onClickPreviousButton()
        }

        binding.nextButton.setOnClickListener {
            viewModel.onClickNextButton()
        }

        binding.getStartedButton.setOnClickListener {
            viewModel.onClickGetStartedButton()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object{
        const val ON_BOARDING_COMPLETED = "onboarding_completed"
        const val ON_BOARDING_PREFS = "onboarding_prefs"
    }

}

