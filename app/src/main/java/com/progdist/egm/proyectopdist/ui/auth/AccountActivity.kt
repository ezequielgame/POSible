package com.progdist.egm.proyectopdist.ui.auth

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.adapter.AccountPagerAdapter
import com.progdist.egm.proyectopdist.data.UserPreferences
import com.progdist.egm.proyectopdist.data.repository.BaseRepository
import com.progdist.egm.proyectopdist.databinding.ActivityAccountBinding
import com.progdist.egm.proyectopdist.ui.base.BaseActivity


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

        viewPager.adapter = AccountPagerAdapter(this)
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