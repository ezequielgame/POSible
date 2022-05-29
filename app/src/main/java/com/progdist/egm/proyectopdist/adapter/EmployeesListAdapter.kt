package com.progdist.egm.proyectopdist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.responses.employees.Employee


class EmployeesListAdapter : RecyclerView.Adapter<EmployeesListAdapter.MyViewHolder>(){

    private var employeesList: List<Employee>? = null
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

    fun setEmployeesList(employeesList: List<Employee>){
        this.employeesList = employeesList
    }

    fun getEmployee(position: Int): Employee{
        return this.employeesList?.get(position)!!
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): EmployeesListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.employee_item, parent, false)
        return MyViewHolder(view, mListener, mLongListener)
    }

    override fun onBindViewHolder(holder: EmployeesListAdapter.MyViewHolder, position: Int) {
        holder.bind(employeesList?.get(position)!!)
    }

    override fun getItemCount(): Int {
        if(employeesList == null){
            return 0
        } else {
            return employeesList?.size!!
        }
    }

    inner class MyViewHolder(view: View, listener: onItemClickListener, longListener: onItemLongClickListener): RecyclerView.ViewHolder(view){

        lateinit var employeeImg: ImageView
        lateinit var employeeName: TextView
        lateinit var employeeRole: TextView
        lateinit var employeeBranch: TextView

        init {
            view.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
            view.setOnLongClickListener {
                longListener.onItemLongClick(adapterPosition)
            }
            employeeImg = view.findViewById(R.id.employeeImage)
            employeeName = view.findViewById(R.id.tvEmployeeName)
            employeeRole = view.findViewById(R.id.tvEmployeeRole)
            employeeBranch = view.findViewById(R.id.tvEmployeeBranch)
        }

        fun bind(employee: Employee){
            employeeName.text = employee.name_employee
            employeeRole.text = employee.name_role
            employeeBranch.text = employee.name_branch
        }
    }


}