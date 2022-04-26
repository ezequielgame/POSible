package com.progdist.egm.proyectopdist.ui.auth

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.progdist.egm.proyectopdist.adapter.AccountPagerAdapter
import com.progdist.egm.proyectopdist.databinding.ActivityAccountBinding


class AccountActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private var _binding: ActivityAccountBinding? = null
    private lateinit var binding: ActivityAccountBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAccountBinding.inflate(layoutInflater)
        binding = _binding!!
        setContentView(binding.root)

        tabLayout = binding.tabLayout
        viewPager = binding.viewPager

        viewPager.adapter = AccountPagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager){ tab, index ->
            tab.text = when(index){
                0 -> {
                    "LOGIN"
                } 1 -> {
                    "SIGNUP"
                } else -> {
                    throw  Resources.NotFoundException("Position not found")
                }
            }
        }.attach()

    }

}