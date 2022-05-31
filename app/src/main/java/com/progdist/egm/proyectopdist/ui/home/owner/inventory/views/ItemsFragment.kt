package com.progdist.egm.proyectopdist.ui.home.owner.inventory.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.adapter.CategoriesListAdapter
import com.progdist.egm.proyectopdist.adapter.ItemsListAdapter
import com.progdist.egm.proyectopdist.data.network.InventoryApi
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.responses.generic.DeleteResponse
import com.progdist.egm.proyectopdist.databinding.FragmentItemsBinding
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.employee.view.EmployeeHomeActivity
import com.progdist.egm.proyectopdist.ui.home.owner.HomeActivity
import com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels.ItemsViewModel
import com.progdist.egm.proyectopdist.ui.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ItemsFragment : BaseFragment<ItemsViewModel,FragmentItemsBinding,InventoryRepository>() {

    lateinit var recyclerAdapter: ItemsListAdapter
    var selectedBranchId: Int? = -1
    var roleId: Int? = -1
    lateinit var homeActivity: AppCompatActivity

    val delObserver = {delResponse: Resource<DeleteResponse>->
        when(delResponse){
            is Resource.success->{
                if(delResponse.value.result.msg == "Deleted"){
                    viewModel.getItems("id_branch_item", selectedBranchId!!)
                }
            }
            is Resource.failure->{
                if(delResponse.errorCode == 405){
                    val builder = MaterialAlertDialogBuilder(requireContext())
                    builder.apply {
                        setTitle("Error")
                        setMessage("No puedes eliminar este producto, primero, elimina los registros de las ventas asociados a este producto.")
                        setPositiveButton("Cerrar") { dialog, which ->
                            findNavController().clearBackStack(R.id.itemsFragment)
                            val action = ItemsFragmentDirections.actionItemsFragmentSelf(roleId!!)
                            findNavController().navigate(action)
                        }
                        setCancelable(false)
                    }
                    val dialog = builder.create()
                    dialog.show()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        roleId = requireArguments().getInt("roleId")

        val collapsingToolbarLayout =
            requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout.title = "Productos"

        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
        heroIcon.setImageResource(R.drawable.ic_hero_categories)

        val fab: FloatingActionButton =
            requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton
        fab.setImageResource(R.drawable.ic_add)

        if(roleId != 3){
            fab.visibility = View.VISIBLE
        }else{
            fab.visibility = View.GONE
        }


        fab.setOnClickListener {
            val action = ItemsFragmentDirections.actionItemsFragmentToAddItemFragment(selectedBranchId!!,roleId!!)
            findNavController().navigate(action)
        }
        when(activity){
            is HomeActivity ->{
                homeActivity = requireActivity() as HomeActivity
            }
            is EmployeeHomeActivity ->{
                homeActivity = requireActivity() as EmployeeHomeActivity
            }
        }

        val idUser: Int
        if(homeActivity is HomeActivity){
            idUser = activity?.intent?.getIntExtra("user", -1)!!
        }else if(homeActivity is EmployeeHomeActivity){
            idUser = (homeActivity as EmployeeHomeActivity).userId!!
        }

        recyclerAdapter.setOnItemClickListener(object : ItemsListAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                val itemId: Int = recyclerAdapter.getItem(position).id_item
                val action =
                    ItemsFragmentDirections.actionItemsFragmentToItemSummaryFragment(itemId,
                        roleId!!)
                findNavController().navigate(action)
            }
        })

        selectedBranchId = runBlocking { userPreferences.idSelectedBranch.first() }

        viewModel.getItems("id_branch_item", selectedBranchId!!)


        viewModel.getItemsResponse.observe(viewLifecycleOwner) { getResponse ->
            when (getResponse) {
                is Resource.success -> {
                    if (getResponse.value.status != 404) {
                        binding.tvNoItems.visibility = View.GONE
                        recyclerAdapter.setItemsList(getResponse.value.result)
                        recyclerAdapter.notifyDataSetChanged()
                    }
                }
                is Resource.failure -> {
                    if(getResponse.errorCode == 404){
                        recyclerAdapter.setItemsList(arrayListOf())
                        recyclerAdapter.notifyDataSetChanged()
                        binding.tvNoItems.visibility = View.VISIBLE
                    }
                }
            }
        }

//        viewModel.deleteCategoryResponse.observe(viewLifecycleOwner){ delResponse->
//            when(delResponse){
//                is Resource.success->{
//                    if(delResponse.value.result.msg == "Deleted"){
//                        viewModel.getCategories("id_user_category", idUser)
//                    }
//                }
//                is Resource.failure->{
//
//                }
//            }
//        }




        recyclerAdapter.setOnItemLongClickListener(object :
            ItemsListAdapter.onItemLongClickListener {
            override fun onItemLongClick(position: Int): Boolean {
                if(roleId != 3){
                    val builder = MaterialAlertDialogBuilder(requireContext())
                    val options = arrayOf("Eliminar")
                    builder.apply {
                        setTitle("Selecciona una opcion")
                        setPositiveButton("Cerrar") { dialog, which -> }
                        setItems(options) { dialog, which ->
                            when (which) {
                                0 -> {
                                    val deleteBuilder = MaterialAlertDialogBuilder(requireContext())
                                    deleteBuilder.apply {
                                        setTitle("Eliminar")
                                        setMessage("Se eliminará el producto. No se podrá recuperar")
                                        setPositiveButton("Eliminar") { view, _ ->
                                            val idDelete = recyclerAdapter.getItem(position).id_item
                                            viewModel.deleteItem(idDelete,"id_item")
                                            viewModel.deleteItemResponse.observe(viewLifecycleOwner,delObserver)
//                                        viewModel.deleteCategory(idDelete, "id_category")
                                        }
                                        setNegativeButton("Cancelar") { view, _ ->

                                        }
                                        setCancelable(true)
                                        create()
                                        show()
                                    }
                                }
                            }
                        }
                        setCancelable(true)
                    }
                    val dialog = builder.create()
                    dialog.show()
                }
                return true
            }
        })

    }

    private fun initRecyclerView() {

        binding.itemsRecycler.layoutManager = LinearLayoutManager(requireContext())
        recyclerAdapter = ItemsListAdapter()
        binding.itemsRecycler.adapter = recyclerAdapter

    }

    override fun getViewModel(): Class<ItemsViewModel> = ItemsViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentItemsBinding = FragmentItemsBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): InventoryRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(InventoryApi::class.java, token)
        return InventoryRepository(api, userPreferences)
    }

}