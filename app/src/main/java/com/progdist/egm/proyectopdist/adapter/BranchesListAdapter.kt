package com.progdist.egm.proyectopdist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.responses.branches.Branch

class BranchesListAdapter : RecyclerView.Adapter<BranchesListAdapter.MyViewHolder>(){

    private var branchesList: List<Branch>? = null
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

    fun setBranchesList(branchesList: List<Branch>){
        this.branchesList = branchesList
    }

    fun getBranch(position: Int): Branch{
        return this.branchesList?.get(position)!!
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BranchesListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.branch_item, parent, false)
        return MyViewHolder(view, mListener, mLongListener)
    }

    override fun onBindViewHolder(holder: BranchesListAdapter.MyViewHolder, position: Int) {
        holder.bind(branchesList?.get(position)!!)
    }

    override fun getItemCount(): Int {
        if(branchesList == null){
            return 0
        } else {
            return branchesList?.size!!
        }
    }

    inner class MyViewHolder(view: View, listener: onItemClickListener, longListener: onItemLongClickListener): RecyclerView.ViewHolder(view){

        lateinit var branchImg: ImageView
        lateinit var branchName: TextView
        lateinit var branchDetail: TextView

        init {
            view.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
            view.setOnLongClickListener {
                longListener.onItemLongClick(adapterPosition)
            }
            branchImg = view.findViewById(R.id.branchImage)
            branchName = view.findViewById(R.id.tvBranchName)
            branchDetail = view.findViewById(R.id.tvBranchDetail)
        }

        fun bind(category: Branch){
            branchName.text = category.name_branch
            branchDetail.text = category.description_branch
        }
    }


}