package com.progdist.egm.proyectopdist.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.progdist.egm.proyectopdist.data.repository.*
import com.progdist.egm.proyectopdist.ui.CodeScannerViewModel
import com.progdist.egm.proyectopdist.ui.auth.AuthViewModel
import com.progdist.egm.proyectopdist.ui.home.owner.HomeViewModel
import com.progdist.egm.proyectopdist.ui.home.owner.branches.viewmodel.AddBranchViewModel
import com.progdist.egm.proyectopdist.ui.home.owner.branches.viewmodel.BranchesSummaryViewModel
import com.progdist.egm.proyectopdist.ui.home.owner.branches.viewmodel.BranchesViewModel
import com.progdist.egm.proyectopdist.ui.home.owner.employees.viewmodel.AddEmployeeViewModel
import com.progdist.egm.proyectopdist.ui.home.owner.employees.viewmodel.EmployeeSummaryViewModel
import com.progdist.egm.proyectopdist.ui.home.owner.employees.viewmodel.ManageEmployeesViewModel
import com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels.*
import com.progdist.egm.proyectopdist.ui.home.owner.sales.viewmodel.*
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
            modelClass.isAssignableFrom(AddItemViewModel::class.java) -> AddItemViewModel(
                repository as InventoryRepository) as T
            modelClass.isAssignableFrom(ItemSummaryViewModel::class.java) -> ItemSummaryViewModel(
                repository as InventoryRepository) as T
            modelClass.isAssignableFrom(CodeScannerViewModel::class.java) -> CodeScannerViewModel(
                repository as SalesRepository) as T
            modelClass.isAssignableFrom(NewSaleViewModel::class.java) -> NewSaleViewModel(
                repository as SalesRepository) as T
            modelClass.isAssignableFrom(CheckoutViewModel::class.java) -> CheckoutViewModel(
                repository as SalesRepository) as T
            modelClass.isAssignableFrom(SalesViewModel::class.java) -> SalesViewModel(
                repository as SalesRepository) as T
            modelClass.isAssignableFrom(SalesListViewModel::class.java) -> SalesListViewModel(
                repository as SalesRepository) as T
            modelClass.isAssignableFrom(SaleSummaryViewModel::class.java) -> SaleSummaryViewModel(
                repository as SalesRepository) as T
            modelClass.isAssignableFrom(AddEmployeeViewModel::class.java) -> AddEmployeeViewModel(
                repository as ManageEmployeesRepository) as T
            modelClass.isAssignableFrom(EmployeeSummaryViewModel::class.java) ->EmployeeSummaryViewModel(
                repository as ManageEmployeesRepository) as T
            else -> throw IllegalArgumentException("View Model Class Not Found")
        }
    }

}