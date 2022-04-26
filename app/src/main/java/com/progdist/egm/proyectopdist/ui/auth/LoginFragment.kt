package com.progdist.egm.proyectopdist.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.progdist.egm.proyectopdist.databinding.FragmentLoginBinding
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.data.network.AuthApi
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.AuthRepository

class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.success ->{
                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
                }
                is Resource.failure -> {
                    Toast.makeText(requireContext(), "Login failure", Toast.LENGTH_LONG).show()
                }
            }
        })

        binding.btnLogin.setOnClickListener{
            val email = binding.tfEmail.text.toString().trim()
            val password = binding.tfPassword.text.toString().trim()
            //todo add input validations
            viewModel.login(email, password)
        }
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = AuthRepository(remoteDataSource.buildApi(AuthApi::class.java))


}