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
import android.widget.ImageView
import android.widget.Toast
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
import com.progdist.egm.proyectopdist.databinding.FragmentEmployeeSummaryBinding
import com.progdist.egm.proyectopdist.ui.CodeScannerAcitvity
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.owner.employees.viewmodel.EmployeeSummaryViewModel
import com.progdist.egm.proyectopdist.ui.home.owner.employees.viewmodel.ManageEmployeesViewModel
import com.progdist.egm.proyectopdist.ui.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.regex.Matcher
import java.util.regex.Pattern

class EmployeeSummaryFragment : BaseFragment<EmployeeSummaryViewModel,FragmentEmployeeSummaryBinding,ManageEmployeesRepository>() {

    var employeeId: Int = -1
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

//        binding.tilEmployeePassword.visibility = View.GONE
//        binding.tilEmployeeConfirmPassword.visibility = View.GONE
        employeeId = requireArguments().getInt("employeeId",-1)
        userId = requireArguments().getInt("userId",-1)

        val collapsingToolbarLayout = requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout.title = "Empleado"

        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
        heroIcon.setImageResource(R.drawable.ic_employee)

        val fab: FloatingActionButton =
            requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton
        fab.visibility = View.VISIBLE
        fab.setImageResource(R.drawable.ic_edit)
        
        viewModel.getEmployees("id_employee",employeeId.toString())
        
        viewModel.getEmployeesResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.success->{
                    collapsingToolbarLayout.title = it.value.result[0].name_employee
                    binding.tfEmployeeName.setText(it.value.result[0].name_employee)
                    binding.tfEmployeeCode.setText(it.value.result[0].code_employee)
                    binding.tfEmployeeMail.setText(it.value.result[0].email_employee)
                    binding.tfEmployeePhoneNumber.setText(it.value.result[0].phone_number_employee)
                    binding.actvEmployeeBranch.setText(it.value.result[0].name_branch)
                    binding.actvEmployeeRole.setText(it.value.result[0].name_role)
                    idBranchSelected = it.value.result[0].id_branch_employee
                    idRoleSelected = it.value.result[0].id_role_employee
                }
                is Resource.failure->{
                    
                }
            }
        }

        fab.setOnClickListener {
            setTextFieldsState(true)
            fab.visibility = View.GONE
            setTextInputLayoutsHelperText("Requerido*")
            binding.tilEmployeeName.visibility = View.VISIBLE
            binding.btnSave.visibility = View.VISIBLE
            binding.tilEmployeePassword.visibility = View.VISIBLE
            binding.tilEmployeeConfirmPassword.visibility = View.VISIBLE
        }

        setTextFieldsState(false)
        setTextInputLayoutsHelperText("")

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
                    idRoleSelected = aRoles[position+1].id_role
                    binding.actvEmployeeRole.setText(aRoles[position+1].name_role)
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

        binding.btnSave.setOnClickListener{

            if(validForm()){
                if(saveInfo()){
                    fab.visibility = View.VISIBLE
                    setTextFieldsState(false)
                    setTextInputLayoutsHelperText("")
                    binding.tilEmployeeName.visibility = View.GONE
                    binding.btnSave.visibility = View.GONE
                    binding.tilEmployeePassword.visibility = View.GONE
                    binding.tilEmployeeConfirmPassword.visibility = View.GONE
                    viewModel.getEmployees("id_employee",employeeId.toString())
                }

            }

        }

        viewModel.editEmployeeResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.success -> {

                }
                is Resource.failure -> {
                    if(it.errorCode == 409){
                        binding.root.showToast("No se editó porque ya había un empleado con esos datos")
                    }
                }
            }
        }


    }

    private fun saveInfo(): Boolean{
        val name = binding.tfEmployeeName.text.toString().trim()
        val code = binding.tfEmployeeCode.text.toString().trim()
        val mail = binding.tfEmployeeMail.text.toString().trim()
        val password = binding.tfEmployeePassword.text.toString().trim()
        val cpassword = binding.tfEmployeeConfirmPassword.text.toString().trim()
        val phone = binding.tfEmployeePhoneNumber.text.toString().trim()

        if(password != "" || cpassword != ""){
            if(password == cpassword){
                if(!validPassword(password)){
                    Snackbar.make(binding.root,getString(R.string.password_format_error), Snackbar.LENGTH_SHORT).show()
                }else{
                    viewModel.editEmployee(
                        employeeId,
                        "id_employee",
                        idBranchSelected,
                        code,
                        name,
                        mail,
                        password,
                        phone,
                        idRoleSelected
                    )
                    binding.root.showToast("Se guardó la información")
                    return true
                }
                return false
            }else{
                Snackbar.make(binding.root,getString(R.string.password_mismatch_error), Snackbar.LENGTH_SHORT).show()
                return false
            }
        }

        viewModel.editEmployee(
            employeeId,
            "id_employee",
            idBranchSelected,
            code,
            name,
            mail,
            phone,
            idRoleSelected
        )
        Toast.makeText(requireContext(), "Se guardó la información", Toast.LENGTH_SHORT).show()
        return true
    }

    private fun setTextFieldsState(state: Boolean){
        binding.tilEmployeeName.isEnabled = state
        binding.tilEmployeeCode.isEnabled = state
        binding.tilEmployeeMail.isEnabled = state
        binding.tilEmployeePassword.isEnabled = state
        binding.tilEmployeeConfirmPassword.isEnabled = state
        binding.tilEmployeePhoneNumber.isEnabled = state
        binding.tilEmployeeBranch.isEnabled = state
        binding.tilEmployeeRole.isEnabled = state
        binding.btnCodeScanner.isEnabled = state
    }

    private fun setTextInputLayoutsHelperText(text: String){
        binding.tilEmployeeName.helperText = text
        binding.tilEmployeeCode.helperText = text
        binding.tilEmployeeMail.helperText = text
        binding.tilEmployeePhoneNumber.helperText = text
        binding.tilEmployeeBranch.helperText = text
        binding.tilEmployeeRole.helperText = text
    }

    private fun validForm(): Boolean {
        var valid: Boolean = true
        val name = binding.tfEmployeeName.text.toString().trim()
        val code = binding.tfEmployeeCode.text.toString().trim()
        val mail = binding.tfEmployeeMail.text.toString().trim()
        val phone = binding.tfEmployeePhoneNumber.text.toString().trim()


        valid = validField(binding.tfEmployeeName, binding.tilEmployeeName, listOf()) and
                validField(binding.tfEmployeeCode, binding.tilEmployeeCode, listOf(name)) and
                validField(binding.tfEmployeeMail,
                    binding.tilEmployeeMail,
                    listOf(name, code)) and
                validField(binding.tfEmployeePhoneNumber,
                    binding.tilEmployeePhoneNumber,
                    listOf(name, code, mail))

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
    

    override fun getViewModel(): Class<EmployeeSummaryViewModel> = EmployeeSummaryViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentEmployeeSummaryBinding = FragmentEmployeeSummaryBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): ManageEmployeesRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ManageEmployeesApi::class.java,token)
        return ManageEmployeesRepository(api,userPreferences)
    }


}