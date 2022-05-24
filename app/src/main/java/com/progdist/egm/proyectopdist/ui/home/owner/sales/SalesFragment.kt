package com.progdist.egm.proyectopdist.ui.home.owner.sales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.network.SalesApi
import com.progdist.egm.proyectopdist.data.repository.SalesRepository
import com.progdist.egm.proyectopdist.databinding.FragmentSalesBinding
import com.progdist.egm.proyectopdist.ui.base.BaseFragment

class SalesFragment : BaseFragment<SalesViewModel, FragmentSalesBinding, SalesRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val collapsingToolbarLayout = requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout.title = "Caja"

        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
        heroIcon.setImageResource(R.drawable.ic_hero_sales)

        val fab: FloatingActionButton = requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton
        fab.visibility = View.GONE

    }


    override fun getViewModel(): Class<SalesViewModel> = SalesViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSalesBinding = FragmentSalesBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): SalesRepository = SalesRepository(remoteDataSource.buildApi(SalesApi::class.java))

}