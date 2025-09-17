package com.karrar.movieapp.ui.main

import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.widget.TextViewCompat

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHome: LinearLayout
    private lateinit var navExplore: LinearLayout
    private lateinit var navMatch: LinearLayout
    private lateinit var navMe: LinearLayout

    private lateinit var iconHome: ImageView
    private lateinit var iconExplore: ImageView
    private lateinit var iconMatch: ImageView
    private lateinit var iconMe: ImageView

    private lateinit var labelHome: TextView
    private lateinit var labelExplore: TextView
    private lateinit var labelMatch: TextView
    private lateinit var labelMe: TextView

    private var currentTab = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY)
        setTheme(R.style.Theme_MovieApp)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        installSplashScreen()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initializeCustomNavigation()
    }

    override fun onResume() {
        super.onResume()
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.exploringFragment,
                R.id.myListFragment,
                R.id.profileFragment,
            )
        )
        navController = findNavController(R.id.nav_host_fragment)

        setupCustomBottomNavigation()
    }

    private fun initializeCustomNavigation() {
        // Initialize custom bottom navigation views
        navHome = binding.navHome
        navExplore = binding.navExplore
        navMatch = binding.navMatch
        navMe = binding.navMe

        iconHome = binding.iconHome
        iconExplore = binding.iconExplore
        iconMatch = binding.iconMatch
        iconMe = binding.iconMe

        labelHome = binding.labelHome
        labelExplore = binding.labelExplore
        labelMatch = binding.labelMatch
        labelMe = binding.labelMe
    }

    private fun setupCustomBottomNavigation() {
        navHome.setOnClickListener {
            navigateToDestination(R.id.homeFragment, 0)
        }
        navExplore.setOnClickListener {
            navigateToDestination(R.id.exploringFragment, 1)
        }
        navMatch.setOnClickListener {
            navigateToDestination(R.id.myListFragment, 2)
        }
        navMe.setOnClickListener {
            navigateToDestination(R.id.profileFragment, 3)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateTabSelection(destination.id)
        }

        updateTabSelection(R.id.homeFragment)
    }

    private fun navigateToDestination(destinationId: Int, tabIndex: Int) {
        if (currentTab == tabIndex) return

        currentTab = tabIndex

        try {
            navController.navigate(destinationId)
        } catch (e: IllegalArgumentException) {
            // Handle case where destination might not be accessible from current location
            navController.popBackStack(destinationId, false)
                ?: navController.navigate(destinationId)
        }
    }

    private fun updateTabSelection(destinationId: Int) {
        resetAllTabs()

        when (destinationId) {
            R.id.homeFragment -> {
                currentTab = 0
                setTabSelected(iconHome, labelHome, R.drawable.ic_home_duetone)
            }

            R.id.exploringFragment -> {
                currentTab = 1
                setTabSelected(iconExplore, labelExplore, R.drawable.ic_search_duetone)
            }

            R.id.myListFragment -> {
                currentTab = 2
                setTabSelected(iconMatch, labelMatch, R.drawable.ic_magic_stick_duetone)
            }

            R.id.profileFragment -> {
                currentTab = 3
                setTabSelected(iconMe, labelMe, R.drawable.ic_user_square_duetone)
            }
        }
    }

    private fun resetAllTabs() {
        setTabUnselected(iconHome, labelHome, R.drawable.ic_home)
        setTabUnselected(iconExplore, labelExplore, R.drawable.ic_search)
        setTabUnselected(iconMatch, labelMatch, R.drawable.ic_magic_stick)
        setTabUnselected(iconMe, labelMe, R.drawable.ic_user_square)
    }

    private fun setTabSelected(icon: ImageView, label: TextView, selectedIconRes: Int) {
        icon.setImageResource(selectedIconRes)
        icon.setColorFilter(ContextCompat.getColor(this, R.color.brand_primary))
        label.setTextColor(ContextCompat.getColor(this, R.color.brand_primary))
        TextViewCompat.setTextAppearance(label, R.style.Typography_label_md_semi_bold)
    }

    private fun setTabUnselected(icon: ImageView, label: TextView, unSelectedIconRes: Int) {
        icon.setImageResource(unSelectedIconRes)
        icon.setColorFilter(ContextCompat.getColor(this, R.color.shade_tertiary))
        label.setTextColor(ContextCompat.getColor(this, R.color.shade_tertiary))
        TextViewCompat.setTextAppearance(label, R.style.Typography_label_md_regular)
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}