package com.progdist.egm.proyectopdist.ui.auth


import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.network.AuthApi
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.AuthRepository
import com.progdist.egm.proyectopdist.databinding.FragmentSignUpBinding
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import java.util.regex.Matcher
import java.util.regex.Pattern


class SignUpFragment : BaseFragment<AuthViewModel, FragmentSignUpBinding, AuthRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Do the register when click button
        binding.btnSignup.setOnClickListener{
            val name = binding.tfName.text.toString().trim()
            val email = binding.tfEmail.text.toString().trim()
            val password = binding.tfPassword.text.toString().trim()
            val businessName = binding.tfBusinessName.text.toString().trim()
            if(validForm()){
                viewModel.register(name,email,password,businessName) // Call the viewModel
            }
        }

        //Observe for register response
        viewModel.registerResponse.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.success -> {
                    val dialog = AlertDialog.Builder(requireContext())
                        .setTitle(R.string.success)
                        .setMessage(R.string.successful_register)
                        .setPositiveButton(R.string.ok) { view, _ ->

                        }
                        .setCancelable(false)
                        .create()
                    dialog.show()
                }
                is Resource.failure -> {
                    Toast.makeText(requireContext(), "Registro fallido", Toast.LENGTH_SHORT).show()
                }
            }
        })

        // Correcting inputs

        binding.tfName.addTextChangedListener{

            if(!binding.tfName.text.toString().trim().isEmpty()){
                binding.tilEmail.error = null
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

        binding.tfConfirmPassword.addTextChangedListener {
            if(!binding.tfConfirmPassword.text.toString().trim().isEmpty()){
                binding.tilConfirmPassword.error = null
            }
        }

        binding.tfBusinessName.addTextChangedListener {
            if(!binding.tfBusinessName.text.toString().trim().isEmpty()){
                binding.tilBusinessName.error = null
            }
        }

    }

    private fun validForm(): Boolean {
        var valid: Boolean = true
        val name = binding.tfName.text.toString().trim()
        val email = binding.tfEmail.text.toString().trim()
        val password = binding.tfPassword.text.toString().trim()
        val confirm = binding.tfConfirmPassword.text.toString().trim()
        val businessName = binding.tfBusinessName.text.toString().trim()

        valid = validField(binding.tfName, binding.tilName, listOf())
            validField(binding.tfEmail, binding.tilEmail, listOf(name)) and
            validField(binding.tfPassword, binding.tilPassword, listOf(name,email)) and
            validField(binding.tfConfirmPassword, binding.tilConfirmPassword, listOf(name,email, password)) and
            validField(binding.tfBusinessName, binding.tilBusinessName, listOf(name,email,password, confirm))

        if(valid){
            if(!email.isValidEmail()){
                binding.tilEmail.error = getString(R.string.email_format_error)
                binding.tilEmail.requestFocus()
                valid = false
            } else if (!validPassword(password)) {
                Snackbar.make(binding.root,getString(R.string.password_format_error), Snackbar.LENGTH_SHORT).show()
                valid = false
            } else if(password != confirm) {
                Snackbar.make(binding.root,getString(R.string.password_mismatch_error), Snackbar.LENGTH_SHORT).show()
                binding.tilConfirmPassword.error = getString(R.string.password_mismatch)
                binding.tilConfirmPassword.requestFocus()
                valid = false
            }

        }
        return valid
    }

    private fun String?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun validPassword(password: String): Boolean {

        val pattern: Pattern
        val matcher: Matcher
        val specialCharacters = "-@%\\[\\}+'!/#$^?:;,\\(\"\\)~`.*=&\\{>\\]<_"
        val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[$specialCharacters])(?=\\S+$).{8,20}$"
        pattern = Pattern.compile(PASSWORD_REGEX)
        matcher = pattern.matcher(password)
        return matcher.matches()
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

    override fun getViewModel(): Class<AuthViewModel> = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignUpBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): AuthRepository =
        AuthRepository(remoteDataSource.buildApi(AuthApi::class.java), userPreferences)


}