package com.progdist.egm.proyectopdist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.Credit

class CreditsListAdapter : RecyclerView.Adapter<CreditsListAdapter.MyViewHolder>(){

    private var creditsList: List<Credit>? = null
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

    fun setCreditsList(creditsList: List<Credit>){
        this.creditsList = creditsList
    }

    fun getCredit(position: Int): Credit{
        return this.creditsList?.get(position)!!
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CreditsListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.credit_item, parent, false)
        return MyViewHolder(view, mListener, mLongListener)
    }

    override fun onBindViewHolder(holder: CreditsListAdapter.MyViewHolder, position: Int) {
        holder.bind(creditsList?.get(position)!!)
    }

    override fun getItemCount(): Int {
        if(creditsList == null){
            return 0
        } else {
            return creditsList?.size!!
        }
    }

    inner class MyViewHolder(view: View, listener: onItemClickListener, longListener: onItemLongClickListener): RecyclerView.ViewHolder(view){

        lateinit var creditImg: ImageView
        lateinit var creditName: TextView
        lateinit var creditDetail: TextView

        init {
            view.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
            view.setOnLongClickListener {
                longListener.onItemLongClick(adapterPosition)
            }
            creditImg = view.findViewById(R.id.creditImage)
            creditName = view.findViewById(R.id.tvCreditName)
            creditDetail = view.findViewById(R.id.tvCreditDetail)
        }

        fun bind(credit: Credit){
            creditName.text = credit.name
            creditDetail.text = credit.details
        }
    }


}