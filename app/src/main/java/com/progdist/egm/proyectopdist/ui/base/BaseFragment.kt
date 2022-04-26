package com.progdist.egm.proyectopdist.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.progdist.egm.proyectopdist.data.network.RemoteDataSource
import com.progdist.egm.proyectopdist.data.repository.BaseRepository

abstract class BaseFragment<VM: ViewModel, B: ViewBinding, R: BaseRepository> : Fragment(), LifecycleObserver {

    private var _binding: B? = null
    protected lateinit var binding: B

    protected lateinit var viewModel: VM

    protected val remoteDataSource = RemoteDataSource()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getFragmentBinding(inflater, container)
        binding = _binding!!
        val factory = ViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(this, factory).get(getViewModel())
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    abstract fun getViewModel(): Class<VM>

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    abstract fun getFragmentRepository(): R

}