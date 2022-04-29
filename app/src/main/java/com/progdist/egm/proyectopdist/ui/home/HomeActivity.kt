package com.progdist.egm.proyectopdist.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.progdist.egm.proyectopdist.databinding.ActivityHomeBinding
import com.progdist.egm.proyectopdist.ui.base.BaseActivity

class HomeActivity : BaseActivity<Nothing, ActivityHomeBinding, Nothing>() {

    override fun getViewBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)

    override fun getViewModel(): Class<Nothing> {
        TODO("Not yet implemented")
    }

    override fun getActivityRepository(): Nothing {
        TODO("Not yet implemented")
    }
}