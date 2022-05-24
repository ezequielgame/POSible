package com.progdist.egm.proyectopdist.ui.auth

import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.adapter.AccountPagerAdapter
import com.progdist.egm.proyectopdist.databinding.ActivityAccountBinding


class AccountActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private var _binding: ActivityAccountBinding? = null
    protected val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tabLayout = binding.tabLayout
        viewPager = binding.viewPager

        viewPager.adapter = AccountPagerAdapter(supportFragmentManager, lifecycle)
        TabLayoutMediator(tabLayout, viewPager){ tab, index ->
            tab.text = when(index){
                0 -> {
                    getString(R.string.login_action)
                } 1 -> {
                    getString(R.string.action_signup)
                } else -> {
                    throw  Resources.NotFoundException("Position not found")
                }
            }
        }.attach()

    }
}