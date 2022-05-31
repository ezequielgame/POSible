package com.progdist.egm.proyectopdist.ui.home.owner.inventory.views


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.network.HomeApi
import com.progdist.egm.proyectopdist.data.network.InventoryApi
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.responses.inventory.Category
import com.progdist.egm.proyectopdist.data.responses.inventory.Supplier
import com.progdist.egm.proyectopdist.databinding.FragmentItemSummaryBinding
import com.progdist.egm.proyectopdist.ui.CodeScannerAcitvity
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.employee.view.EmployeeHomeActivity
import com.progdist.egm.proyectopdist.ui.home.owner.HomeActivity
import com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels.ItemSummaryViewModel
import com.progdist.egm.proyectopdist.ui.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ItemSummaryFragment : BaseFragment<ItemSummaryViewModel,FragmentItemSummaryBinding,InventoryRepository>() {

    private var idCategorySelected: Int = -1
    private var idSupplierSelected: Int = -1
    private var idUser: Int = -1
    private var roleId: Int = -1
    private lateinit var aCategories: Array<Category>
    private lateinit var aSuppliers: Array<Supplier>
    private lateinit var homeActivity: AppCompatActivity

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
        roleId = requireArguments().getInt("roleId")
        when(activity){
            is HomeActivity->{
                homeActivity = requireActivity() as HomeActivity
            }
            is EmployeeHomeActivity->{
                homeActivity = requireActivity() as EmployeeHomeActivity
            }
        }

        if(homeActivity is HomeActivity){
            idUser = activity?.intent?.getIntExtra("user", -1)!!
        }else if(homeActivity is EmployeeHomeActivity){
            idUser = (homeActivity as EmployeeHomeActivity).userId!!
        }


        val collapsingToolbarLayout =
            requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout.title = "Editar producto"

        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
        heroIcon.setImageResource(R.drawable.ic_item)

        val fab: FloatingActionButton =
            requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton
        fab.setImageResource(R.drawable.ic_edit)
        if(roleId!=3){
            fab.visibility = View.VISIBLE
        }else{
            fab.visibility = View.GONE
        }


        fab.setOnClickListener {
            setTextFieldsState(true)
            fab.visibility = View.GONE
            setTextInputLayoutsHelperText("Requerido*")
            binding.tilItemName.visibility = View.VISIBLE
            binding.btnSave.visibility = View.VISIBLE
        }

        setTextFieldsState(false)
        setTextInputLayoutsHelperText("")

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

        viewModel.getItems("id_item", requireArguments().getInt("itemId"))

        viewModel.getItemsResponse.observe(viewLifecycleOwner){ getResponse ->
            when (getResponse) {
                is Resource.success -> {
                    collapsingToolbarLayout.title = getResponse.value.result[0].name_item
                    binding.tfItemName.setText(getResponse.value.result[0].name_item)
                    binding.tfItemCode.setText(getResponse.value.result[0].code_item)
                    binding.tfItemSalePrice.setText(getResponse.value.result[0].sale_price_item.toString())
                    binding.tfItemPurchasePrice.setText(getResponse.value.result[0].purchase_price_item.toString())
                    binding.tfItemStock.setText(getResponse.value.result[0].stock_item.toString())
                    binding.actvItemCategory.setText(getResponse.value.result[0].name_category)
                    binding.actvItemSupplier.setText(getResponse.value.result[0].name_supplier)
                    idCategorySelected = getResponse.value.result[0].id_category_item
                    idSupplierSelected = getResponse.value.result[0].id_supplier_item
                }
                is Resource.failure -> {

                }
            }
        }

        viewModel.getCategories("id_user_category", idUser)

        viewModel.getCategoriesResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.success -> {
                    aCategories = it.value.result.toTypedArray()
                }
                is Resource.failure -> {

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

                }
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

        binding.btnSave.setOnClickListener{

            if(validForm()){
                saveInfo()
                fab.visibility = View.VISIBLE
                setTextFieldsState(false)
                setTextInputLayoutsHelperText("")
                binding.tilItemName.visibility = View.GONE
                binding.btnSave.visibility = View.GONE
                viewModel.getItems("id_item", requireArguments().getInt("itemId"))
            }

        }

        viewModel.editItemResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.success -> {

                }
                is Resource.failure -> {
                    if(it.errorCode == 409){
                        binding.root.showToast("No se editó porque ya había un producto con ese codigo")
                    }
                }
            }
        }

        
    }

    private fun saveInfo(){
        val name = binding.tfItemName.text.toString().trim()
        val code = binding.tfItemCode.text.toString().trim()
        val sale = binding.tfItemSalePrice.text.toString().trim().toFloat()
        val purchase = binding.tfItemPurchasePrice.text.toString().trim().toFloat()
        val stock = binding.tfItemStock.text.toString().trim().toFloat().toInt()

        viewModel.editItem(
            requireArguments().getInt("itemId"),
            "id_item",
            code,
            name,
            sale,
            purchase,
            stock,
            idCategorySelected,
            idSupplierSelected)

        Toast.makeText(requireContext(), "Se guardó la información", Toast.LENGTH_SHORT).show()
    }

    private fun setTextFieldsState(state: Boolean){
        binding.tilItemName.isEnabled = state
        binding.tilItemCode.isEnabled = state
        binding.tilItemSalePrice.isEnabled = state
        binding.tilItemPurchasePrice.isEnabled = state
        binding.tilItemStock.isEnabled = state
        binding.tilItemCategory.isEnabled = state
        binding.tilItemSupplier.isEnabled = state
        binding.btnCodeScanner.isEnabled = state
    }

    private fun setTextInputLayoutsHelperText(text: String){
        binding.tilItemName.helperText = text
        binding.tilItemCode.helperText = text
        binding.tilItemSalePrice.helperText = text
        binding.tilItemPurchasePrice.helperText = text
        binding.tilItemStock.helperText = text
        binding.tilItemCategory.helperText = text
        binding.tilItemSupplier.helperText = text
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
    
    override fun getViewModel(): Class<ItemSummaryViewModel> = ItemSummaryViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentItemSummaryBinding = FragmentItemSummaryBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): InventoryRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(InventoryApi::class.java,token)
        return InventoryRepository(api,userPreferences)
    }

}