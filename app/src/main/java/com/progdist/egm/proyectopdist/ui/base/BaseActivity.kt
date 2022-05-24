package com.progdist.egm.proyectopdist.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.progdist.egm.proyectopdist.data.UserPreferences
import com.progdist.egm.proyectopdist.core.RemoteDataSource
import com.progdist.egm.proyectopdist.data.repository.BaseRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseActivity<VM: ViewModel, B: ViewBinding, R: BaseRepository> : AppCompatActivity(), LifecycleObserver {

    protected lateinit var userPreferences: UserPreferences
    private var _binding: B? = null
    protected val binding
        get() = _binding!!

    protected lateinit var viewModel: VM

    protected val remoteDataSource = RemoteDataSource()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        userPreferences = UserPreferences(this)
        _binding = getViewBinding()
        setContentView(binding.root)

        val factory = ViewModelFactory(getActivityRepository())
        viewModel = ViewModelProvider(this, factory).get(getViewModel())

        lifecycleScope.launch{userPreferences.authToken.first()}

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    abstract fun getViewModel(): Class<VM>

    abstract fun getViewBinding(): B

    abstract fun getActivityRepository(): R

}