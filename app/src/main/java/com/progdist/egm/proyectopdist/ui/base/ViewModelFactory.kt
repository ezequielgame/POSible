package com.progdist.egm.proyectopdist.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.progdist.egm.proyectopdist.data.repository.*
import com.progdist.egm.proyectopdist.ui.auth.AuthViewModel
import com.progdist.egm.proyectopdist.ui.home.owner.HomeViewModel
import com.progdist.egm.proyectopdist.ui.home.owner.branches.AddBranchViewModel
import com.progdist.egm.proyectopdist.ui.home.owner.branches.BranchesSummaryViewModel
import com.progdist.egm.proyectopdist.ui.home.owner.branches.BranchesViewModel
import com.progdist.egm.proyectopdist.ui.home.owner.employees.ManageEmployeesViewModel
import com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels.*
import com.progdist.egm.proyectopdist.ui.home.owner.sales.SalesViewModel
import com.progdist.egm.proyectopdist.ui.main.MainViewModel

class ViewModelFactory(
    private val repository: BaseRepository,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(repository as MainRepository) as T
            modelClass.isAssignableFrom(BranchesViewModel::class.java) -> BranchesViewModel(
                repository as BranchesRepository) as T
            modelClass.isAssignableFrom(ManageEmployeesViewModel::class.java) -> ManageEmployeesViewModel(
                repository as ManageEmployeesRepository) as T
            modelClass.isAssignableFrom(SalesViewModel::class.java) -> SalesViewModel(repository as SalesRepository) as T
            modelClass.isAssignableFrom(InventoryViewModel::class.java) -> InventoryViewModel(
                repository as InventoryRepository) as T
            modelClass.isAssignableFrom(AddBranchViewModel::class.java) -> AddBranchViewModel(
                repository as BranchesRepository) as T
            modelClass.isAssignableFrom(BranchesSummaryViewModel::class.java) -> BranchesSummaryViewModel(
                repository as BranchesRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(
                repository as HomeRepository) as T
            modelClass.isAssignableFrom(CategoriesViewModel::class.java) -> CategoriesViewModel(
                repository as InventoryRepository) as T
            modelClass.isAssignableFrom(AddCategoryViewModel::class.java) -> AddCategoryViewModel(
                repository as InventoryRepository) as T
            modelClass.isAssignableFrom(CategorySummaryViewModel::class.java) -> CategorySummaryViewModel(
                repository as InventoryRepository) as T
            modelClass.isAssignableFrom(ItemsViewModel::class.java) -> ItemsViewModel(
                repository as InventoryRepository) as T
            modelClass.isAssignableFrom(SuppliersViewModel::class.java) -> SuppliersViewModel(
                repository as InventoryRepository) as T
            modelClass.isAssignableFrom(AddSupplierViewModel::class.java) -> AddSupplierViewModel(
                repository as InventoryRepository) as T
            modelClass.isAssignableFrom(SupplierSummaryViewModel::class.java) -> SupplierSummaryViewModel(
                repository as InventoryRepository) as T
            else -> throw IllegalArgumentException("View Model Class Not Found")
        }
    }

}