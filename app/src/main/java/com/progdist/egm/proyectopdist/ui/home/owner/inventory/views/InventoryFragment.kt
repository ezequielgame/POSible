package com.progdist.egm.proyectopdist.ui.home.owner.inventory.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.network.InventoryApi
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.responses.branches.Branch
import com.progdist.egm.proyectopdist.databinding.FragmentInventoryBinding
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.owner.HomeActivity
import com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels.InventoryViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class InventoryFragment : BaseFragment<InventoryViewModel, FragmentInventoryBinding, InventoryRepository>() {

    lateinit var homeActivity: HomeActivity
    var selectedCategory: Branch? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeActivity = (requireActivity() as HomeActivity)
        val collapsingToolbarLayout = requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout.title = "Inventario"

        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
        heroIcon.setImageResource(R.drawable.ic_hero_inventory)

        val fab: FloatingActionButton = requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton
        fab.visibility = View.GONE

        viewModel.getBranchesListResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.success->{
                    binding.tvWorkBranch.text = "Trabajando en ${it.value.result[0].name_branch}"
                }
                is Resource.failure->{

                }
            }
        }

        binding.btnItems.setOnClickListener{

            val action =
                InventoryFragmentDirections.actionInventoryFragmentToItemsFragment()
            findNavController().navigate(action)

        }

        binding.btnCategories.setOnClickListener{

            val action =
                InventoryFragmentDirections.actionInventoryFragmentToCategoriesFragment()
            findNavController().navigate(action)

        }

        binding.btnSuppliers.setOnClickListener{

            val action = InventoryFragmentDirections.actionInventoryFragmentToSuppliersFragment()
            findNavController().navigate(action)

        }

        val selectedBranchId = runBlocking { userPreferences.idSelectedBranch.first() }

        if(homeActivity.branches.isNotEmpty()){ //branches created
            //Check for selected branch
            if(homeActivity.selectedBranch != null){
                binding.btnCategories.isEnabled = true
                binding.btnItems.isEnabled = true
                binding.btnSuppliers.isEnabled = true
                if (selectedBranchId != null) {
                    viewModel.getBranchInInventory("id_branch", selectedBranchId)
                }
            }else {
                binding.btnCategories.isEnabled = false
                binding.btnItems.isEnabled = false
                binding.btnSuppliers.isEnabled = false
                binding.tvWorkBranch.text = "Selecciona una sucursal"

                val builder = MaterialAlertDialogBuilder(requireContext())
                builder.apply {
                    setTitle("Lugar de trabajo")
                    setMessage("Debes seleccionar una sucursal")
                    setPositiveButton("Seleccionar") { dialog, which ->
                        homeActivity.drawerLayout.openDrawer(GravityCompat.START)
                        val action =
                            InventoryFragmentDirections.actionInventoryFragmentToBranchesFragment()
                        findNavController().navigate(action)
                    }
                    setCancelable(false)
                }
                val dialog = builder.create()
                dialog.show()
            }
        }else{
            //No branches created
            binding.btnCategories.isEnabled = false
            binding.btnItems.isEnabled = false
            binding.btnSuppliers.isEnabled = false
            binding.tvWorkBranch.text = "Crea una sucursal"
        }
    }

    override fun getViewModel(): Class<InventoryViewModel> = InventoryViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentInventoryBinding = FragmentInventoryBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): InventoryRepository{
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(InventoryApi::class.java, token)
        return InventoryRepository(api, userPreferences)
    }

}