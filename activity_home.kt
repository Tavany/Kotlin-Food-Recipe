package com.sehatin.ittp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.sehatin.ittp.databinding.ActivityHomeBinding
import com.sehatin.ittp.fragment.AddFragment
import com.sehatin.ittp.fragment.HomeFragment
import com.sehatin.ittp.fragment.ProfileFragment
import com.sehatin.ittp.fragment.ResepFragment

class activity_home : AppCompatActivity() {

    val fragmentResep : Fragment = ResepFragment()
    val fragmentHome: Fragment = HomeFragment()
//    val fragmentAdd: Fragment = AddFragment()
    val fragmentProfile: Fragment = ProfileFragment()
    val fm: FragmentManager = supportFragmentManager
    var active: Fragment = fragmentHome

    lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var binding: ActivityHomeBinding
    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonNavigation()
    }

    private fun buttonNavigation() {
        fm.beginTransaction().add(R.id.container, fragmentResep).show(fragmentResep).commit()
        fm.beginTransaction().add(R.id.container, fragmentHome).hide(fragmentHome).commit()
//        fm.beginTransaction().add(R.id.container, fragmentAdd).hide(fragmentAdd).commit()
        fm.beginTransaction().add(R.id.container, fragmentProfile).hide(fragmentProfile).commit()

        bottomNavigationView = binding.navView
        menu = bottomNavigationView.menu
        menuItem = menu.getItem(0)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.navigation_resep -> {
                    callFragment(0, fragmentResep)
                }
                R.id.navigation_home -> {
                    callFragment(1, fragmentHome)
                }
//                R.id.navigation_add -> {
////                    item.isChecked = false
////                    startActivity(Intent(this@activity_home,AddPostActivity::class.java))
//                    callFragment(2, fragmentAdd)
//                }
                R.id.navigation_profile -> {
                    callFragment(2, fragmentProfile)
                }
            }
            false
        }
    }

    private fun callFragment(index : Int, fragment: Fragment) {
        menuItem = menu.getItem(index)
        menuItem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }
}