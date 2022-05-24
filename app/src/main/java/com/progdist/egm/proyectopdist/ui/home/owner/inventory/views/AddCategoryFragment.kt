package com.progdist.egm.proyectopdist.ui.home.owner.inventory.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.network.InventoryApi
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.databinding.FragmentAddCategoryBinding
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels.AddCategoryViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AddCategoryFragment : BaseFragment<AddCategoryViewModel, FragmentAddCategoryBinding, InventoryRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val collapsingToolbarLayout = requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout.title = "Añadir categoría"

//        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
//        heroIcon.setImageResource(R.drawable.ic_hero_add_branch)

        val fab: FloatingActionButton = requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton
        fab.visibility = View.GONE

        binding.tfCategoryName.addTextChangedListener {
            if(!binding.tfCategoryName.text.toString().trim().isEmpty()){
                binding.tilCategoryName.error = null
            }
        }

        binding.tfDescription.addTextChangedListener {
            if(!binding.tfDescription.text.toString().trim().isEmpty()){
                binding.tilDescription.error = null
            }
        }

        viewModel.addCategoryResponse.observe(viewLifecycleOwner) {
            when(it){
                is Resource.success -> {
                    Toast.makeText(requireContext(), "Se añadió la categoría", Toast.LENGTH_SHORT).show()
                }
                is Resource.failure -> {

                }
            }
        }


        binding.btnAdd.setOnClickListener{

            if(validForm()){
                val userId = activity?.intent?.getIntExtra("user", -1)
                val categoryName = binding.tfCategoryName.text.toString().trim()
                val description = binding.tfDescription.text.toString().trim()
                viewModel.addCategory(userId!!,categoryName,description)
            }

        }

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

    override fun getViewModel(): Class<AddCategoryViewModel> = AddCategoryViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentAddCategoryBinding = FragmentAddCategoryBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): InventoryRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(InventoryApi::class.java, token)
        return InventoryRepository(api,userPreferences)
    }

}