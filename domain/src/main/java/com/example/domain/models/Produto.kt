package com.example.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Produto(
    val id :String = "",
    val nome :String ="",
    val descricao  :String ="",
    val preco :Double = 0.0,
    val precoDestaque :Double = 0.0,
    val urlImagem :String ="",
    val produtoEmDestaque :Boolean = false
):Parcelable

fun Produto.toMap() : Map<String,Any>{
    val dados = mutableMapOf<String,Any>()

    dados["id"] = this.id
    dados["nome"] = this.nome
    dados["descricao"]=this.descricao
    dados["preco"] = this.preco
    dados["precoDestaque"] = this.precoDestaque
    dados["urlImagem"]= this.urlImagem
    dados["produtoEmDestaque"] = this.produtoEmDestaque



    return  dados
}