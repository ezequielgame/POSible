package com.progdist.egm.proyectopdist.ui.home.owner.sales.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.progdist.egm.proyectopdist.adapter.SaleSummaryListAdapter
import com.progdist.egm.proyectopdist.data.SaleItem
import com.progdist.egm.proyectopdist.data.SaleItemInfo
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.network.SalesApi
import com.progdist.egm.proyectopdist.data.repository.SalesRepository
import com.progdist.egm.proyectopdist.data.responses.sales.Customer
import com.progdist.egm.proyectopdist.data.responses.sales.PaymentType
import com.progdist.egm.proyectopdist.databinding.ActivityCheckoutBinding
import com.progdist.egm.proyectopdist.ui.base.BaseActivity
import com.progdist.egm.proyectopdist.ui.home.owner.sales.viewmodel.CheckoutViewModel
import com.progdist.egm.proyectopdist.ui.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class CheckoutActivity : BaseActivity<CheckoutViewModel,ActivityCheckoutBinding,SalesRepository>() {

    private lateinit var recyclerAdapter: SaleSummaryListAdapter
    private var idPaymentTypeSelected: Int = -1
    private var idCustomerSelected: Int = -1
    private lateinit var arrayOfPaymentTypes: Array<PaymentType>
    private lateinit var aCustomers: Array<Customer>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initRecyclerView()

        val intent = intent
        val itemsList = intent.getParcelableArrayListExtra<SaleItemInfo>("itemsList")

        viewModel.getPaymentTypes("id_user_payment_type","1")
//        viewModel.getPaymentTypes("id_user_payment_type", itemsList!![0].idUser.toString())

        viewModel.getPaymentTypesResponse.observe(this){
            when(it){
                is Resource.success->{
                    arrayOfPaymentTypes = it.value.result.toTypedArray()
                }
                is Resource.failure->{

                }
            }
        }

        viewModel.getCustomers("id_user_customer","1")

        viewModel.getCustomersResponse.observe(this){
            when(it){
                is Resource.success->{
                    aCustomers = it.value.result.toTypedArray()
                }
                is Resource.failure->{

                }
            }
        }


        viewModel.getItemsResponse.observe(this){
            when(it){
                is Resource.success->{
                    itemsList!!.forEach { saleListItem->
                        if(saleListItem.idItem == it.value.result[0].id_item){
                            recyclerAdapter.addItem(SaleItem(it.value.result[0],saleListItem.quantity.toInt()))
                        }
                    }
                    var total = 0f
                    var totalQ = 0
                    recyclerAdapter.getItemsList()!!.forEach {saleRecyclerListItem->
                        total += saleRecyclerListItem.item.sale_price_item * saleRecyclerListItem.qty
                        totalQ += saleRecyclerListItem.qty
                    }
                    binding.tvSaleTotal.text = "$${total}"
                    binding.tvTotalQuantity.text = totalQ.toString()
                    recyclerAdapter.notifyDataSetChanged()
                }
                is Resource.failure->{

                }
            }
        }

        binding.actvSalePayment.setOnClickListener {

            val paymentNames: MutableList<String> = ArrayList()
            arrayOfPaymentTypes.forEach {
                paymentNames += it.name_payment_type
            }
            val suppliersOptions = paymentNames.toTypedArray()


            val builder = MaterialAlertDialogBuilder(this)
            // dialog title
            builder.setTitle("Selecciona un método de pago")

            builder.setSingleChoiceItems(
                suppliersOptions, -1
            ) { dialog, i -> }

            // alert dialog positive button
            builder.setPositiveButton("Seleccionar") { dialog, which ->
                val position = (dialog as AlertDialog).listView.checkedItemPosition
                // if selected, then get item text
                if (position != -1) {
                    idPaymentTypeSelected = arrayOfPaymentTypes[position].id_payment_type
                    binding.actvSalePayment.setText(arrayOfPaymentTypes[position].name_payment_type)
                } else {
                    idPaymentTypeSelected = -1
                    binding.actvSalePayment.setText("Método de pago")
                }
            }
            // set dialog non cancelable
            builder.setCancelable(true)

            // finally, create the alert dialog and show it
            val dialog = builder.create()
            dialog.show()
        }

        binding.actvSaleCustomer.setOnClickListener {

            val customersNames: MutableList<String> = ArrayList()
            aCustomers.forEach {
                customersNames += it.name_customer
            }
            val customersOptions = customersNames.toTypedArray()


            val builder = MaterialAlertDialogBuilder(this)
            // dialog title
            builder.setTitle("Selecciona un cliente")

            builder.setSingleChoiceItems(
                customersOptions, -1
            ) { dialog, i -> }

            // alert dialog positive button
            builder.setPositiveButton("Seleccionar") { dialog, which ->
                val position = (dialog as AlertDialog).listView.checkedItemPosition
                // if selected, then get item text
                if (position != -1) {
                    idCustomerSelected = aCustomers[position].id_customer
                    binding.actvSaleCustomer.setText(aCustomers[position].name_customer)
                } else {
                    idCustomerSelected = -1
                    binding.actvSaleCustomer.setText("Cliente")
                }
            }
            // set dialog non cancelable
            builder.setCancelable(true)

            // finally, create the alert dialog and show it
            val dialog = builder.create()
            dialog.show()

        }


        itemsList!!.forEach {
            viewModel.getItems("id_item",it.idItem.toString())
        }

        viewModel.addSaleItemResponse.observe(this){
            when(it){
                is Resource.success->{

                }
                is Resource.failure->{

                }
            }
        }

        viewModel.addOwnerSaleResponse.observe(this){
            when(it){
                is Resource.success->{
                    recyclerAdapter.getItemsList()!!.forEach {saleItem->
                        viewModel.addSaleItem(
                            saleItem.item.id_item,
                            it.value.result.lastId.toInt(),
                            saleItem.qty,
                            saleItem.qty * saleItem.item.sale_price_item
                        )
                        viewModel.editItemStock(
                            saleItem.item.id_item,
                            "id_item",
                            saleItem.item.stock_item - saleItem.qty
                        )
                    }
                    mostrarDialogo()
                }
                is Resource.failure->{

                }
            }

        }

        binding.btnCompleteSale.setOnClickListener {

            var valido = true
            if(idCustomerSelected == -1){
                binding.root.showToast("Debes seleccionar el cliente")
                valido = false
            }
            if(idPaymentTypeSelected == -1){
                binding.root.showToast("Debes seleccionar el método de pago")
                valido = false
            }
            if(!valido){
                return@setOnClickListener
            }
            var total = 0f
            var totalQ = 0
            recyclerAdapter.getItemsList()!!.forEach { saleItem->
                total += saleItem.item.sale_price_item * saleItem.qty
                totalQ += saleItem.qty
            }

            viewModel.addOwnerSale(itemsList[0].idBranch,idCustomerSelected,itemsList[0].idUser,total, totalQ,idPaymentTypeSelected)

        }

    }

    private fun mostrarDialogo(){
        val builder = MaterialAlertDialogBuilder(this)
        // dialog title
        builder.setTitle("Se registró la venta")
        builder.setMessage("Presiona finalizar para continuar")
        // alert dialog positive button
        builder.setPositiveButton("Finalizar") { dialog, which ->
            val intent = Intent(this, NewSaleActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("renewSale",true)
            startActivity(intent)
            finish()
        }
        // set dialog non cancelable
        builder.setCancelable(false)
        // finally, create the alert dialog and show it
        val dialog = builder.create()
        dialog.show()
    }

    private fun initRecyclerView() {

        binding.saleSummaryRecycler.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = SaleSummaryListAdapter()
        binding.saleSummaryRecycler.adapter = recyclerAdapter

        recyclerAdapter.setOnItemClickListener(object : SaleSummaryListAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

            }
        })

        recyclerAdapter.setOnItemLongClickListener(object :
            SaleSummaryListAdapter.onItemLongClickListener {
            override fun onItemLongClick(position: Int): Boolean {

                return true
            }
        })

    }

    override fun getViewModel(): Class<CheckoutViewModel> = CheckoutViewModel::class.java

    override fun getViewBinding(): ActivityCheckoutBinding = ActivityCheckoutBinding.inflate(layoutInflater)

    override fun getActivityRepository(): SalesRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(SalesApi::class.java,token)
        return SalesRepository(api,userPreferences)
    }

}