package com.progdist.egm.proyectopdist.ui.home.owner.sales.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.progdist.egm.proyectopdist.adapter.ItemsListAdapter
import com.progdist.egm.proyectopdist.adapter.NewSaleItemsListAdapter
import com.progdist.egm.proyectopdist.data.SaleItem
import com.progdist.egm.proyectopdist.data.SaleItemInfo
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.network.SalesApi
import com.progdist.egm.proyectopdist.data.repository.SalesRepository
import com.progdist.egm.proyectopdist.databinding.ActivityNewSaleBinding
import com.progdist.egm.proyectopdist.ui.base.BaseActivity
import com.progdist.egm.proyectopdist.ui.home.owner.inventory.views.ItemsFragmentDirections
import com.progdist.egm.proyectopdist.ui.home.owner.sales.viewmodel.NewSaleViewModel
import com.progdist.egm.proyectopdist.ui.setTextAnimation
import com.progdist.egm.proyectopdist.ui.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class NewSaleActivity : BaseActivity<NewSaleViewModel,ActivityNewSaleBinding,SalesRepository>(){

    private lateinit var codeScanner: CodeScanner
    lateinit var scannerView: CodeScannerView
    lateinit var context: String

    private lateinit var recyclerAdapter: NewSaleItemsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = intent.getStringExtra("context").toString()

        initScanner()
        initRecyclerView()


        if(intent.getBooleanExtra("renewSale",false)) {
            recyclerAdapter.setItemsList(listOf())
        }

        recyclerAdapter.setOnItemClickListener(object : NewSaleItemsListAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

            }
        })

        recyclerAdapter.setOnItemLongClickListener(object :
            NewSaleItemsListAdapter.onItemLongClickListener {
            override fun onItemLongClick(position: Int): Boolean {

                return true
            }
        })

        viewModel.getItemsResponse.observe(this){
            when(it){
                is Resource.success->{
                    if(recyclerAdapter.getItemsList() != null && recyclerAdapter.getItemsList()!!.isNotEmpty()){
                        val saleItems: ArrayList<SaleItem> = ArrayList()
                        var find = false
                        recyclerAdapter.getItemsList()!!.forEach { saleItem->
                            //Creating new list
                            if(saleItem.item.id_item == it.value.result[0].id_item){
                                // if item already in list
                                //Check available stock if selling or add it anyway if purchasing
                                if((saleItem.qty + 1 <= saleItem.item.stock_item && context == "sale") || context == "purchase"){
                                    saleItems.add(SaleItem(saleItem.item,saleItem.qty+1))
                                    find = true
                                }else {
                                    return@observe // No stock, and not purchasing, do nothing
                                }
                            }else{
                                // We add the item, not concern
                                saleItems.add(saleItem)
                            }
                        }
                        if(!find){
                            //There was not in the list, add it
                            recyclerAdapter.addItem(SaleItem(it.value.result[0],1))
                        }else{
                            //Apply new list
                            recyclerAdapter.setItemsList(saleItems)
                        }
                        var total = 0f
                        recyclerAdapter.getItemsList()!!.forEach {
                            if(context == "sale"){
                                total += it.qty * it.item.sale_price_item
                            } else{
                                total += it.qty * it.item.purchase_price_item
                            }
                        }
                        binding.btnNewSale.setText("Proceder al pago: $${total}")
                        recyclerAdapter.notifyDataSetChanged()
                    }else{
                        if((it.value.result[0].stock_item > 0 && context == "sale") || context == "purchase") {
                            val total: Float
                            if(context == "sale"){
                                total = it.value.result[0].sale_price_item
                            }else{
                                total = it.value.result[0].purchase_price_item
                            }
                            recyclerAdapter.addItem(SaleItem(it.value.result[0],1))
                            binding.btnNewSale.setText("Proceder al pago: $${total}")
                            recyclerAdapter.notifyDataSetChanged()
                        }else{
                            binding.root.showToast("Al parecer ya no hay existencias de ese producto")
                        }
                    }

                }
                is Resource.failure->{
                }
            }
        }

        binding.btnNewSale.setOnClickListener {

            val itemsList = recyclerAdapter.getItemsList()
            if(itemsList.isNullOrEmpty()){
                binding.root.showToast("No hay productos en el carrito")
            }else{
                val itemsInfo: ArrayList<SaleItemInfo> = ArrayList()
                val branchId = intent.getIntExtra("branchId",-1)
                val customerId = 1
                val userId = intent.getIntExtra("userId",-1)
                val employeeId = -1
                var total = 0f
                val paymentTypeId = 1
                itemsList.forEach {
                    total += it.qty * it.item.sale_price_item
                }
                itemsList.forEach {
                    itemsInfo.add(SaleItemInfo(it.item.id_item,branchId,customerId,userId,employeeId,total,paymentTypeId,it.qty.toFloat()))
                }
                val intent = Intent(this,CheckoutActivity::class.java)
                intent.putParcelableArrayListExtra("itemsList",itemsInfo)
                intent.putExtra("context",context)
                intent.putExtra("userId",userId)
                startActivity(intent)
            }
        }
    }

    private fun initRecyclerView() {

        binding.newSaleItemsRecycler.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = NewSaleItemsListAdapter({
            var total = 0f
            recyclerAdapter.getItemsList()!!.forEach {
                if(context == "sale"){
                    total += it.qty * it.item.sale_price_item
                } else {
                    total += it.qty * it.item.purchase_price_item
                }
            }
            binding.btnNewSale.setText("Proceder al pago: $${total}")
        },{
            var total = 0f
            recyclerAdapter.getItemsList()!!.forEach {
                if(context == "sale"){
                    total += it.qty * it.item.sale_price_item
                } else {
                    total += it.qty * it.item.purchase_price_item
                }
            }
            binding.btnNewSale.setText("Proceder al pago: $${total}")
        }, context)
        binding.newSaleItemsRecycler.adapter = recyclerAdapter

    }

    private fun initScanner(){

        scannerView = binding.scannerView
        codeScanner = CodeScanner(this,scannerView)

//        binding.btnSaveCode.visibility = View.GONE

        codeScanner.startPreview()
        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ONE_DIMENSIONAL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.CONTINUOUS // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not


        if(binding.switchAutomatic.isChecked.not()){
            binding.root.showToast("Pulsa sobre la vista de la cámara para volver a escanear")
        }


        binding.switchAutomatic.setOnCheckedChangeListener { compoundButton, b ->
            if(binding.switchAutomatic.isChecked){
                codeScanner.startPreview()
                codeScanner.scanMode = ScanMode.CONTINUOUS
            } else {
                codeScanner.scanMode = ScanMode.SINGLE
                binding.root.showToast("Pulsa sobre la vista de la cámara para volver a escanear")
            }
        }

        val DELAY = 1000
        var lastTimestamp: Long = 0

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback { scanResult->

            if(System.currentTimeMillis() - lastTimestamp < DELAY){
                return@DecodeCallback
            }
            runOnUiThread {
//                binding.btnSaveCode.visibility = View.VISIBLE

                binding.tvScanStatus.setTextAnimation("Resultado: ${scanResult.text}",200)

                val branchId = intent.getIntExtra("branchId",-1)
                viewModel.getItems("code_item,id_branch_item",scanResult.text.toString() + "," + branchId.toString() )

            }


            lastTimestamp = System.currentTimeMillis()
        }

        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Error iniciando la cámara ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
//            binding.btnSaveCode.visibility = View.GONE
            binding.tvScanStatus.text = "Escaneando..."
        }

    }

    override fun onPause() {
        super.onPause()
        codeScanner.stopPreview()
        codeScanner.releaseResources()
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun getViewModel(): Class<NewSaleViewModel> = NewSaleViewModel::class.java

    override fun getViewBinding(): ActivityNewSaleBinding = ActivityNewSaleBinding.inflate(layoutInflater)

    override fun getActivityRepository(): SalesRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(SalesApi::class.java, token)
        return SalesRepository(api,userPreferences)
    }
}