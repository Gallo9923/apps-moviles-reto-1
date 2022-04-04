package com.example.instagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.instagram.databinding.ActivityMainBinding
import com.example.instagram.databinding.ActivityNavigatorBinding


class NavigatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigatorBinding

    private lateinit var homeFragment: HomeFragment
    private lateinit var publishFragment: PublishFragment
    private lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigatorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        homeFragment = HomeFragment.newInstance()
        publishFragment = PublishFragment.newInstance()
        profileFragment = ProfileFragment.newInstance()

        showFragment(homeFragment)

        binding.navigator.setOnItemSelectedListener {menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    showFragment(homeFragment)
                }
                R.id.publication -> {
                    showFragment(publishFragment)
                }
                R.id.profile -> {
                    showFragment(profileFragment)
                }
            }
            true
        }

    }

    fun showFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }
}