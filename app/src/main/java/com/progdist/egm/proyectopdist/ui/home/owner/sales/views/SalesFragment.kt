package com.progdist.egm.proyectopdist.ui.home.owner.sales.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.network.SalesApi
import com.progdist.egm.proyectopdist.data.repository.SalesRepository
import com.progdist.egm.proyectopdist.data.responses.branches.Branch
import com.progdist.egm.proyectopdist.databinding.FragmentSalesBinding
import com.progdist.egm.proyectopdist.ui.CodeScannerAcitvity
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.owner.HomeActivity
import com.progdist.egm.proyectopdist.ui.home.owner.inventory.views.InventoryFragmentDirections
import com.progdist.egm.proyectopdist.ui.home.owner.sales.viewmodel.SalesViewModel
import com.progdist.egm.proyectopdist.ui.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SalesFragment : BaseFragment<SalesViewModel, FragmentSalesBinding, SalesRepository>() {


    lateinit var homeActivity: HomeActivity
    var selectedCategory: Branch? = null

    private val askCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if(it){
                val intent = Intent(requireActivity(), NewSaleActivity::class.java)
                requireActivity().startActivity(intent)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeActivity = (requireActivity() as HomeActivity)

        val collapsingToolbarLayout = requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout.title = "Caja"

        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
        heroIcon.setImageResource(R.drawable.ic_hero_sales)

        val fab: FloatingActionButton = requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton
        fab.visibility = View.GONE

        val idUser = activity?.intent?.getIntExtra("user", -1)

        val selectedBranchId = runBlocking { userPreferences.idSelectedBranch.first() }

        viewModel.getBranchesListResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.success->{
                    binding.tvWorkBranch.text = "Trabajando en ${it.value.result[0].name_branch}"
                }
                is Resource.failure->{

                }
            }
        }

        binding.btnNewPurchase.setOnClickListener {

            val permission = requireContext().checkCallingOrSelfPermission(android.Manifest.permission.CAMERA)
            if(permission == PackageManager.PERMISSION_GRANTED){

                val intent = Intent(requireActivity(), NewSaleActivity::class.java)
                intent.putExtra("branchId",selectedBranchId)
                intent.putExtra("userId",idUser)
                intent.putExtra("context","purchase")
                requireActivity().startActivity(intent)

            } else {
                binding.root.showToast("Se necesitan permisos para usar la cámara")
                askCameraPermission.launch(Manifest.permission.CAMERA)
            }

        }

        binding.btnNewSale.setOnClickListener {

            val permission = requireContext().checkCallingOrSelfPermission(android.Manifest.permission.CAMERA)
            if(permission == PackageManager.PERMISSION_GRANTED){

                val intent = Intent(requireActivity(), NewSaleActivity::class.java)
                intent.putExtra("branchId",selectedBranchId)
                intent.putExtra("userId",idUser)
                intent.putExtra("context","sale")
                requireActivity().startActivity(intent)

            } else {
                binding.root.showToast("Se necesitan permisos para usar la cámara")
                askCameraPermission.launch(Manifest.permission.CAMERA)
            }

        }

        binding.btnListSales.setOnClickListener {

            val action = SalesFragmentDirections.actionSalesFragmentToSalesListFragment("sale")
            findNavController().navigate(action)

        }

        binding.btnListPurchases.setOnClickListener {

            val action = SalesFragmentDirections.actionSalesFragmentToSalesListFragment("purchase")
            findNavController().navigate(action)

        }

        if(homeActivity.branches.isNotEmpty()){ //branches created
            //Check for selected branch
            if(homeActivity.selectedBranch != null){
                setButtonsState(true)
                if (selectedBranchId != null) {
                    viewModel.getBranch("id_branch", selectedBranchId)
                }
            }else {
                setButtonsState(false)
                binding.tvWorkBranch.text = "Selecciona una sucursal"

                val builder = MaterialAlertDialogBuilder(requireContext())
                builder.apply {
                    setTitle("Lugar de trabajo")
                    setMessage("Debes seleccionar una sucursal")
                    setPositiveButton("Seleccionar") { dialog, which ->
                        homeActivity.drawerLayout.openDrawer(GravityCompat.START)
                        val action =
                            SalesFragmentDirections.actionSalesFragmentToBranchesFragment()
                        findNavController().navigate(action)
                    }
                    setCancelable(false)
                }
                val dialog = builder.create()
                dialog.show()
            }
        }else{
            //No branches created
            setButtonsState(false)
            binding.tvWorkBranch.text = "Crea una sucursal"
        }


    }

    private fun setButtonsState(state: Boolean){
        binding.btnNewPurchase.isEnabled = state
        binding.btnNewSale.isEnabled = state
        binding.btnListPurchases.isEnabled = state
        binding.btnListSales.isEnabled = state
    }


    override fun getViewModel(): Class<SalesViewModel> = SalesViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSalesBinding = FragmentSalesBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): SalesRepository{
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(SalesApi::class.java, token)
        return SalesRepository(api,userPreferences)
    }

}