package com.progdist.egm.proyectopdist.ui.home.owner.employees

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.network.ManageEmployeesApi
import com.progdist.egm.proyectopdist.data.repository.ManageEmployeesRepository
import com.progdist.egm.proyectopdist.databinding.FragmentManageEmployeesBinding
import com.progdist.egm.proyectopdist.ui.base.BaseFragment

class ManageEmployeesFragment : BaseFragment<ManageEmployeesViewModel, FragmentManageEmployeesBinding, ManageEmployeesRepository>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val collapsingToolbarLayout = requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout.title = "Empleados"

        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
        heroIcon.setImageResource(R.drawable.ic_hero_employees)

        val fab: FloatingActionButton = requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton
        fab.visibility = View.VISIBLE

        fab.setImageResource(R.drawable.ic_add_employee)

        fab.setOnClickListener {
            Toast.makeText(requireContext(), "Añadir empleado", Toast.LENGTH_SHORT).show()
        }



    }

    override fun getViewModel(): Class<ManageEmployeesViewModel> = ManageEmployeesViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentManageEmployeesBinding = FragmentManageEmployeesBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): ManageEmployeesRepository = ManageEmployeesRepository(remoteDataSource.buildApi(ManageEmployeesApi::class.java))

}