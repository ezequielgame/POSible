package com.progdist.egm.proyectopdist.ui.home.owner.employees.views

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
import com.progdist.egm.proyectopdist.adapter.EmployeesListAdapter
import com.progdist.egm.proyectopdist.data.network.ManageEmployeesApi
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.ManageEmployeesRepository
import com.progdist.egm.proyectopdist.databinding.FragmentManageEmployeesBinding
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.owner.employees.viewmodel.ManageEmployeesViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ManageEmployeesFragment : BaseFragment<ManageEmployeesViewModel, FragmentManageEmployeesBinding, ManageEmployeesRepository>() {

    lateinit var recyclerAdapter: EmployeesListAdapter
    var selectedBranchId: Int? = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedBranchId = runBlocking { userPreferences.idSelectedBranch.first() }
        val idUser = activity?.intent?.getIntExtra("user", -1)
        initRecyclerView()

        val collapsingToolbarLayout = requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout.title = "Empleados"

        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
        heroIcon.setImageResource(R.drawable.ic_hero_employees)

        val fab: FloatingActionButton = requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton
        fab.visibility = View.VISIBLE

        fab.setImageResource(R.drawable.ic_add_employee)

        fab.setOnClickListener {
            val action = ManageEmployeesFragmentDirections.actionManageEmployeesFragmentToAddEmployeeFragment(idUser!!)
            findNavController().navigate(action)
        }

        viewModel.getEmployees("id_user_employee", idUser!!.toString())

        viewModel.getEmployeesResponse.observe(viewLifecycleOwner){ getResponse ->
            when (getResponse) {
                is Resource.success -> {
                    if (getResponse.value.status != 404) {
                        binding.tvNoEmployees.visibility = View.GONE
                        recyclerAdapter.setEmployeesList(getResponse.value.result)
                        recyclerAdapter.notifyDataSetChanged()
                    }
                }
                is Resource.failure -> {
                    if(getResponse.errorCode == 404){
                        recyclerAdapter.setEmployeesList(arrayListOf())
                        recyclerAdapter.notifyDataSetChanged()
                        binding.tvNoEmployees.visibility = View.VISIBLE
                    }
                }
            }
        }


        viewModel.deleteEmployeeResponse.observe(viewLifecycleOwner){ delResponse->
            when(delResponse){
                is Resource.success->{
                    if(delResponse.value.result.msg == "Deleted"){
                        viewModel.getEmployees("id_user_employee", idUser.toString()) //Update
                    }
                }
                is Resource.failure->{

                }
            }
        }

        recyclerAdapter.setOnItemClickListener(object : EmployeesListAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                val employeeId: Int = recyclerAdapter.getEmployee(position).id_employee

                val action = ManageEmployeesFragmentDirections.actionManageEmployeesFragmentToEmployeeSummaryFragment(employeeId,idUser)
                findNavController().navigate(action)

            }
        })


        recyclerAdapter.setOnItemLongClickListener(object :
            EmployeesListAdapter.onItemLongClickListener {
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
                                    setMessage("Se eliminará el empleado. No se podrá recuperar")
                                    setPositiveButton("Eliminar") { view, _ ->
                                        val idDelete = recyclerAdapter.getEmployee(position).id_employee
                                        viewModel.deleteEmployee(idDelete, "id_employee")
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
        recyclerAdapter = EmployeesListAdapter()
        binding.categoriesRecycler.adapter = recyclerAdapter

    }

    override fun getViewModel(): Class<ManageEmployeesViewModel> = ManageEmployeesViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentManageEmployeesBinding = FragmentManageEmployeesBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): ManageEmployeesRepository{
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ManageEmployeesApi::class.java,token)
        return ManageEmployeesRepository(api,userPreferences)
    }

}