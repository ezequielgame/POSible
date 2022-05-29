package com.progdist.egm.proyectopdist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.responses.sales.PurchaseItem
import com.progdist.egm.proyectopdist.data.responses.sales.SaleItem

class SaleDetailItemListAdapter(private val context: String) : RecyclerView.Adapter<SaleDetailItemListAdapter.MyViewHolder>(){

    private var itemsList: List<Any>? = ArrayList()
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

    fun setItemsList(itemsList: List<Any>){
        this.itemsList = itemsList
    }

    fun getItemsList(): List<Any>? {
        return this.itemsList
    }


    fun getItem(position: Int): Any{
        return this.itemsList?.get(position)!!
    }

    fun addItem(item: Any){
        this.itemsList = this.itemsList?.plus(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SaleDetailItemListAdapter.MyViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.sale_summary_item, parent, false)
        return MyViewHolder(view, mListener, mLongListener)
    }

    override fun onBindViewHolder(holder: SaleDetailItemListAdapter.MyViewHolder, position: Int) {
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
        lateinit var itemName: TextView
        lateinit var itemPrice: TextView
        lateinit var itemQuantity: TextView
        lateinit var itemTotal: TextView


        init {
            view.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
            view.setOnLongClickListener {
                longListener.onItemLongClick(adapterPosition)
            }
            itemName = view.findViewById(R.id.tvItemName)
            itemPrice = view.findViewById(R.id.tvItemPrice)
            itemQuantity = view.findViewById(R.id.tvItemQuantity)
            itemTotal = view.findViewById(R.id.tvItemTotal)

        }
        fun bind(item: Any){
            if(context == "sale"){
                item as SaleItem
                itemPrice.text = "$${item.sale_price_item}"
                itemName.text = item.name_item
                itemQuantity.text = item.quantity_sale_item.toString()
                itemTotal.text = "$${item.quantity_sale_item * item.sale_price_item}"
            }else{
                item as PurchaseItem
                itemPrice.text = "$${item.purchase_price_item}"
                itemName.text = item.name_item
                itemQuantity.text = item.quantity_purchase_item.toString()
                itemTotal.text = "$${item.total_purchase_item}"
            }
        }

    }

}