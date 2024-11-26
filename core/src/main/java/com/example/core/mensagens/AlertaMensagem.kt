package com.example.core.mensagens

import android.app.Activity
import android.app.AlertDialog
import android.view.ViewGroup
import com.example.core.R


class AlertaMensagem(private val activity: Activity) {

    private  var alertDialog : AlertDialog? =null
    private  val layoutInflater = activity.layoutInflater.inflate(
        R.layout.layout_carregando,null
    )
    fun call(mensagem: String){
        val alertBuilder = AlertDialog
            .Builder(activity)
            .setMessage(mensagem)
            .setView(layoutInflater)
            .setCancelable(false)

        alertDialog = alertBuilder.create()
        alertDialog?.show()
    }


    fun hide(){
        alertDialog?.setOnDismissListener {
            val viewGroup = layoutInflater.parent as ViewGroup
            viewGroup.removeView(layoutInflater)
        }
        alertDialog?.dismiss()
    }
}