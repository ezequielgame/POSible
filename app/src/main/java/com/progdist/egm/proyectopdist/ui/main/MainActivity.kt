package com.progdist.egm.proyectopdist.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.network.MainApi
import com.progdist.egm.proyectopdist.data.network.Resource
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

        // Check for token in Preferences
        viewModel.validateEmployeeToken()
        viewModel.validateOwnerToken()

        viewModel.validateEmployeeTokenResponse.observe(this, Observer {
            if(it is Resource.success){
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Empleado")
                    .setMessage("sesion iniciada")
                    .setPositiveButton(R.string.ok) { view, _ ->

                    }
                    .setCancelable(false)
                    .create()
                dialog.show()
            } else{
                Toast.makeText(this, "Empleado sin sesion", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.validateOwnerTokenResponse.observe(this, Observer {
            if(it is Resource.success){
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Dueño")
                    .setMessage("sesion iniciada")
                    .setPositiveButton(R.string.ok) { view, _ ->

                    }
                    .setCancelable(false)
                    .create()
                dialog.show()
            } else {
                Toast.makeText(this, "Dueño sin sesion", Toast.LENGTH_SHORT).show()
            }
        })

        // No token exists, so
        binding.btnAdmin.setOnClickListener{
            val intent: Intent = Intent(this,AccountActivity::class.java)
            intent.putExtra("type","owner")
            startActivity(intent)
        }

        binding.btnEmployee.setOnClickListener{
            val intent: Intent = Intent(this,EmployeeAccountActivity::class.java)
            intent.putExtra("type","employee")
            startActivity(intent)
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