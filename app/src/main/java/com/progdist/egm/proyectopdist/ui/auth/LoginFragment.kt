package com.progdist.egm.proyectopdist.ui.auth

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.data.network.AuthApi
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.AuthRepository
import com.progdist.egm.proyectopdist.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.success -> {
                    if(binding.cbRememberMe.isChecked) {
                        viewModel.saveAuthToken(it.value.user.token_user)
                    }
                    Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_LONG).show()
                }
                is Resource.failure -> {
                    Toast.makeText(requireContext(), "Login failure", Toast.LENGTH_LONG).show()
                }
            }
        })

        viewModel.employeeLoginResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.success -> {
                    Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_LONG).show()
                    if(binding.cbRememberMe.isChecked) {
                        viewModel.saveAuthToken(it.value.employee.token_employee)
                    }
                }
                is Resource.failure -> {
                    Toast.makeText(requireContext(), "Login failure", Toast.LENGTH_LONG).show()
                }
            }
        })

        binding.tfEmail.addTextChangedListener {
            if(!binding.tfEmail.text.toString().trim().isEmpty()){
                binding.tilEmail.error = null
            }
        }

        binding.tfPassword.addTextChangedListener {
            if(!binding.tfPassword.text.toString().trim().isEmpty()){
                binding.tilPassword.error = null
            }
        }

        var domain: String = ""
        when(activity){
            is EmployeeAccountActivity->{
                domain = "employees"
            }
            is AccountActivity -> {
                domain = "users"
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.tfEmail.text.toString().trim()
            val password = binding.tfPassword.text.toString().trim()
            if(validForm()){
                viewModel.login(domain, email, password)
            }
        }
    }

    private fun validForm(): Boolean {
        var valid: Boolean = true
        val email = binding.tfEmail.text.toString().trim()

        valid = validField(binding.tfEmail, binding.tilEmail, listOf()) and
                validField(binding.tfPassword, binding.tilPassword, listOf(email))

        return valid
    }

    private fun validField(textField: TextInputEditText, textInputLayout: TextInputLayout, prevContents: List<String>): Boolean{        val content: String = textField.text.toString().trim()

        if(content.isEmpty()){
            textInputLayout.error = getString(R.string.required_field)
            var isTopError: Boolean = true
            for(prevContent in prevContents){
                println(prevContent)
                if(prevContent.isEmpty()){
                    isTopError = false
                    break
                }
            }
            if(isTopError){
                textInputLayout.requestFocus()
            }
            return false
        } else {
            textInputLayout.error = null
            return true
        }
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        AuthRepository(remoteDataSource.buildApi(AuthApi::class.java), userPreferences)


}