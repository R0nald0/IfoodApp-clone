package com.example.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
fun <T> Activity.navegarPara(destino : Class<T>){
    startActivity(Intent(this,destino))
    finish();
}

fun View.esconderTeclado(){
    val inputMethodManager  =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    if(inputMethodManager.isAcceptingText){
        inputMethodManager
            .hideSoftInputFromWindow(windowToken,0)
    }

}


fun View.exibirTecladoEFoco(){
    if(requestFocus()){
        val inputMethodManager  =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this,0)

    }

}

fun Activity.exibirMensagem(mensagem :String){
    Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show()
}
