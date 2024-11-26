package com.example.domain.models

data class Opcional(
    val id: String ="",
    val nome: String = "",
    val descricao: String = "",
    val preco: Double = 0.0,
    val uriImagem: String = "",
    val idProduto: String = "",
)