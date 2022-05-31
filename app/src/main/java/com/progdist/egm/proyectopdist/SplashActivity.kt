package com.progdist.egm.proyectopdist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.progdist.egm.proyectopdist.core.RemoteDataSource
import com.progdist.egm.proyectopdist.data.UserPreferences
import com.progdist.egm.proyectopdist.data.network.MainApi
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.MainRepository
import com.progdist.egm.proyectopdist.ui.base.ViewModelFactory
import com.progdist.egm.proyectopdist.ui.home.employee.view.EmployeeHomeActivity
import com.progdist.egm.proyectopdist.ui.home.owner.HomeActivity
import com.progdist.egm.proyectopdist.ui.main.MainActivity
import com.progdist.egm.proyectopdist.ui.main.MainViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SplashActivity : AppCompatActivity() {

    lateinit var userPreferences: UserPreferences

    lateinit var viewModel: MainViewModel

    val remoteDataSource = RemoteDataSource()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        userPreferences = UserPreferences(this)

        val factory = ViewModelFactory(getActivityRepository())
        viewModel = ViewModelProvider(this, factory).get(getViewModel())

        // Check for valid token in Preferences
        viewModel.validateEmployeeToken()
        viewModel.validateOwnerToken()

        viewModel.validateEmployeeTokenResponse.observe(this, Observer {
            if(it is Resource.success){
                val intent: Intent = Intent(this, EmployeeHomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("employeeId",it.value.employee)
                this.startActivity(intent)
                finish()
            } else{

            }
        })

        viewModel.validateOwnerTokenResponse.observe(this, Observer {
            if(it is Resource.success){
                val intent: Intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("user",it.value.user)
                this.startActivity(intent)
                finish()
            } else {
                // Request Errors

            }
        })
    }

    override fun onResume() {
        super.onResume()
        val intent: Intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        this.startActivity(intent)
        finish()
    }

    fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    fun getActivityRepository(): MainRepository{
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(MainApi::class.java, token)
        return  MainRepository(api)
    }
}