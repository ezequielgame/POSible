package com.progdist.egm.proyectopdist.ui.auth

import android.content.Intent
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
import com.progdist.egm.proyectopdist.ui.home.employee.view.EmployeeHomeActivity
import com.progdist.egm.proyectopdist.ui.home.owner.HomeActivity

class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var domain: String = ""
        when(activity){
            is EmployeeAccountActivity->{
                domain = "employees"
            }
            is AccountActivity -> {
                domain = "users"
            }
        }

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.success -> {
                    val intent:Intent = Intent(requireContext(), HomeActivity::class.java)
                    intent.flags =  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra("user",  it.value.user.id_user)
                    viewModel.saveAuthToken(it.value.user.token_user)
                    startActivity(intent)
                }
                is Resource.failure -> {
                    if(it.errorCode == 401){
                        Toast.makeText(requireContext(), "Contraseña o correo incorrectos", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })

        viewModel.employeeLoginResponse.observe(viewLifecycleOwner){
            when (it) {
                is Resource.success -> {
                    val intent:Intent = Intent(requireContext(), EmployeeHomeActivity::class.java)
                    intent.flags =  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra("employeeId",  it.value.employee.id_employee)
                    viewModel.saveAuthToken(it.value.employee.token_employee)
                    startActivity(intent)
                }
                is Resource.failure -> {
                    if(it.errorCode == 401){
                        Toast.makeText(requireContext(), "Contraseña o correo incorrectos", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

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