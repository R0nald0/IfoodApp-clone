package com.example.domain.repository

import com.example.core.uistatus.UiStatus

import com.example.domain.models.Produto

interface IProdutoRepository {
    suspend fun salvar(produto:  Produto, status: (UiStatus<String>) -> Unit)
    suspend   fun atualizar(produto: Produto, status: (UiStatus<String>) -> Unit)
    suspend   fun listar(idloja:String,status: (UiStatus<List<Produto>>) -> Unit)
    suspend   fun recuperarPeloId(idLoja : String, idProduto :String ,status: (UiStatus<Produto>) -> Unit)
    suspend   fun remover(idProduto :String ,status: (UiStatus<Boolean>) -> Unit)
}