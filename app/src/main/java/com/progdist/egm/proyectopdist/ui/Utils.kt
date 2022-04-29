package com.progdist.egm.proyectopdist.ui

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.progdist.egm.proyectopdist.ui.home.HomeActivity

fun <A: Activity> Activity.startNewActivity(activity: Class<HomeActivity>){

    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //Back button not returning

        startActivity(it)
    }

}

fun View.visible(isVisible: Boolean){

    visibility = if(isVisible) View.VISIBLE else View.GONE

}

fun View.enable(enabled: Boolean){

    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f

}