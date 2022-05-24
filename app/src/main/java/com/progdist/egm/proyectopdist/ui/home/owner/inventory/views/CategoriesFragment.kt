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
import com.progdist.egm.proyectopdist.data.network.InventoryApi
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.databinding.FragmentCategoriesBinding
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.owner.branches.BranchesFragmentDirections
import com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels.CategoriesViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class CategoriesFragment : BaseFragment<CategoriesViewModel, FragmentCategoriesBinding, InventoryRepository>() {

    lateinit var recyclerAdapter: CategoriesListAdapter
    var selectedBranchId: Int? = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        val collapsingToolbarLayout =
            requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout.title = "Categorías"

        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
        heroIcon.setImageResource(R.drawable.ic_hero_categories)

        selectedBranchId = runBlocking { userPreferences.idSelectedBranch.first() }

        val fab: FloatingActionButton =
            requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton
        fab.visibility = View.VISIBLE

        fab.setImageResource(R.drawable.ic_add)

        fab.setOnClickListener {
            val action = CategoriesFragmentDirections.actionCategoriesFragmentToAddCategoryFragment(selectedBranchId!!)
            findNavController().navigate(action)
        }
        val idUser = activity?.intent?.getIntExtra("user", -1)



        viewModel.getCategories("id_user_category", idUser!!)

        viewModel.getCategoriesResponse.observe(viewLifecycleOwner){ getResponse ->
            when (getResponse) {
                is Resource.success -> {
                    if (getResponse.value.status != 404) {
//                        binding.branchesRecycler.visibility = View.VISIBLE
                        binding.tvNoCategories.visibility = View.GONE
                        recyclerAdapter.setCategoriesList(getResponse.value.result)
                        recyclerAdapter.notifyDataSetChanged()
                    }
                }
                is Resource.failure -> {
                    if(getResponse.errorCode == 404){
//                        binding.branchesRecycler.visibility = View.GONE
                        recyclerAdapter.setCategoriesList(arrayListOf())
                        recyclerAdapter.notifyDataSetChanged()
                        binding.tvNoCategories.visibility = View.VISIBLE
                    }
                }
            }
        }


        viewModel.deleteCategoryResponse.observe(viewLifecycleOwner){ delResponse->
            when(delResponse){
                is Resource.success->{
                    if(delResponse.value.result.msg == "Deleted"){
                        viewModel.getCategories("id_user_category", idUser)
                    }
                }
                is Resource.failure->{

                }
            }
        }

        recyclerAdapter.setOnItemClickListener(object : CategoriesListAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                val categoryId: Int = recyclerAdapter.getCategory(position).id_category

                val action = CategoriesFragmentDirections.actionCategoriesFragmentToCategorySummaryFragment(categoryId)
                findNavController().navigate(action)

            }
        })


        recyclerAdapter.setOnItemLongClickListener(object :
            CategoriesListAdapter.onItemLongClickListener {
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
                                        val idDelete = recyclerAdapter.getCategory(position).id_category
                                        viewModel.deleteCategory(idDelete, "id_category")
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

        binding.categoriesRecycler.layoutManager = LinearLayoutManager(requireContext())
        recyclerAdapter = CategoriesListAdapter()
        binding.categoriesRecycler.adapter = recyclerAdapter

    }

    override fun getViewModel(): Class<CategoriesViewModel> = CategoriesViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentCategoriesBinding = FragmentCategoriesBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): InventoryRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(InventoryApi::class.java, token)
        return InventoryRepository(api, userPreferences)
    }

}