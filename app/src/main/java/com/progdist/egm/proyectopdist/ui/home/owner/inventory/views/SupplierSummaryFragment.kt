package com.progdist.egm.proyectopdist.ui.home.owner.inventory.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.network.InventoryApi
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.databinding.FragmentSupplierSummaryBinding
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels.SupplierSummaryViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SupplierSummaryFragment : BaseFragment<SupplierSummaryViewModel, FragmentSupplierSummaryBinding, InventoryRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val collapsingToolbarLayout =
            requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout.title = "Editar proveedor"

        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
        heroIcon.setImageResource(R.drawable.ic_supplier_list)

        val fab: FloatingActionButton =
            requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton
        fab.visibility = View.VISIBLE
        fab.setImageResource(R.drawable.ic_edit)

        fab.setOnClickListener {
            setTextFieldsState(true)
            fab.visibility = View.GONE
            setTextInputLayoutsHelperText("Requerido*")
            binding.tilSupplierName.visibility = View.VISIBLE
            binding.btnSave.visibility = View.VISIBLE
        }

        binding.btnSave.visibility = View.GONE

        viewModel.getSuppliers("id_supplier", requireArguments().getInt("supplierId"))

        viewModel.getSuppliersResponse.observe(viewLifecycleOwner) { getResponse ->
            when (getResponse) {
                is Resource.success -> {
                    if (getResponse.value.status != 404) {
                        collapsingToolbarLayout.title = getResponse.value.result[0].name_supplier
                        binding.tfSupplierName.setText(getResponse.value.result[0].name_supplier)
                        binding.tfSupplierMail.setText(getResponse.value.result[0].mail_supplier)
                        binding.tfSupplierPhoneNumber.setText(getResponse.value.result[0].phone_number_supplier)
                    }
                }
                is Resource.failure -> {

                }
            }
        }

        viewModel.editSupplierResponse.observe(viewLifecycleOwner){editResponse->
            when(editResponse){
                is Resource.success->{
                    viewModel.getSuppliers("id_supplier", requireArguments().getInt("supplierId"))
                }
                is Resource.failure->{

                }
            }
        }

        binding.btnSave.setOnClickListener{

            if(validForm()){
                saveInfo()
                fab.visibility = View.VISIBLE
                setTextFieldsState(false)
                setTextInputLayoutsHelperText("")
                binding.tilSupplierName.visibility = View.GONE
                binding.btnSave.visibility = View.GONE
                viewModel.getSuppliers("id_supplier", requireArguments().getInt("supplierId"))
            }

        }

    }

    private fun saveInfo(){
        val supplierName = binding.tfSupplierName.text.toString().trim()
        val supplierMail = binding.tfSupplierMail.text.toString().trim()
        val supplierPhone = binding.tfSupplierPhoneNumber.text.toString().trim()
        viewModel.editSupplier(requireArguments().getInt("supplierId"),"id_supplier",supplierName,supplierPhone,supplierMail)
        Toast.makeText(requireContext(), "Se guardó la información", Toast.LENGTH_SHORT).show()
    }

    private fun setTextFieldsState(state: Boolean){
        binding.tilSupplierName.isEnabled = state
        binding.tilSupplierMail.isEnabled = state
        binding.tilSupplierPhoneNumber.isEnabled = state
    }

    private fun setTextInputLayoutsHelperText(text: String){
        binding.tilSupplierName.helperText = text
        binding.tilSupplierMail.helperText = text
        binding.tilSupplierPhoneNumber.helperText = text
    }


    private fun validForm(): Boolean {
        var valid: Boolean = true
        val supplierName = binding.tfSupplierName.text.toString().trim()
        val supplierMail = binding.tfSupplierMail.text.toString().trim()
        val supplierPhone = binding.tfSupplierPhoneNumber.text.toString().trim()


        valid = validField(binding.tfSupplierName, binding.tilSupplierName, listOf()) and
                validField(binding.tfSupplierMail, binding.tilSupplierMail, listOf(supplierName))and
                validField(binding.tfSupplierPhoneNumber, binding.tilSupplierPhoneNumber, listOf(supplierName,supplierMail))

        return valid
    }


    private fun validField(textField: TextInputEditText, textInputLayout: TextInputLayout, prevContents: List<String>): Boolean{
        val content: String = textField.text.toString().trim()

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



    override fun getViewModel(): Class<SupplierSummaryViewModel> = SupplierSummaryViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSupplierSummaryBinding = FragmentSupplierSummaryBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): InventoryRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(InventoryApi::class.java,token)
        return InventoryRepository(api,userPreferences)
    }

}