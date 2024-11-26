package com.example.domain.models

data class Usuario(
    val id:String= "",
    val nome:String= "",
    val email:String,
    val senha:String,
    val telefone:String="",
    val urlImagemPerfil : String =""
)