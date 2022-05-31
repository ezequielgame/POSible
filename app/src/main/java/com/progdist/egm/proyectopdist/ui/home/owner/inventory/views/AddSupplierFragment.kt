package com.progdist.egm.proyectopdist.ui.home.owner.inventory.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.network.InventoryApi
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.databinding.FragmentAddSupplierBinding
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels.AddSupplierViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AddSupplierFragment : BaseFragment<AddSupplierViewModel,FragmentAddSupplierBinding,InventoryRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val collapsingToolbarLayout = requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout.title = "Añadir proveedor"

//        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
//        heroIcon.setImageResource(R.drawable.ic_hero_add_branch)

        val fab: FloatingActionButton = requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton
        fab.visibility = View.GONE

        binding.tfSupplierName.addTextChangedListener {
            if(!binding.tfSupplierName.text.toString().trim().isEmpty()){
                binding.tilSupplierName.error = null
            }
        }

        binding.tfSupplierMail.addTextChangedListener {
            if(!binding.tfSupplierMail.text.toString().trim().isEmpty()){
                binding.tilSupplierMail.error = null
            }
        }

        binding.tfSupplierPhoneNumber.addTextChangedListener {
            if(!binding.tfSupplierPhoneNumber.text.toString().trim().isEmpty()){
                binding.tilSupplierPhoneNumber.error = null
            }
        }

        viewModel.addSupplierResponse.observe(viewLifecycleOwner) {
            when(it){
                is Resource.success -> {
                    Toast.makeText(requireContext(), "Se añadió el proveedor", Toast.LENGTH_SHORT).show()
                    val action = AddSupplierFragmentDirections.actionAddSupplierFragmentToSuppliersFragment()
                    findNavController().navigate(action)
                }
                is Resource.failure -> {

                }
            }
        }

        binding.btnAdd.setOnClickListener{

            if(validForm()){
                val userId = requireArguments().getInt("userId")
                val supplierName = binding.tfSupplierName.text.toString().trim()
                val supplierMail = binding.tfSupplierMail.text.toString().trim()
                val supplierPhone = binding.tfSupplierPhoneNumber.text.toString().trim()
                viewModel.addSupplier(userId,supplierName,supplierPhone, supplierMail)
            }

        }

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

    override fun getViewModel(): Class<AddSupplierViewModel> = AddSupplierViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentAddSupplierBinding = FragmentAddSupplierBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): InventoryRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(InventoryApi::class.java,token)
        return InventoryRepository(api,userPreferences)
    }

}