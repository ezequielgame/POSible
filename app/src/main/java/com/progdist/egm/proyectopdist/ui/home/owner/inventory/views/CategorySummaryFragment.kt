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
import com.progdist.egm.proyectopdist.databinding.FragmentCategorySummaryBinding
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels.CategorySummaryViewModel
import com.progdist.egm.proyectopdist.ui.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class CategorySummaryFragment :
    BaseFragment<CategorySummaryViewModel, FragmentCategorySummaryBinding, InventoryRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val roleId =requireArguments().getInt("roleId")

        val collapsingToolbarLayout =
            requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout.title = "Editar categoría"

        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
        heroIcon.setImageResource(R.drawable.ic_category)

        val fab: FloatingActionButton =
            requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton
        if(roleId != 3){
            fab.visibility = View.VISIBLE
        }else{
            fab.visibility = View.GONE
        }
        fab.setImageResource(R.drawable.ic_edit)

        fab.setOnClickListener {
            setTextFieldsState(true)
            if(roleId != 3){
                fab.visibility = View.VISIBLE
            }else{
                fab.visibility = View.GONE
            }
            setTextInputLayoutsHelperText("Requerido*")
            binding.tilCategoryName.visibility = View.VISIBLE
            binding.btnSave.visibility = View.VISIBLE
        }

        binding.btnSave.visibility = View.GONE

        viewModel.getCategories("id_category", requireArguments().getInt("categoryId"))

        viewModel.getCategoriesResponse.observe(viewLifecycleOwner) { getResponse ->
            when (getResponse) {
                is Resource.success -> {
                    if (getResponse.value.status != 404) {
                        collapsingToolbarLayout.title = getResponse.value.result[0].name_category
                        binding.tfCategoryName.setText(getResponse.value.result[0].name_category)
                        binding.tfDescription.setText(getResponse.value.result[0].description_category)
                    }
                }
                is Resource.failure -> {

                }
            }
        }

        viewModel.editCategoryResponse.observe(viewLifecycleOwner){editResponse->
            when(editResponse){
                is Resource.success->{
                    viewModel.getCategories("id_category",requireArguments().getInt("categoryId"))
                }
                is Resource.failure->{

                }
            }
        }

        binding.btnSave.setOnClickListener{

            if(validForm()){
                saveInfo()
                if(roleId != 3){
                    fab.visibility = View.VISIBLE
                }else{
                    fab.visibility = View.GONE
                }
                setTextFieldsState(false)
                setTextInputLayoutsHelperText("")
                binding.tilCategoryName.visibility = View.GONE
                viewModel.getCategories("id_category",requireArguments().getInt("categoryId"))
            }

        }
    }


    private fun saveInfo(){
        val categoryName = binding.tfCategoryName.text.toString().trim()
        val descrption = binding.tfDescription.text.toString().trim()
        viewModel.editCategory(requireArguments().getInt("categoryId"),"id_category",categoryName,descrption)
        Toast.makeText(requireContext(), "Se guardó la información", Toast.LENGTH_SHORT).show()
    }

    private fun setTextFieldsState(state: Boolean){
        binding.tilCategoryName.isEnabled = state
        binding.tilDescription.isEnabled = state
    }

    private fun setTextInputLayoutsHelperText(text: String){
        binding.tilCategoryName.helperText = text
        binding.tilDescription.helperText = text
    }


    private fun validForm(): Boolean {
        var valid: Boolean = true
        val categoryName = binding.tfCategoryName.text.toString().trim()
        val description = binding.tfDescription.text.toString().trim()

        valid = validField(binding.tfCategoryName, binding.tilCategoryName, listOf()) and
                validField(binding.tfDescription, binding.tilDescription, listOf(categoryName))

        if(description.length > 255){

            var isTopError = true
            val prevContents = listOf(categoryName)
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

    override fun getViewModel(): Class<CategorySummaryViewModel> =
        CategorySummaryViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentCategorySummaryBinding =
        FragmentCategorySummaryBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): InventoryRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(InventoryApi::class.java, token)
        return InventoryRepository(api, userPreferences)
    }
}