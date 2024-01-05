package com.example.e_marketapp

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.e_marketapp.databinding.ActivityMainBinding
import com.example.e_marketapp.ui.basket.BasketFragment
import com.example.e_marketapp.viewmodel.MarketDbViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val marketDbViewModel : MarketDbViewModel by viewModels()
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        //bunu kapattım ve noaction bara çekip toolbarı kapattım
        //val appBarConfiguration = AppBarConfiguration(setOf(R.id.home, R.id.basket, R.id.favorite))
        //      setupActionBarWithNavController(navController)

        observeBadgeCount()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.home, R.id.basket, R.id.favorite,R.id.detailFragment -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
                else -> {
                    window.statusBarColor = ContextCompat.getColor(this,R.color.white)
                    bottomNavigationView.visibility = View.GONE
                }
            }
        }
        bottomNavigationView.setupWithNavController(navController)
    }
    private fun observeBadgeCount(){
        lifecycleScope.launch {
            marketDbViewModel.getBasketItems()
            marketDbViewModel.basketState.collectLatest {
                if (it.basketData?.isEmpty() == true){
                    badgeClear(R.id.basket)
                }else{
                    it.basketData?.size?.let { it1 -> badgeSetup(R.id.basket, it1) }
                }
            }
        }
    }

    private fun badgeSetup(id: Int, alerts: Int) {
        val badge = bottomNavigationView.getOrCreateBadge(id)
        badge.isVisible = true
        badge.number = alerts
    }

    private fun badgeClear(id: Int) {
        val badgeDrawable = bottomNavigationView.getBadge(id)
        if (badgeDrawable != null) {
            badgeDrawable.isVisible = false
            badgeDrawable.clearNumber()
        }
    }
}