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
import com.progdist.egm.proyectopdist.adapter.SuppliersListAdapter
import com.progdist.egm.proyectopdist.data.network.InventoryApi
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.databinding.FragmentSuppliersBinding
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels.SuppliersViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SuppliersFragment : BaseFragment<SuppliersViewModel,FragmentSuppliersBinding,InventoryRepository>() {

    lateinit var recyclerAdapter: SuppliersListAdapter
    var selectedBranchId: Int? = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        val collapsingToolbarLayout =
            requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout.title = "Proveedores"

        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
        heroIcon.setImageResource(R.drawable.ic_hero_suppliers)

        selectedBranchId = runBlocking { userPreferences.idSelectedBranch.first() }
        val idUser = activity?.intent?.getIntExtra("user", -1)

        val fab: FloatingActionButton =
            requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton
        fab.visibility = View.VISIBLE

        fab.setImageResource(R.drawable.ic_add)

        fab.setOnClickListener {
            val action = SuppliersFragmentDirections.actionSuppliersFragmentToAddSupplierFragment(idUser!!)
            findNavController().navigate(action)
        }

        viewModel.getSuppliers("id_user_supplier", idUser!!)

        viewModel.getSuppliersResponse.observe(viewLifecycleOwner){ getResponse ->
            when (getResponse) {
                is Resource.success -> {
                    if (getResponse.value.status != 404) {
                        binding.tvNoSuppliers.visibility = View.GONE
                        recyclerAdapter.setSuppliersList(getResponse.value.result)
                        recyclerAdapter.notifyDataSetChanged()
                    }
                }
                is Resource.failure -> {
                    if(getResponse.errorCode == 404){
                        recyclerAdapter.setSuppliersList(arrayListOf())
                        recyclerAdapter.notifyDataSetChanged()
                        binding.tvNoSuppliers.visibility = View.VISIBLE
                    }
                }
            }
        }


        viewModel.deleteSupplierResponse.observe(viewLifecycleOwner){ delResponse->
            when(delResponse){
                is Resource.success->{
                    if(delResponse.value.result.msg == "Deleted"){
                        viewModel.getSuppliers("id_user_supplier", idUser)
                    }
                }
                is Resource.failure->{

                }
            }
        }


        recyclerAdapter.setOnItemClickListener(object : SuppliersListAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                val supplierId: Int = recyclerAdapter.getSupplier(position).id_supplier

                val action = SuppliersFragmentDirections.actionSuppliersFragmentToSupplierSummaryFragment(supplierId)
                findNavController().navigate(action)

            }
        })

        recyclerAdapter.setOnItemLongClickListener(object :
            SuppliersListAdapter.onItemLongClickListener {
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
                                    setMessage("Se eliminará el proveedor, y TODOS sus productos relaciondos y no se podrá recuperar")
                                    setPositiveButton("Eliminar") { view, _ ->
                                        val idDelete = recyclerAdapter.getSupplier(position).id_supplier
                                        viewModel.deleteSupplier(idDelete, "id_supplier")
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

        binding.suppliersRecycler.layoutManager = LinearLayoutManager(requireContext())
        recyclerAdapter = SuppliersListAdapter()
        binding.suppliersRecycler.adapter = recyclerAdapter

    }

    override fun getViewModel(): Class<SuppliersViewModel> = SuppliersViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSuppliersBinding = FragmentSuppliersBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): InventoryRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(InventoryApi::class.java,token)
        return InventoryRepository(api, userPreferences)
    }

}