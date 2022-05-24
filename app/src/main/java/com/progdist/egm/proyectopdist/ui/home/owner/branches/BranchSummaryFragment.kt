package com.progdist.egm.proyectopdist.ui.home.owner.branches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.network.BranchesApi
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.network.UploadBranchImageRequestBody
import com.progdist.egm.proyectopdist.data.repository.BranchesRepository
import com.progdist.egm.proyectopdist.data.responses.branches.ExtendedBranch
import com.progdist.egm.proyectopdist.databinding.FragmentBranchSummaryBinding
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.owner.HomeActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class BranchSummaryFragment : BaseFragment<BranchesSummaryViewModel, FragmentBranchSummaryBinding, BranchesRepository>(), UploadBranchImageRequestBody.UploadCallback {


    private lateinit var fab: FloatingActionButton
    private lateinit var heroIcon: ImageView
    private var thisBranch: ExtendedBranch? = null
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab = requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton

        collapsingToolbarLayout = requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout.title = "Sucursal"

        heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
        heroIcon.setImageResource(R.drawable.ic_shop)

        fab.visibility = View.VISIBLE

        fab.setImageResource(R.drawable.ic_edit)

        fab.setOnClickListener {
            setTextFieldsState(true)
            fab.visibility = View.GONE
            setTextInputLayoutsHelperText("Requerido*")
            binding.tilBusinessName.visibility = View.VISIBLE
            binding.btnSave.visibility = View.VISIBLE
        }

        binding.btnSave.setOnClickListener{

            if(validForm()){
                saveInfo()
                fab.visibility = View.VISIBLE
                setTextFieldsState(false)
                setTextInputLayoutsHelperText("")
                binding.tilBusinessName.visibility = View.GONE
                binding.btnSave.visibility = View.GONE
                viewModel.getBranch("id_branch",thisBranch!!.id_branch)
            }
        }

        setTextInputLayoutsHelperText("")

        viewModel.getBranch("id_branch", requireArguments().getInt("branchId"))

        viewModel.getBranchResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.success->{
                    thisBranch = it.value.result[0]
                    collapsingToolbarLayout.title = thisBranch!!.name_branch
                    binding.tfBusinessName.setText(thisBranch!!.name_branch)
                    binding.tfCountry.setText(thisBranch!!.country_location)
                    binding.tfEstate.setText(thisBranch!!.estate_location)
                    binding.tfCity.setText(thisBranch!!.city_location)
                    binding.tfAddress.setText(thisBranch!!.address_location)
                    binding.tfPhoneNumber.setText(thisBranch!!.phone_number_branch)
                    binding.tfDescription.setText(thisBranch!!.description_branch)
                    val homeActivity = (activity as HomeActivity?)
                    val selectedBranch = homeActivity!!.selectedBranch
                    //Update in the branches list of home activity
                    //Search updated branch in list
                    homeActivity.branches.forEach {
                        if(it.id_branch == thisBranch!!.id_branch){
                            it.name_branch = thisBranch!!.name_branch
                            it.phone_number_branch = thisBranch!!.phone_number_branch
                            it.description_branch = thisBranch!!.description_branch
                        }
                    }
                    if(selectedBranch!=null && selectedBranch.id_branch == thisBranch!!.id_branch){ //If selected branch updated
                        //Update the name in the header
                        homeActivity.headerBranch.text = thisBranch!!.name_branch
                    }
                }
                is Resource.failure->{

                }
            }
        })
    }

    private fun saveInfo(){
        val country = binding.tfCountry.text.toString().trim()
        val estate = binding.tfEstate.text.toString().trim()
        val city = binding.tfCity.text.toString().trim()
        val address = binding.tfAddress.text.toString().trim()
        val name = binding.tfBusinessName.text.toString().trim()
        val phone = binding.tfPhoneNumber.text.toString().trim()
        val descrption = binding.tfDescription.text.toString().trim()
        viewModel.editBranchLocation(thisBranch!!.id_location,"id_location",address, city, estate, country)
        viewModel.editBranch(thisBranch!!.id_branch,"id_branch",name,phone,descrption)
        Toast.makeText(requireContext(), "Se guardó la información", Toast.LENGTH_SHORT).show()
    }
    
    private fun setTextFieldsState(state: Boolean){
        binding.tilBusinessName.isEnabled = state
        binding.tilCountry.isEnabled = state
        binding.tilEstate.isEnabled = state
        binding.tilCity.isEnabled = state
        binding.tilAddress.isEnabled = state
        binding.tilPhoneNumber.isEnabled = state
        binding.tilDescription.isEnabled = state
    }
    
    private fun setTextInputLayoutsHelperText(text: String){
        binding.tilBusinessName.helperText = text
        binding.tilCountry.helperText = text
        binding.tilEstate.helperText = text
        binding.tilCity.helperText = text
        binding.tilAddress.helperText = text
        binding.tilPhoneNumber.helperText = text
        binding.tilDescription.helperText = text
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


    override fun getViewModel(): Class<BranchesSummaryViewModel>  = BranchesSummaryViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentBranchSummaryBinding = FragmentBranchSummaryBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): BranchesRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(BranchesApi::class.java, token)
        return BranchesRepository(api,userPreferences)
    }

    override fun onProgressUpdate(percentage: Int) {

    }

}