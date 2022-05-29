package com.progdist.egm.proyectopdist.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.SaleItem
import com.progdist.egm.proyectopdist.data.responses.inventory.Item

class NewSaleItemsListAdapter(private val onAddBtnClicked: () -> Unit, private val onMinBtnClicked: () -> Unit,private val context:String) : RecyclerView.Adapter<NewSaleItemsListAdapter.MyViewHolder>(){

    private var itemsList: List<SaleItem>? = ArrayList()
    private lateinit var mListener: onItemClickListener
    private lateinit var mLongListener: onItemLongClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    interface onItemLongClickListener{
        fun onItemLongClick(position: Int) : Boolean
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    fun setOnItemLongClickListener(listener: onItemLongClickListener){
        mLongListener = listener
    }

    fun setItemsList(itemsList: List<SaleItem>){
        this.itemsList = itemsList
    }

    fun getItemsList(): List<SaleItem>? {
        return this.itemsList
    }


    fun getItem(position: Int): SaleItem{
        return this.itemsList?.get(position)!!
    }

    fun addItem(item: SaleItem){
        this.itemsList = this.itemsList?.plus(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): NewSaleItemsListAdapter.MyViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.new_sale_item, parent, false)
        return MyViewHolder(view, mListener, mLongListener)
    }

    override fun onBindViewHolder(holder: NewSaleItemsListAdapter.MyViewHolder, position: Int) {
        holder.bind(itemsList?.get(position)!!)
    }

    override fun getItemCount(): Int {
        if(itemsList == null){
            return 0
        } else {
            return itemsList?.size!!
        }
    }

    inner class MyViewHolder(view: View, listener: onItemClickListener, longListener: onItemLongClickListener): RecyclerView.ViewHolder(view){

        lateinit var itemImg: ImageView

        lateinit var addBtn: ImageView
        lateinit var minBtn: ImageView

        lateinit var itemName: TextView
        lateinit var itemPrice: TextView
        lateinit var itemStock: TextView
        lateinit var quantity: TextView

        init {
            view.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
            view.setOnLongClickListener {
                longListener.onItemLongClick(adapterPosition)
            }
            itemImg = view.findViewById(R.id.itemImage)
            itemName = view.findViewById(R.id.tvItemName)
            itemPrice = view.findViewById(R.id.tvItemPrice)
            itemStock = view.findViewById(R.id.tvItemStock)
            quantity = view.findViewById(R.id.tvQuantity)
            addBtn = view.findViewById(R.id.ivAdd)
            minBtn = view.findViewById(R.id.ivMinus)
        }

        fun bind(item: SaleItem){
            itemName.text = item.item.name_item
            when(context){
                "sale"->{
                    itemPrice.text = "Precio: $ ${item.item.sale_price_item * item.qty}"
                    itemStock.text = "Disponibles: ${item.item.stock_item - item.qty}"
                }
                "purchase"->{
                    itemPrice.text = "Precio: $ ${item.item.purchase_price_item * item.qty}"
                    itemStock.text = "Disponibles: ${item.item.stock_item + item.qty}"
                }
            }
            quantity.text = item.qty.toString()
            addBtn.setOnClickListener {
                addQuantity(item)
                onAddBtnClicked()
            }
            minBtn.setOnClickListener {
                minQuantity(item)
                onMinBtnClicked()
            }
        }

        fun addQuantity(item:SaleItem){
            val saleItems: ArrayList<SaleItem> = ArrayList()
            getItemsList()!!.forEach { saleItem->
                if(saleItem.item.id_item == item.item.id_item){
                    if((saleItem.qty + 1 <= item.item.stock_item && context == "sale") || context == "purchase"){
                        saleItems.add(SaleItem(saleItem.item,saleItem.qty+1))
                    } else{
                        return
                    }
                }else{
                    saleItems.add(saleItem)
                }
            }
            setItemsList(saleItems)
            notifyDataSetChanged()
        }

        fun minQuantity(item: SaleItem){
            val saleItems: ArrayList<SaleItem> = ArrayList()
            getItemsList()!!.forEach { saleItem->
                if(saleItem.item.id_item == item.item.id_item){
                    if(saleItem.qty - 1 > 0){
                        saleItems.add(SaleItem(saleItem.item,saleItem.qty-1))
                    } else if(saleItem.qty - 1 == 0){
                    } else{
                        return
                    }
                }else{
                    saleItems.add(saleItem)
                }
            }
            setItemsList(saleItems)
            notifyDataSetChanged()
        }

    }

}