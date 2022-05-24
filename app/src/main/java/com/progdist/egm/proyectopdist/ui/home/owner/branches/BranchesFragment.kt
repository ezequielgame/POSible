package com.progdist.egm.proyectopdist.ui.home.owner.branches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.adapter.BranchesListAdapter
import com.progdist.egm.proyectopdist.data.network.BranchesApi
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.BranchesRepository
import com.progdist.egm.proyectopdist.data.responses.branches.DeleteBranchResponse
import com.progdist.egm.proyectopdist.databinding.FragmentBranchesBinding
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.owner.HomeActivity
import com.progdist.egm.proyectopdist.ui.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class BranchesFragment :
    BaseFragment<BranchesViewModel, FragmentBranchesBinding, BranchesRepository>() {

    lateinit var recyclerAdapter: BranchesListAdapter
    var selectedBranchId: Int? = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        val collapsingToolbarLayout =
            requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout.title = "Sucursales"

        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
        heroIcon.setImageResource(R.drawable.ic_undraw_world_re_768g)

        val fab: FloatingActionButton =
            requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton
        fab.visibility = View.VISIBLE

        fab.setImageResource(R.drawable.ic_round_add_business_24)

        fab.setOnClickListener {
            val action = BranchesFragmentDirections.actionBranchesFragmentToAddBranchFragment()
            findNavController().navigate(action)
        }
        val idUser = activity?.intent?.getIntExtra("user", -1)

        selectedBranchId = runBlocking { userPreferences.idSelectedBranch.first() }

//        if(selectedBranchId != -1 && selectedBranchId != null){
//            viewModel.getBranch(selectedBranchId!!)
//        }

        viewModel.getBranchesList("id_user_branch", idUser.toString())

        viewModel.getBranchesListResponse.observe(viewLifecycleOwner){ getResponse ->
            when (getResponse) {
                is Resource.success -> {
                    if (getResponse.value.status != 404) {
//                        binding.branchesRecycler.visibility = View.VISIBLE
                        binding.tvNoBranches.visibility = View.GONE
                        recyclerAdapter.setBranchesList(getResponse.value.result)
                        (activity as HomeActivity?)!!.branches = getResponse.value.result.toTypedArray()
                        recyclerAdapter.notifyDataSetChanged()
                    }
                }
                is Resource.failure -> {
                    if(getResponse.errorCode == 404){
//                        binding.branchesRecycler.visibility = View.GONE
                        recyclerAdapter.setBranchesList(arrayListOf())
                        recyclerAdapter.notifyDataSetChanged()
                        binding.tvNoBranches.visibility = View.VISIBLE
                    }
                }
            }
        }

        viewModel.deleteBranchResponse.observe(viewLifecycleOwner){ delResponse->
            when(delResponse){
                is Resource.success->{
                    if(delResponse.value.result.msg == "Deleted"){
                        viewModel.getBranchesList("id_user_branch", idUser.toString())

                    }
                }
                is Resource.failure->{

                }
            }
        }

        recyclerAdapter.setOnItemClickListener(object : BranchesListAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                val branchId: Int = recyclerAdapter.getBranch(position).id_branch

                val action =
                    BranchesFragmentDirections.actionBranchesFragmentToBranchSummaryFragment(
                        branchId)
                findNavController().navigate(action)

            }
        })

        recyclerAdapter.setOnItemLongClickListener(object :
            BranchesListAdapter.onItemLongClickListener {
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
                                    setMessage("Se eliminará la sucursal y no se podrá recuperar")
                                    setPositiveButton("Eliminar") { view, _ ->
                                        val idDelete = recyclerAdapter.getBranch(position).id_branch
                                        val idLocationDelete = recyclerAdapter.getBranch(position).id_location_branch
                                        viewModel.deleteBranch(idDelete, "id_branch")
                                        if(selectedBranchId != -1 && selectedBranchId != null){
                                            if(selectedBranchId == idDelete){
                                                (requireActivity() as HomeActivity).selectedBranch = null
                                                (requireActivity() as HomeActivity).headerBranch.text ="Selecciona una sucursal"
                                                viewModel.deleteIdWorkingBranch()
                                            }
                                        }
                                        viewModel.deleteBranchLocation(idLocationDelete,"id_location")
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

        binding.branchesRecycler.layoutManager = LinearLayoutManager(requireContext())
        recyclerAdapter = BranchesListAdapter()
        binding.branchesRecycler.adapter = recyclerAdapter

    }

    override fun getViewModel(): Class<BranchesViewModel> = BranchesViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentBranchesBinding = FragmentBranchesBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): BranchesRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(BranchesApi::class.java, token)
        return BranchesRepository(api,userPreferences)
    }

}