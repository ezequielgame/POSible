package com.progdist.egm.proyectopdist.adapter

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.progdist.egm.proyectopdist.ui.auth.SignUpFragment
import com.progdist.egm.proyectopdist.ui.auth.LoginFragment

class AccountPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                LoginFragment()
            } 1->{
                SignUpFragment()
            } else -> {
                throw Resources.NotFoundException("Position not found")
            }
        }
    }

}