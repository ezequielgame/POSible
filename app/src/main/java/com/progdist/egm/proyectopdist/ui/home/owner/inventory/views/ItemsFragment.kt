package com.progdist.egm.proyectopdist.ui.home.owner.inventory.views

import android.view.LayoutInflater
import android.view.ViewGroup
import com.progdist.egm.proyectopdist.adapter.CategoriesListAdapter
import com.progdist.egm.proyectopdist.data.network.InventoryApi
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.databinding.FragmentItemsBinding
import com.progdist.egm.proyectopdist.ui.base.BaseFragment
import com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels.ItemsViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ItemsFragment : BaseFragment<ItemsViewModel,FragmentItemsBinding,InventoryRepository>() {

    lateinit var recyclerAdapter: CategoriesListAdapter
    var selectedBranchId: Int? = -1

    override fun getViewModel(): Class<ItemsViewModel> = ItemsViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentItemsBinding = FragmentItemsBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): InventoryRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(InventoryApi::class.java, token)
        return InventoryRepository(api, userPreferences)
    }

}