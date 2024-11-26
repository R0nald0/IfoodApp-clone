package com.example.ifood_clone2.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavHost
import androidx.navigation.ui.NavigationUI
import com.example.ifood_clone2.R
import com.example.ifood_clone2.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private val  binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        inicializarNavigation()
    }

    fun inicializarNavigation(){
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragment_conteiner_view_principal) as NavHost

        val navController = navHost.navController
        NavigationUI.setupWithNavController(
            binding.bottomNavigationViewPrincipal,
            navController
        )
    }
}