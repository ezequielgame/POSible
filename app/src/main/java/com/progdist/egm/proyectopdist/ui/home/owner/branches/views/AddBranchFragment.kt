package com.progdist.egm.proyectopdist.ui.home.owner.branches.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.network.BranchesApi
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.BranchesRepository
import com.progdist.egm.proyectopdist.databinding.FragmentAddBranchBinding
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.owner.branches.viewmodel.AddBranchViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AddBranchFragment : BaseFragment<AddBranchViewModel, FragmentAddBranchBinding, BranchesRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val collapsingToolbarLayout = requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout.title = "Añadir sucursal"

        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
        heroIcon.setImageResource(R.drawable.ic_hero_add_branch)

        val fab: FloatingActionButton = requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton
        fab.visibility = View.GONE

        binding.tfBusinessName.addTextChangedListener {
            if(!binding.tfBusinessName.text.toString().trim().isEmpty()){
                binding.tilBusinessName.error = null
            }
        }

        binding.tfCountry.addTextChangedListener {
            if(!binding.tfCountry.text.toString().trim().isEmpty()){
                binding.tilCountry.error = null
            }
        }

        binding.tfEstate.addTextChangedListener {
            if(!binding.tfEstate.text.toString().trim().isEmpty()){
                binding.tilEstate.error = null
            }
        }
        binding.tfCity.addTextChangedListener {
            if(!binding.tfCity.text.toString().trim().isEmpty()){
                binding.tilCity.error = null
            }
        }
        binding.tfAddress.addTextChangedListener {
            if(!binding.tfAddress.text.toString().trim().isEmpty()){
                binding.tilAddress.error = null
            }
        }
        binding.tfPhoneNumber.addTextChangedListener {
            if(!binding.tfPhoneNumber.text.toString().trim().isEmpty()){
                binding.tilPhoneNumber.error = null
            }
        }
        binding.tfDescription.addTextChangedListener {
            if(!binding.tfDescription.text.toString().trim().isEmpty()){
                binding.tilDescription.error = null
            }
        }



        viewModel.addBranchLocationResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.success -> {
                    val branchName = binding.tfBusinessName.text.toString().trim()
                    val branchPhone = binding.tfPhoneNumber.text.toString().trim()
                    val branchDescription = binding.tfDescription.text.toString().trim()
                    viewModel.addBranch(activity?.intent?.getIntExtra("user",-1).toString(), branchName, it.value.result.lastId, branchPhone, branchDescription)
                }
                is Resource.failure -> {

                }
            }
        })

        viewModel.addBranchResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.success -> {
                    Toast.makeText(requireContext(), "Se añadió la sucursal", Toast.LENGTH_SHORT).show()
                    val action = AddBranchFragmentDirections.actionAddBranchFragmentToBranchesFragment()
                    findNavController().navigate(action)
                }
                is Resource.failure -> {

                }
            }
        })


        binding.btnAdd.setOnClickListener{

            if(validForm()){
                val address = binding.tfAddress.text.toString().trim()
                val city = binding.tfCity.text.toString().trim()
                val estate = binding.tfEstate.text.toString().trim()
                val country = binding.tfCountry.text.toString().trim()
                viewModel.addBranchLocation(address, city, estate, country)
            }

        }

    }

    private fun validForm(): Boolean {
        var valid: Boolean = true
        val businessName = binding.tfBusinessName.text.toString().trim()
        val country = binding.tfCountry.text.toString().trim()
        val estate = binding.tfEstate.text.toString().trim()
        val city = binding.tfCity.text.toString().trim()
        val address = binding.tfAddress.text.toString().trim()
        val phone = binding.tfPhoneNumber.text.toString().trim()
        val desc = binding.tfDescription.text.toString().trim()

        valid = validField(binding.tfBusinessName, binding.tilBusinessName, listOf()) and
                validField(binding.tfCountry, binding.tilCountry, listOf(businessName)) and
                validField(binding.tfEstate, binding.tilEstate, listOf(businessName, country)) and
                validField(binding.tfCity, binding.tilCity, listOf(businessName, country, estate)) and
                validField(binding.tfAddress, binding.tilAddress, listOf(businessName, country, estate, city)) and
                validField(binding.tfPhoneNumber, binding.tilPhoneNumber, listOf(businessName, country, estate, city, address)) and
                validField(binding.tfDescription, binding.tilDescription, listOf(businessName, country, estate, city, address, phone))

        if(desc.length > 255){

            var isTopError = true
            val prevContents = listOf(businessName, country, estate, city, address, phone)
            binding.tilDescription.error = "La descripción debe ser de menos de 255 caracteres"
            for(prevContent in prevContents){
                println(prevContent)
                if(prevContent.isEmpty()){
                    isTopError = false
                    break
                }
            }
            if(isTopError){
                binding.tilDescription.requestFocus()
            }
            valid = false
        }

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

    override fun getViewModel(): Class<AddBranchViewModel> = AddBranchViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentAddBranchBinding = FragmentAddBranchBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): BranchesRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(BranchesApi::class.java, token)
        return BranchesRepository(api,userPreferences)
    }


}