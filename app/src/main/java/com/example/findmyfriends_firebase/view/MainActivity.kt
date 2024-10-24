package com.example.findmyfriends_firebase.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.findmyfriends_firebase.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.example.findmyfriends_firebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var actionDrawerToggle: ActionBarDrawerToggle

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        var navController = findNavController(R.id.fragmentContainerView)
        binding.bottomBar.setupWithNavController(navController)
        binding.drawerNav.setupWithNavController(navController)

        actionDrawerToggle =
            ActionBarDrawerToggle(this, binding.drawerlayout, R.string.nav_open, R.string.nav_close)
        actionDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.drawerNav.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.logout -> {
                    Firebase.auth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()

                }
                R.id.profilesFragment -> {
                    navController.navigate(R.id.profilesFragment)
                }
                R.id.friendFragment -> {
                    navController.navigate(R.id.friendFragment)
                }
            }
            true
        }
        binding.bottomBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.logout -> {
                    Firebase.auth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()

                }
                R.id.friendFragment -> {
                    navController.navigate(R.id.friendFragment)
                }
                R.id.profilesFragment -> {
                    navController.navigate(R.id.profilesFragment)
                }

            }
            true

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
}