package com.progdist.egm.proyectopdist.ui.main

import android.content.Intent
import android.os.Bundle
import com.progdist.egm.proyectopdist.data.network.MainApi
import com.progdist.egm.proyectopdist.data.repository.MainRepository
import com.progdist.egm.proyectopdist.databinding.ActivityMainBinding
import com.progdist.egm.proyectopdist.ui.auth.AccountActivity
import com.progdist.egm.proyectopdist.ui.auth.EmployeeAccountActivity
import com.progdist.egm.proyectopdist.ui.base.BaseActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding, MainRepository>(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnAdmin.setOnClickListener{
            val intent: Intent = Intent(this,AccountActivity::class.java)
            intent.putExtra("type","owner")
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
//            finish()
        }

        binding.btnEmployee.setOnClickListener{
            val intent: Intent = Intent(this,EmployeeAccountActivity::class.java)
            intent.putExtra("type","employee")
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
//            finish()
        }

    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun getActivityRepository(): MainRepository{
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(MainApi::class.java, token)
        return  MainRepository(api)
    }
}