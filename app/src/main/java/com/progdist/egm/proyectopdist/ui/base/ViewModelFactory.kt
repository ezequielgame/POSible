package com.progdist.egm.proyectopdist.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.progdist.egm.proyectopdist.data.repository.AuthRepository
import com.progdist.egm.proyectopdist.data.repository.BaseRepository
import com.progdist.egm.proyectopdist.ui.auth.AuthViewModel

class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(AuthViewModel::class.java)-> AuthViewModel(repository as AuthRepository) as T
            else -> throw IllegalArgumentException("View Model Class Not Found")
        }
    }

}