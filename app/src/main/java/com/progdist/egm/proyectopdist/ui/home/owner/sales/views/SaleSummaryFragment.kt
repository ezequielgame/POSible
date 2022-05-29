package com.progdist.egm.proyectopdist.ui.home.owner.sales.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.adapter.ItemsListAdapter
import com.progdist.egm.proyectopdist.adapter.SaleDetailItemListAdapter
import com.progdist.egm.proyectopdist.adapter.SalesListItemsListAdapter
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.network.SalesApi
import com.progdist.egm.proyectopdist.data.repository.SalesRepository
import com.progdist.egm.proyectopdist.databinding.FragmentSaleSummaryBinding
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.owner.sales.viewmodel.SaleSummaryViewModel
import com.progdist.egm.proyectopdist.ui.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SaleSummaryFragment : BaseFragment<SaleSummaryViewModel,FragmentSaleSummaryBinding,SalesRepository>() {

    lateinit var recyclerAdapter: SaleDetailItemListAdapter
    lateinit var context: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context = requireArguments().getString("context").toString()
        initRecyclerView()

        val collapsingToolbarLayout =
            requireActivity().findViewById<View>(R.id.collapsingToolbar) as CollapsingToolbarLayout
        val heroIcon = requireActivity().findViewById<View>(R.id.ivHeroIcon) as ImageView
        val saleId = requireArguments().getInt("saleId")
        //it is called saleId but can be it or purchaseId

        if(context == "sale"){
            collapsingToolbarLayout.title = "Detalle de venta"
            heroIcon.setImageResource(R.drawable.ic_sale)
            viewModel.getSalesItems("id_sale_sale_item",saleId.toString())
            viewModel.getSalesItemsResponse.observe(viewLifecycleOwner){
                when(it) {
                    is Resource.success -> {
                        recyclerAdapter.setItemsList(it.value.result)
                        recyclerAdapter.notifyDataSetChanged()
                        binding.tvSaleSummarySaleTotal.text =
                            "$" + it.value.result[0].total_sale.toString()
                        binding.tvSaleSummaryTotalQuantity.text =
                            it.value.result[0].total_quantity_sale.toString()
                        viewModel.getPaymentTypes("id_payment_type",
                            it.value.result[0].id_payment_type_sale.toString())
                        viewModel.getCustomers("id_customer",
                            it.value.result[0].id_customer_sale.toString())
                    }
                    is Resource.failure -> {
                        binding.root.showToast("Error")
                    }
                }
            }
            viewModel.getCustomersResponse.observe(viewLifecycleOwner){
                when(it){
                    is Resource.success->{
                        binding.tvSaleSummaryTitleCustomer.text = "Cliente"
                        binding.ivCustomerTitle.setImageResource(R.drawable.ic_customers)
                        binding.tvSaleSummaryCustomerCardTitle.text = "Cliente: " + it.value.result[0].name_customer
                    }
                    is Resource.failure->{

                    }
                }
            }
        }else{
            collapsingToolbarLayout.title = "Detalle de compra"
            heroIcon.setImageResource(R.drawable.ic_sale)
            viewModel.getPurchasesItems("id_purchase_purchase_item",saleId.toString())
            viewModel.getPurchasesItemsResponse.observe(viewLifecycleOwner){
                when(it) {
                    is Resource.success -> {
                        recyclerAdapter.setItemsList(it.value.result)
                        recyclerAdapter.notifyDataSetChanged()
                        binding.tvSaleSummarySaleTotal.text =
                            "$" + it.value.result[0].total_purchase.toString()
                        binding.tvSaleSummaryTotalQuantity.text =
                            it.value.result[0].total_quantity_purchase.toString()
                        viewModel.getPaymentTypes("id_payment_type",
                            it.value.result[0].id_payment_type_purchase.toString())
                        viewModel.getSuppliers("id_supplier",
                            it.value.result[0].id_supplier_purchase)
                    }
                    is Resource.failure -> {
                        binding.root.showToast("Error")
                    }
                }
            }
            viewModel.getSuppliersResponse.observe(viewLifecycleOwner){
                when(it){
                    is Resource.success->{
                        binding.tvSaleSummaryTitleCustomer.text = "Proveedor"
                        binding.ivCustomerTitle.setImageResource(R.drawable.ic_supplier)
                        binding.tvSaleSummaryCustomerCardTitle.text = "Proveedor: " + it.value.result[0].name_supplier
                    }
                    is Resource.failure->{

                    }
                }
            }

        }

        viewModel.getPaymentTypesResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.success->{
                    binding.tvSaleSummaryPaymentCardTitle.text = "Método de Pago: " + it.value.result[0].name_payment_type
                }
                is Resource.failure->{

                }
            }
        }



    }

    private fun initRecyclerView() {

        binding.saleSummaryItemsRecycler.layoutManager = LinearLayoutManager(requireContext())
        recyclerAdapter = SaleDetailItemListAdapter(context)
        binding.saleSummaryItemsRecycler.adapter = recyclerAdapter


        recyclerAdapter.setOnItemClickListener(object : SaleDetailItemListAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

            }
        })

        recyclerAdapter.setOnItemLongClickListener(object :
            SaleDetailItemListAdapter.onItemLongClickListener {
            override fun onItemLongClick(position: Int): Boolean {

                return true
            }
        })

    }

    override fun getViewModel(): Class<SaleSummaryViewModel> = SaleSummaryViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSaleSummaryBinding = FragmentSaleSummaryBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): SalesRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(SalesApi::class.java,token)
        return SalesRepository(api,userPreferences)
    }

}