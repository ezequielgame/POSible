package com.progdist.egm.proyectopdist.ui.home.owner.sales.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.adapter.ItemsListAdapter
import com.progdist.egm.proyectopdist.adapter.SalesListItemsListAdapter
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.network.SalesApi
import com.progdist.egm.proyectopdist.data.repository.SalesRepository
import com.progdist.egm.proyectopdist.databinding.FragmentSalesListBinding
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.owner.HomeActivity
import com.progdist.egm.proyectopdist.ui.home.owner.inventory.views.ItemsFragmentDirections
import com.progdist.egm.proyectopdist.ui.home.owner.sales.viewmodel.SalesListViewModel
import com.progdist.egm.proyectopdist.ui.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.internal.notify
import java.text.SimpleDateFormat
import java.util.*

class SalesListFragment : BaseFragment<SalesListViewModel,FragmentSalesListBinding,SalesRepository>() {

    var topDate: String = ""
    var lowDate: String = ""
    lateinit var homeActivity: HomeActivity
    lateinit var recyclerAdapter: SalesListItemsListAdapter
    private var selectedBranchId: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvNoSales.visibility = View.GONE

        val collapsingToolbarLayout = requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout


        if (this.topDate == "" || this.lowDate == "") {
            binding.btnPickDateRange.isEnabled = false
            showDateRangePicker()
            collapsingToolbarLayout.title = "Ventas"
        }else{
            binding.btnPickDateRange.isEnabled = true
            collapsingToolbarLayout.title = "Ventas del $lowDate al $topDate"
        }
        initRecyclerView()

        homeActivity = (requireActivity() as HomeActivity)

        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
        heroIcon.setImageResource(R.drawable.ic_hero_sales)

        val fab: FloatingActionButton = requireActivity().findViewById<View>(R.id.fabButton) as FloatingActionButton
        fab.visibility = View.GONE

        val idUser = activity?.intent?.getIntExtra("user", -1)

        selectedBranchId = runBlocking { userPreferences.idSelectedBranch.first()!! }

        binding.btnPickDateRange.setOnClickListener {
            showDateRangePicker()
        }

        recyclerAdapter.setOnItemClickListener(object : SalesListItemsListAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val action = SalesListFragmentDirections.actionSalesListFragmentToSaleSummaryFragment(recyclerAdapter.getItem(position).id_sale)
                findNavController().navigate(action)
            }
        })

        recyclerAdapter.setOnItemLongClickListener(object :
            SalesListItemsListAdapter.onItemLongClickListener {
            override fun onItemLongClick(position: Int): Boolean {

                return true
            }
        })



        viewModel.getSalesResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.success->{
                    binding.tvNoSales.visibility = View.GONE
                    recyclerAdapter.setItemsList(it.value.result)
                    recyclerAdapter.notifyDataSetChanged()
                }
                is Resource.failure->{
                    recyclerAdapter.setItemsList(listOf())
                    binding.tvNoSales.visibility = View.VISIBLE
                    binding.root.showToast("No hay ventas durante el periodo seleccionado")
                    binding.tvNoSales.text = "No hay ventas del $lowDate al $topDate"
                    recyclerAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun initRecyclerView() {

        binding.salesRecycler.layoutManager = LinearLayoutManager(requireContext())
        recyclerAdapter = SalesListItemsListAdapter()
        binding.salesRecycler.adapter = recyclerAdapter

    }

    private fun showDateRangePicker(){

        Locale.setDefault(Locale.forLanguageTag("ES"))
        val now = Calendar.getInstance()
        val dateRangePicker = MaterialDatePicker.Builder
            .dateRangePicker()
            .setTitleText("Elige un periodo de ventas")
            .setPositiveButtonText("Guardar")
            .build()
        dateRangePicker.show(parentFragmentManager,"date_range_picker")
        dateRangePicker.addOnCancelListener {
            val action = SalesListFragmentDirections.actionSalesListFragmentToSalesFragment()
            findNavController().navigate(action)
        }

        dateRangePicker.addOnPositiveButtonClickListener { datePicked->
            val first = datePicked.first
            val second = datePicked.second

            lowDate = longToDate(first)
            topDate = longToDate(second)

            binding.btnPickDateRange.isEnabled = true
            viewModel.getSalesRange("date_created_sale",lowDate,topDate,selectedBranchId.toString(),"id_branch_sale")
        }
    }

    private fun longToDate(time: Long): String{


        val timeZoneUTC = TimeZone.getDefault()
        // It will be negative, so that's the -1
        // It will be negative, so that's the -1
        val offsetFromUTC = timeZoneUTC.getOffset(Date().time) * -1

        // Create a date format, then a date object with our offset
        val simpleFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date(time + offsetFromUTC)

        return simpleFormat.format(date)
    }

    override fun getViewModel(): Class<SalesListViewModel> = SalesListViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSalesListBinding = FragmentSalesListBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): SalesRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(SalesApi::class.java,token)
        return SalesRepository(api,userPreferences)
    }


}