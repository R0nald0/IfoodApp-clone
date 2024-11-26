package com.example.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Loja(
    var idLoja: String = "",
    var email: String = "",
    val idCategoria: String = "",
    val categoria: String = "",
    val nome: String = "",
    val razaoSocial: String = "",
    val cnpj: String = "",
    val sobreEmpresa: String = "",
    val telefone: String = "",
    var urlPerfil: String = "",
    var urlCapa: String = "",
):Parcelable

fun Loja.toMap() : Map<String,Any>{
  val dados = mutableMapOf<String,Any>()

    dados["idLoja"] = this.idLoja
    dados["email"] = this.email
     dados["idCategoria"]=this.idCategoria
    dados["categoria"] = this.categoria
    dados["nome"] = this.nome
    dados["razaoSocial"]= this.razaoSocial
    dados["cnpj"] = this.cnpj
    dados["sobreEmpresa"] = this.sobreEmpresa
    dados["telefone"] = this.telefone
    if (this.urlPerfil.isNotEmpty()) dados["urlPerfil"]= this.urlPerfil
    if (this.urlCapa.isNotEmpty()) dados["urlCapa"]= this.urlCapa


    return  dados
}