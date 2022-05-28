package com.progdist.egm.proyectopdist.ui.home.owner.inventory.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.progdist.egm.proyectopdist.databinding.FragmentItemsBinding
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels.ItemsViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ItemsFragment : BaseFragment<ItemsViewModel,FragmentItemsBinding,InventoryRepository>() {

    lateinit var recyclerAdapter: ItemsListAdapter
    var selectedBranchId: Int? = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        val collapsingToolbarLayout =
            requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout.title = "Productos"

        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
        heroIcon.setImageResource(R.drawable.ic_hero_categories)

        val fab: FloatingActionButton =
            requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton
        fab.visibility = View.VISIBLE

        fab.setImageResource(R.drawable.ic_add)

        fab.setOnClickListener {
            val action = ItemsFragmentDirections.actionItemsFragmentToAddItemFragment(selectedBranchId!!)
            findNavController().navigate(action)
        }
        val idUser = activity?.intent?.getIntExtra("user", -1)

        recyclerAdapter.setOnItemClickListener(object : ItemsListAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                val itemId: Int = recyclerAdapter.getItem(position).id_item

                val action = ItemsFragmentDirections.actionItemsFragmentToItemSummaryFragment(itemId)
                findNavController().navigate(action)

            }
        })

        selectedBranchId = runBlocking { userPreferences.idSelectedBranch.first() }

        viewModel.getItems("id_branch_item", selectedBranchId!!)

        viewModel.getItemsResponse.observe(viewLifecycleOwner){ getResponse ->
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
                                    setMessage("Se eliminará la categoría y todos sus productos. No se podrá recuperar")
                                    setPositiveButton("Eliminar") { view, _ ->
                                        val idDelete = recyclerAdapter.getItem(position).id_item
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