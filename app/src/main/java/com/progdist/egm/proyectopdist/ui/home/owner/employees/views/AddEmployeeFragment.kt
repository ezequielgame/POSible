package com.progdist.egm.proyectopdist.ui.home.owner.employees.views

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.network.ManageEmployeesApi
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.ManageEmployeesRepository
import com.progdist.egm.proyectopdist.data.responses.branches.ExtendedBranch
import com.progdist.egm.proyectopdist.data.responses.employees.Role
import com.progdist.egm.proyectopdist.databinding.FragmentAddEmployeeBinding
import com.progdist.egm.proyectopdist.ui.CodeScannerAcitvity
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.owner.employees.viewmodel.AddEmployeeViewModel
import com.progdist.egm.proyectopdist.ui.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.regex.Matcher
import java.util.regex.Pattern

class AddEmployeeFragment : BaseFragment<AddEmployeeViewModel,FragmentAddEmployeeBinding,ManageEmployeesRepository>() {


    var userId: Int = -1
    private var idBranchSelected: Int = -1
    private var idRoleSelected: Int = -1
    private lateinit var aBranches: Array<ExtendedBranch>
    private lateinit var aRoles: Array<Role>

    private val startForCodeScannerResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                binding.tfEmployeeCode.setText(data!!.getStringExtra("code").toString())
            }
        }

    private val askCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if(it){
                val intent = Intent(requireActivity(), CodeScannerAcitvity::class.java)
                startForCodeScannerResult.launch(intent)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val collapsingToolbarLayout =
            requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout.title = "Añadir empleado"

//        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
//        heroIcon.setImageResource(R.drawable.ic_hero_add_branch)

        val fab: FloatingActionButton =
            requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton
        fab.visibility = View.GONE

        userId = requireArguments().getInt("userId",-1)

        viewModel.getBranch("id_user_branch", userId)

        viewModel.getBranchResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.success -> {
                    aBranches = it.value.result.toTypedArray()
                }
                is Resource.failure -> {

                }
            }
        }

        //todo add user created roles, current defaults
        viewModel.getRoles("id_user_role", 1)

        viewModel.getRolesResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.success -> {
                    aRoles = it.value.result.toTypedArray()
                }
                is Resource.failure -> {

                }
            }
        }

        binding.btnCodeScanner.setOnClickListener {
            val permission = requireContext().checkCallingOrSelfPermission(android.Manifest.permission.CAMERA)
            if(permission == PackageManager.PERMISSION_GRANTED){

                val intent = Intent(requireActivity(), CodeScannerAcitvity::class.java)
                startForCodeScannerResult.launch(intent)

            } else {
                binding.root.showToast("Se necesitan permisos para usar la cámara")
                askCameraPermission.launch(Manifest.permission.CAMERA)
            }

        }

        binding.tfEmployeeName.addTextChangedListener {
            if (!binding.tfEmployeeName.text.toString().trim().isEmpty()) {
                binding.tilEmployeeName.error = null
            }
        }

        binding.tfEmployeeCode.addTextChangedListener {
            if (!binding.tfEmployeeCode.text.toString().trim().isEmpty()) {
                binding.tilEmployeeCode.error = null
            }
        }

        binding.tfEmployeeMail.addTextChangedListener {
            if (!binding.tfEmployeeMail.text.toString().trim().isEmpty()) {
                binding.tilEmployeeMail.error = null
            }
        }

        binding.tfEmployeePassword.addTextChangedListener {
            if (!binding.tfEmployeePassword.text.toString().trim().isEmpty()) {
                binding.tilEmployeePassword.error = null
            }
        }

        binding.tfEmployeeConfirmPassword.addTextChangedListener {
            if (!binding.tfEmployeeConfirmPassword.text.toString().trim().isEmpty()) {
                binding.tilEmployeeConfirmPassword.error = null
            }
        }

        binding.tfEmployeePhoneNumber.addTextChangedListener {
            if (!binding.tfEmployeePhoneNumber.text.toString().trim().isEmpty()) {
                binding.tilEmployeePhoneNumber.error = null
            }
        }


        binding.actvEmployeeBranch.setOnClickListener {

            binding.tilEmployeeBranch.error = null

            val branchesNames: MutableList<String> = ArrayList()
            aBranches.forEach {
                branchesNames += it.name_branch
            }
            val branchesOptions = branchesNames.toTypedArray()


            val builder = MaterialAlertDialogBuilder(requireContext())
            // dialog title
            builder.setTitle("Selecciona una sucursal")

            builder.setSingleChoiceItems(
                branchesOptions, -1
            ) { dialog, i -> }

            // alert dialog positive button
            builder.setPositiveButton("Seleccionar") { dialog, which ->
                val position = (dialog as AlertDialog).listView.checkedItemPosition
                // if selected, then get item text
                if (position != -1) {
                    idBranchSelected = aBranches[position].id_branch
                    binding.actvEmployeeBranch.setText(aBranches[position].name_branch)
                } else {
                    idBranchSelected = -1
                    binding.actvEmployeeBranch.setText("Sucursal")
                }
            }

            // set dialog non cancelable
            builder.setCancelable(true)

            // finally, create the alert dialog and show it
            val dialog = builder.create()
            dialog.show()
        }

        binding.actvEmployeeRole.setOnClickListener {

            binding.tilEmployeeRole.error = null

            val rolesNames: MutableList<String> = ArrayList()
            aRoles.forEach {
                if(it.id_role != 1){
                    rolesNames += it.name_role + ": " + it.description_role
                }
            }
            val rolesOptions = rolesNames.toTypedArray()


            val builder = MaterialAlertDialogBuilder(requireContext())
            // dialog title
            builder.setTitle("Selecciona un rol")

            builder.setSingleChoiceItems(
                rolesOptions, -1
            ) { dialog, i -> }

            // alert dialog positive button
            builder.setPositiveButton("Seleccionar") { dialog, which ->
                val position = (dialog as AlertDialog).listView.checkedItemPosition
                // if selected, then get item text
                if (position != -1) {
                    idRoleSelected = aRoles[position].id_role
                    binding.actvEmployeeRole.setText(aRoles[position].name_role)
                } else {
                    idRoleSelected = -1
                    binding.actvEmployeeRole.setText("Rol")
                }
            }

            // set dialog non cancelable
            builder.setCancelable(true)

            // finally, create the alert dialog and show it
            val dialog = builder.create()
            dialog.show()
        }

        viewModel.addEmployeeResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.success -> {
                    if (it.value.status == 200) {
                        binding.root.showToast("Se creó el empleado")
                        val action = AddEmployeeFragmentDirections.actionAddEmployeeFragmentToManageEmployeesFragment()
                        findNavController().navigate(action)
                    }
                }
                is Resource.failure -> {
                    if (it.errorCode == 409) {
                        binding.root.showToast("Ya existe un empleado con esos correo y/o código")
                    }
                }
            }
        }


        binding.btnAdd.setOnClickListener {

            if (validForm()) {
                val name = binding.tfEmployeeName.text.toString().trim()
                val code = binding.tfEmployeeCode.text.toString().trim()
                val mail = binding.tfEmployeeMail.text.toString().trim()
                val password = binding.tfEmployeePassword.text.toString().trim()
                val phone = binding.tfEmployeePhoneNumber.text.toString().trim()

                viewModel.addEmployee(
                    userId,
                    idBranchSelected,
                    code,
                    name,
                    mail,
                    password,
                    phone,
                    idRoleSelected
                )

            }
        }

    }

    private fun validForm(): Boolean {
        var valid: Boolean = true
        val name = binding.tfEmployeeName.text.toString().trim()
        val code = binding.tfEmployeeCode.text.toString().trim()
        val mail = binding.tfEmployeeMail.text.toString().trim()
        val password = binding.tfEmployeePassword.text.toString().trim()
        val confirm = binding.tfEmployeeConfirmPassword.text.toString().trim()
        val phone = binding.tfEmployeePhoneNumber.text.toString().trim()


        valid = validField(binding.tfEmployeeName, binding.tilEmployeeName, listOf()) and
                validField(binding.tfEmployeeCode, binding.tilEmployeeCode, listOf(name)) and
                validField(binding.tfEmployeeMail,
                    binding.tilEmployeeMail,
                    listOf(name, code)) and
                validField(binding.tfEmployeePassword,
                    binding.tilEmployeePassword,
                    listOf(name, code, mail)) and
                validField(binding.tfEmployeeConfirmPassword,
                    binding.tilEmployeeConfirmPassword,
                    listOf(name, code, mail, password)) and
                validField(binding.tfEmployeePhoneNumber,
                    binding.tilEmployeePhoneNumber,
                    listOf(name, code, mail, password, confirm))

        if (idBranchSelected == -1) {
            valid = false
            binding.tilEmployeeBranch.error = "Debes elegir una sucursal"
        }
        if (idRoleSelected == -1) {
            valid = false
            binding.tilEmployeeRole.error = "Debes elegir un rol"
        }

        if(valid){
            if(!mail.isValidEmail()){
                binding.tilEmployeeMail.error = getString(R.string.email_format_error)
                binding.tilEmployeeMail.requestFocus()
                valid = false
            } else if (!validPassword(password)) {
                Snackbar.make(binding.root,getString(R.string.password_format_error), Snackbar.LENGTH_SHORT).show()
                valid = false
            } else if(password != confirm) {
                Snackbar.make(binding.root,getString(R.string.password_mismatch_error), Snackbar.LENGTH_SHORT).show()
                binding.tilEmployeeConfirmPassword.error = getString(R.string.password_mismatch)
                binding.tilEmployeeConfirmPassword.requestFocus()
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



    private fun validField(
        textField: TextInputEditText,
        textInputLayout: TextInputLayout,
        prevContents: List<String>,
    ): Boolean {
        val content: String = textField.text.toString().trim()

        if (content.isEmpty()) {
            textInputLayout.error = getString(R.string.required_field)
            var isTopError: Boolean = true
            for (prevContent in prevContents) {
                println(prevContent)
                if (prevContent.isEmpty()) {
                    isTopError = false
                    break
                }
            }
            if (isTopError) {
                textInputLayout.requestFocus()
            }
            return false
        } else {
            textInputLayout.error = null
            return true
        }
    }


    override fun getViewModel(): Class<AddEmployeeViewModel> = AddEmployeeViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentAddEmployeeBinding = FragmentAddEmployeeBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): ManageEmployeesRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ManageEmployeesApi::class.java,token)
        return ManageEmployeesRepository(api,userPreferences)
    }

}