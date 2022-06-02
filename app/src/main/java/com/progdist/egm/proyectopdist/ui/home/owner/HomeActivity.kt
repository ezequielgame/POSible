package com.progdist.egm.proyectopdist.ui.home.owner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.SplashActivity
import com.progdist.egm.proyectopdist.data.network.HomeApi
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.HomeRepository
import com.progdist.egm.proyectopdist.data.responses.branches.Branch
import com.progdist.egm.proyectopdist.data.responses.branches.ExtendedBranch
import com.progdist.egm.proyectopdist.databinding.ActivityHomeBinding
import com.progdist.egm.proyectopdist.ui.CodeScannerAcitvity
import com.progdist.egm.proyectopdist.ui.CreditsActivity
import com.progdist.egm.proyectopdist.ui.base.BaseActivity
import com.progdist.egm.proyectopdist.ui.home.employee.view.EmployeeHomeActivity
import com.progdist.egm.proyectopdist.ui.home.owner.sales.views.NewSaleActivity
import com.progdist.egm.proyectopdist.ui.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class HomeActivity : BaseActivity<HomeViewModel, ActivityHomeBinding, HomeRepository>() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navController: NavController
    private var user: Int = -1
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

    private val askCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if(it){
                val intent = Intent(this, CodeScannerAcitvity::class.java)
//                        intent.putExtra("branchId",selectedBranchId)
                intent.putExtra("userId",user)
                intent.putExtra("tool",true)
                //employee add employeeId
//                        intent.putExtra("context","purchase")
                this.startActivity(intent)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        drawerLayout = binding.drawerLayout
        navDrawer = binding.drawerNavView
        drawerHeader = navDrawer.getHeaderView(0)
        headerName = drawerHeader.findViewById<View>(R.id.tvName) as TextView
        headerMail = drawerHeader.findViewById<View>(R.id.tvEmail) as TextView
        headerRole = drawerHeader.findViewById<View>(R.id.tvRole) as TextView
        headerBranch = drawerHeader.findViewById<View>(R.id.tvBranch) as TextView
        headeImg = drawerHeader.findViewById<View>(R.id.ivUser) as ImageView

        user = intent.getIntExtra("user", -1)

        headerBranch.text = "Selecciona una sucursal"

        viewModel.getUser(user)

        selectedBranchId = runBlocking { userPreferences.idSelectedBranch.first() }

        viewModel.getBranchResponse.observe(this){
            when(it){
                is Resource.success->{
                    selectedBranch = it.value.result[0]
                    headerBranch.text = selectedBranch!!.name_branch
                }
                is Resource.failure->{

                }
            }
        }


        if(selectedBranchId != -1 && selectedBranchId != null){
            viewModel.getBranch(selectedBranchId!!)
        }

        viewModel.getUserResponse.observe(this, Observer {

            when (it) {

                is Resource.success -> {
                    headerName.text = it.value.result[0].name_user
                    headerMail.text = it.value.result[0].email_user
                    headerRole.text = it.value.result[0].name_role
                    if (it.value.result[0].name_role == "Dueño") {

                        headeImg.setImageResource(R.drawable.ic_admin)

                    }
                }

                is Resource.failure -> {

                }
            }
        })


        toggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        val bottomNav = binding.bottomNavigation
        navController = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)!!
            .findNavController()
        bottomNav.setupWithNavController(navController)

        // always show selected Bottom Navigation item as selected (return true)
        bottomNav.setOnItemSelectedListener { item ->
            // In order to get the expected behavior, you have to call default Navigation method manually
            if(item.itemId != R.id.branchesFragment){
                navController.clearBackStack(item.itemId)
            }
            NavigationUI.onNavDestinationSelected(item, navController)
            return@setOnItemSelectedListener true
        }


        navDrawer.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.nav_drawer_codescanner->{

                    val permission = this.checkCallingOrSelfPermission(android.Manifest.permission.CAMERA)
                    if(permission == PackageManager.PERMISSION_GRANTED){

                        val intent = Intent(this, CodeScannerAcitvity::class.java)
//                        intent.putExtra("branchId",selectedBranchId)
                        intent.putExtra("userId",user)
                        intent.putExtra("tool",true)
                        //employee add employeeId
//                        intent.putExtra("context","purchase")
                        this.startActivity(intent)

                    } else {
                        binding.root.showToast("Se necesitan permisos para usar la cámara")
                        askCameraPermission.launch(Manifest.permission.CAMERA)
                    }
                }
                R.id.nav_drawer_current_branch -> {
                    if (branches.isNotEmpty()) {
                        pickBranch()
                        navController.navigate(R.id.branchesFragment)
                    } else {
                        mostrarToast("No has creado ninguna sucursal")
                    }

                }
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
                R.id.nav_drawer_about->{
                    val intent = Intent(this, CreditsActivity::class.java)
                    this.startActivity(intent)
                }

            }
            true
        }





    }

    fun pickBranch(): ExtendedBranch? {
        val branchesNames: MutableList<String> = ArrayList()
        branches.forEach {
            branchesNames += it.name_branch
        }
        val branchesOptions: Array<String> = branchesNames.toTypedArray()

        val builder = MaterialAlertDialogBuilder(this)
        // dialog title
        builder.setTitle("Selecciona una sucursal")

        builder.setSingleChoiceItems(
            branchesOptions, when (selectedBranch) {
                null -> {
                    -1
                }
                else -> {
//                    branches.indexOf(selectedBranch)
                    -1
                }
            }
        ) { dialog, i -> }

        // alert dialog positive button
        builder.setPositiveButton("Guardar") { dialog, which ->
            val position = (dialog as AlertDialog).listView.checkedItemPosition
            // if selected, then get item text
            if (position != -1) {
                viewModel.getBranch(branches[position].id_branch)
                viewModel.saveIdSelectedBranch(branches[position].id_branch)
                headerBranch.text = branchesNames[position]
            }
        }

        // alert dialog other buttons
        builder.setNegativeButton("Cancelar", null)

        // set dialog non cancelable
        builder.setCancelable(true)

        // finally, create the alert dialog and show it
        val dialog = builder.create()
        dialog.show()

        return selectedBranch
    }

    fun mostrarToast(mensaje: String) = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()

    override fun getViewModel(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun getViewBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)

    override fun getActivityRepository(): HomeRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(HomeApi::class.java, token)
        return HomeRepository(api, userPreferences)
    }

}