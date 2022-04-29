package com.progdist.egm.proyectopdist.ui.auth

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.adapter.AccountPagerAdapter
import com.progdist.egm.proyectopdist.adapter.EmployeeAccountPagerAdapter
import com.progdist.egm.proyectopdist.databinding.ActivityEmployeeAccountBinding

class EmployeeAccountActivity : AppCompatActivity() {

    private var _binding: ActivityEmployeeAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEmployeeAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tabLayout = binding.tabLayout
        viewPager = binding.viewPager

        viewPager.adapter = EmployeeAccountPagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager){ tab, index ->
            tab.text = when(index){
                0 -> {
                    getString(R.string.login_action)
                } else -> {
                    throw  Resources.NotFoundException("Position not found")
                }
            }
        }.attach()

    }
}