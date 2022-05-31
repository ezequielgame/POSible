package com.progdist.egm.proyectopdist.ui.home.owner.inventory.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
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
import com.progdist.egm.proyectopdist.ui.home.employee.view.EmployeeHomeActivity
import com.progdist.egm.proyectopdist.ui.home.owner.HomeActivity
import com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels.InventoryViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class InventoryFragment : BaseFragment<InventoryViewModel, FragmentInventoryBinding, InventoryRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedBranchId = runBlocking { userPreferences.idSelectedBranch.first() }

        val homeActivity: AppCompatActivity
        when(activity){
            is HomeActivity->{
                homeActivity = requireActivity() as HomeActivity
            }
            else->{
                homeActivity = requireActivity() as EmployeeHomeActivity
            }
        }
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

        binding.btnSuppliers.setOnClickListener{

            val action = InventoryFragmentDirections.actionInventoryFragmentToSuppliersFragment()
            findNavController().navigate(action)

        }

        binding.btnItems.setOnClickListener{
            val action: Any
            if(homeActivity is HomeActivity){
                action =
                    InventoryFragmentDirections.actionInventoryFragmentToItemsFragment(1)
                findNavController().navigate(action)
            }else if(homeActivity is EmployeeHomeActivity){
                action =
                    InventoryFragmentDirections.actionInventoryFragmentToItemsFragment(homeActivity.employeeRoleId!!)
                findNavController().navigate(action)
            }


        }

        binding.btnCategories.setOnClickListener{

            val action: Any
            if(homeActivity is HomeActivity){
                action =
                    InventoryFragmentDirections.actionInventoryFragmentToCategoriesFragment(1)
                findNavController().navigate(action)
            }else if(homeActivity is EmployeeHomeActivity){
                action =
                    InventoryFragmentDirections.actionInventoryFragmentToCategoriesFragment(homeActivity.employeeRoleId!!)
                findNavController().navigate(action)
            }

        }


        if(homeActivity is HomeActivity){
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
        } else if(homeActivity is EmployeeHomeActivity){
            if (selectedBranchId != null) {
                setButtonsState(true)
                viewModel.getBranchInInventory("id_branch", selectedBranchId)
                when(homeActivity.employeeRoleId){
                    1->{
                    }
                    2->{
                        binding.btnSuppliers.visibility = View.GONE
                    }
                    3->{
                        binding.btnSuppliers.visibility = View.GONE
                    }
                }
            }
        }


    }

    private fun setButtonsState(state: Boolean){
        binding.btnSuppliers.isEnabled = state
        binding.btnItems.isEnabled = state
        binding.btnCategories.isEnabled = state
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