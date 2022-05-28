package com.progdist.egm.proyectopdist.ui.home.owner.inventory.views

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.network.InventoryApi
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.responses.inventory.Category
import com.progdist.egm.proyectopdist.data.responses.inventory.Supplier
import com.progdist.egm.proyectopdist.databinding.FragmentAddItemBinding
import com.progdist.egm.proyectopdist.ui.CodeScannerAcitvity
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels.AddItemViewModel
import com.progdist.egm.proyectopdist.ui.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AddItemFragment :
    BaseFragment<AddItemViewModel, FragmentAddItemBinding, InventoryRepository>() {

    private var idCategorySelected: Int = -1
    private var idSupplierSelected: Int = -1
    private var idUser: Int = -1
    private lateinit var aCategories: Array<Category>
    private lateinit var aSuppliers: Array<Supplier>

    private val askCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if(it){
                val intent = Intent(requireActivity(), CodeScannerAcitvity::class.java)
                startForCodeScannerResult.launch(intent)
            }
        }

    private val startForCodeScannerResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                binding.tfItemCode.setText(data!!.getStringExtra("code").toString())
            }
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val collapsingToolbarLayout =
            requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout.title = "Añadir producto"

//        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
//        heroIcon.setImageResource(R.drawable.ic_hero_add_branch)

        val fab: FloatingActionButton =
            requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton
        fab.visibility = View.GONE

        idUser = activity?.intent?.getIntExtra("user", -1)!!

        viewModel.getCategories("id_user_category", idUser)

        viewModel.getCategoriesResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.success -> {
                    aCategories = it.value.result.toTypedArray()
                }
                is Resource.failure -> {
                    val builder = MaterialAlertDialogBuilder(requireContext())
                    // dialog title
                    builder.setTitle("Antes de continuar")
                    builder.setMessage("Debes crear al menos una categoría y un proveedor")
                    // alert dialog positive button
                    builder.setPositiveButton("Regresar") { dialog, which ->
                        val action = AddItemFragmentDirections.actionAddItemFragmentToInventoryFragment()
                        findNavController().navigate(action)
                    }
                    // set dialog non cancelable
                    builder.setCancelable(false)
                    // finally, create the alert dialog and show it
                    val dialog = builder.create()
                    dialog.show()
                }
            }
        }

        viewModel.getSuppliers("id_user_supplier", idUser)

        viewModel.getSuppliersResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.success -> {
                    aSuppliers = it.value.result.toTypedArray()
                }
                is Resource.failure -> {
                    val builder = MaterialAlertDialogBuilder(requireContext())
                    // dialog title
                    builder.setTitle("Antes de continuar")
                    builder.setMessage("Debes crear al menos una categoría y un proveedor")
                    // alert dialog positive button
                    builder.setPositiveButton("Regresar") { dialog, which ->
                        val action = AddItemFragmentDirections.actionAddItemFragmentToInventoryFragment()
                        findNavController().navigate(action)
                    }
                    // set dialog non cancelable
                    builder.setCancelable(false)
                    // finally, create the alert dialog and show it
                    val dialog = builder.create()
                    dialog.show()
                    dialog.dismiss()
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


        binding.tfItemName.addTextChangedListener {
            if (!binding.tfItemName.text.toString().trim().isEmpty()) {
                binding.tilItemName.error = null
            }
        }

        binding.tfItemCode.addTextChangedListener {
            if (!binding.tfItemCode.text.toString().trim().isEmpty()) {
                binding.tilItemCode.error = null
            }
        }

        binding.tfItemSalePrice.addTextChangedListener {
            if (!binding.tfItemSalePrice.text.toString().trim().isEmpty()) {
                binding.tilItemSalePrice.error = null
            }
        }

        binding.tfItemPurchasePrice.addTextChangedListener {
            if (!binding.tfItemPurchasePrice.text.toString().trim().isEmpty()) {
                binding.tilItemPurchasePrice.error = null
            }
        }

        binding.tfItemStock.addTextChangedListener {
            if (!binding.tfItemStock.text.toString().trim().isEmpty()) {
                binding.tilItemStock.error = null
            }
        }

        binding.actvItemSupplier.setOnClickListener {

            binding.tilItemSupplier.error = null

            val suppliersNames: MutableList<String> = ArrayList()
            aSuppliers.forEach {
                suppliersNames += it.name_supplier
            }
            val suppliersOptions = suppliersNames.toTypedArray()


            val builder = MaterialAlertDialogBuilder(requireContext())
            // dialog title
            builder.setTitle("Selecciona un proveedor")

            builder.setSingleChoiceItems(
                suppliersOptions, -1
            ) { dialog, i -> }

            // alert dialog positive button
            builder.setPositiveButton("Seleccionar") { dialog, which ->
                val position = (dialog as AlertDialog).listView.checkedItemPosition
                // if selected, then get item text
                if (position != -1) {
                    idSupplierSelected = aSuppliers[position].id_supplier
                    binding.actvItemSupplier.setText(aSuppliers[position].name_supplier)
                } else {
                    idSupplierSelected = -1
                    binding.actvItemSupplier.setText("Proveedor")
                }
            }

            // set dialog non cancelable
            builder.setCancelable(true)

            // finally, create the alert dialog and show it
            val dialog = builder.create()
            dialog.show()
        }


        binding.actvItemCategory.setOnClickListener {

            binding.tilItemCategory.error = null

            val categoriesNames: MutableList<String> = ArrayList()
            aCategories.forEach {
                categoriesNames += it.name_category
            }
            val categoriesOptions = categoriesNames.toTypedArray()


            val builder = MaterialAlertDialogBuilder(requireContext())
            // dialog title
            builder.setTitle("Selecciona una categoría")

            builder.setSingleChoiceItems(
                categoriesOptions, -1
            ) { dialog, i -> }

            // alert dialog positive button
            builder.setPositiveButton("Seleccionar") { dialog, which ->
                val position = (dialog as AlertDialog).listView.checkedItemPosition
                // if selected, then get item text
                if (position != -1) {
                    idCategorySelected = aCategories[position].id_category
                    binding.actvItemCategory.setText(aCategories[position].name_category)
                } else {
                    idCategorySelected = -1
                    binding.actvItemCategory.setText("Categoría")
                }
            }

            // set dialog non cancelable
            builder.setCancelable(true)

            // finally, create the alert dialog and show it
            val dialog = builder.create()
            dialog.show()
        }


        viewModel.addItemResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.success -> {
                    if (it.value.status == 200) {
                        binding.root.showToast("Se añadió el producto")
                        val action = AddItemFragmentDirections.actionAddItemFragmentToItemsFragment()
                        findNavController().navigate(action)
                    }
                }
                is Resource.failure -> {
                    if (it.errorCode == 409) {
                        binding.root.showToast("Ya existe un producto con ese código")
                    }
                }
            }
        }

        binding.btnAdd.setOnClickListener {

            if (validForm()) {
                val name = binding.tfItemName.text.toString().trim()
                val code = binding.tfItemCode.text.toString().trim()
                val sale = binding.tfItemSalePrice.text.toString().trim().toFloat()
                val purchase = binding.tfItemPurchasePrice.text.toString().trim().toFloat()
                val stock = binding.tfItemStock.text.toString().trim().toInt()

                viewModel.addItem(requireArguments().getInt("branchId"),
                    code,
                    name,
                    sale,
                    purchase,
                    stock,
                    idCategorySelected,
                    idSupplierSelected)

            }

        }

    }

    private fun validForm(): Boolean {
        var valid: Boolean = true
        val name = binding.tfItemName.text.toString().trim()
        val code = binding.tfItemCode.text.toString().trim()
        val sale = binding.tfItemSalePrice.text.toString().trim()
        val purchase = binding.tfItemPurchasePrice.text.toString().trim()
        val stock = binding.tfItemStock.text.toString().trim()


        valid = validField(binding.tfItemName, binding.tilItemName, listOf()) and
                validField(binding.tfItemCode, binding.tilItemCode, listOf(name)) and
                validField(binding.tfItemSalePrice,
                    binding.tilItemSalePrice,
                    listOf(name, code)) and
                validField(binding.tfItemPurchasePrice,
                    binding.tilItemPurchasePrice,
                    listOf(name, code, sale)) and
                validField(binding.tfItemStock,
                    binding.tilItemStock,
                    listOf(name, code, sale, purchase))

        if (idCategorySelected == -1) {
            valid = false
            binding.tilItemCategory.error = "Debes elegir una categoría"
        }
        if (idSupplierSelected == -1) {
            valid = false
            binding.tilItemSupplier.error = "Debes elegir un proveedor"
        }

        return valid
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

    override fun getViewModel(): Class<AddItemViewModel> = AddItemViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentAddItemBinding = FragmentAddItemBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): InventoryRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(InventoryApi::class.java, token)
        return InventoryRepository(api, userPreferences)
    }

}