package com.progdist.egm.proyectopdist.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.progdist.egm.proyectopdist.ui.home.owner.HomeActivity

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

fun View.showToast(msg: String){

    Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show()
    
}

fun TextView.setTextAnimation(text: String, duration: Long = 300, completion: (() -> Unit)? = null) {
    fadOutAnimation(duration) {
        this.text = text
        fadInAnimation(duration) {
            completion?.let {
                it()
            }
        }
    }
}


fun View.fadOutAnimation(duration: Long = 300, visibility: Int = View.INVISIBLE, completion: (() -> Unit)? = null) {
    animate()
        .alpha(0f)
        .setDuration(duration)
        .withEndAction {
            this.visibility = visibility
            completion?.let {
                it()
            }
        }
}

fun View.fadInAnimation(duration: Long = 300, completion: (() -> Unit)? = null) {
    alpha = 0f
    visibility = View.VISIBLE
    animate()
        .alpha(1f)
        .setDuration(duration)
        .withEndAction {
            completion?.let {
                it()
            }
        }
}

@SuppressLint("Range")
fun ContentResolver.getFileName(uri: Uri): String {

    var name = ""
    val cursor = query(uri, null, null, null, null)
    cursor?.use {
        it.moveToFirst()
        name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }
    return name

}