package com.example.domain.models

data class Pedido (
    val imagemLoja: String,
    val tituloLoja :String,
    val itens :List<String>,
    val dataPedido:String
)