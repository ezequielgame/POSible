package com.progdist.egm.proyectopdist.ui.home.employee.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.SplashActivity
import com.progdist.egm.proyectopdist.data.network.HomeApi
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.HomeRepository
import com.progdist.egm.proyectopdist.data.responses.branches.Branch
import com.progdist.egm.proyectopdist.data.responses.branches.ExtendedBranch
import com.progdist.egm.proyectopdist.databinding.ActivityEmployeeHomeBinding
import com.progdist.egm.proyectopdist.databinding.ActivityHomeBinding
import com.progdist.egm.proyectopdist.ui.base.BaseActivity
import com.progdist.egm.proyectopdist.ui.home.owner.HomeViewModel
import com.progdist.egm.proyectopdist.ui.showToast
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class EmployeeHomeActivity : BaseActivity<HomeViewModel,ActivityEmployeeHomeBinding,HomeRepository>() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navController: NavController
    var employeeId: Int = -1
    var branches = arrayOf<Branch>()
    var selectedBranch: ExtendedBranch? = null
    lateinit var drawerLayout: DrawerLayout
    lateinit var navDrawer: NavigationView
    lateinit var drawerHeader: View
    lateinit var headerName: TextView
    lateinit var headerMail: TextView
    lateinit var headerRole: TextView
    lateinit var headerBranch: TextView
    lateinit var headeImg: ImageView
    var selectedBranchId: Int? = -1
    var employeeRoleId: Int? = -1
    var userId: Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        employeeId = intent.getIntExtra("employeeId", -1)

        drawerLayout = binding.drawerLayout
        navDrawer = binding.drawerNavView
        drawerHeader = navDrawer.getHeaderView(0)
        headerName = drawerHeader.findViewById<View>(R.id.tvName) as TextView
        headerMail = drawerHeader.findViewById<View>(R.id.tvEmail) as TextView
        headerRole = drawerHeader.findViewById<View>(R.id.tvRole) as TextView
        headerBranch = drawerHeader.findViewById<View>(R.id.tvBranch) as TextView
        headeImg = drawerHeader.findViewById<View>(R.id.ivUser) as ImageView

        viewModel.getEmployees("id_employee",employeeId.toString())

        viewModel.getEmployeesResponse.observe(this){
            when(it){
                is Resource.success->{
                    viewModel.saveIdSelectedBranch(it.value.result[0].id_branch_employee)
                    selectedBranchId = it.value.result[0].id_branch_employee
                    headerName.text = it.value.result[0].name_employee
                    headerBranch.text = it.value.result[0].name_branch
                    headerMail.text = it.value.result[0].email_employee
                    headerRole.text = it.value.result[0].name_role
                    employeeRoleId = it.value.result[0].id_role_employee
                    userId = it.value.result[0].id_user_employee
                }
                is Resource.failure->{

                }
            }
        }

        viewModel.saveSelectedBranchResponse.observe(this){
            navController.navigate(R.id.salesFragment)
        }

        toggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        navDrawer.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.nav_drawer_logout -> {
                    val dialog = AlertDialog.Builder(this)
                        .setTitle("Salir")
                        .setMessage("¿Cerrar Sesión?")
                        .setPositiveButton("Cerrar Sesión") { view, _ ->
                            viewModel.deleteAuthToken()
                            val intent: Intent = Intent(this, SplashActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        .setNegativeButton("Cancelar") { view, _ ->

                        }
                        .setCancelable(false)
                        .create()
                    dialog.show()
                }
            }
            true
        }

        val bottomNav = binding.bottomNavigation
        navController = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)!!
            .findNavController()
        bottomNav.setupWithNavController(navController)
        navController.navigate(R.id.salesFragment)

        // always show selected Bottom Navigation item as selected (return true)
        bottomNav.setOnItemSelectedListener { item ->
            // In order to get the expected behavior, you have to call default Navigation method manually
//            navController.clearBackStack(item.itemId)
            NavigationUI.onNavDestinationSelected(item, navController)
            return@setOnItemSelectedListener true
        }

    }

    override fun getViewModel(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun getViewBinding(): ActivityEmployeeHomeBinding = ActivityEmployeeHomeBinding.inflate(layoutInflater)

    override fun getActivityRepository(): HomeRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(HomeApi::class.java, token)
        return HomeRepository(api, userPreferences)
    }

}