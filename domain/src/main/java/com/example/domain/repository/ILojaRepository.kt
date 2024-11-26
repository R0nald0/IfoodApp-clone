package com.example.domain.repository

import com.example.core.uistatus.UiStatus
import com.example.domain.models.Loja

interface ILojaRepository {
    suspend fun cadastrar(loja: Loja, status: (UiStatus<Boolean>) -> Unit)
     suspend   fun editar(loja: Loja,status: UiStatus<Boolean>)
     suspend   fun recuperarDadosLoja( idLoja :String ,status: (UiStatus<Loja>) -> Unit)
    suspend fun atualizarDadoLoja(dados: Map<String, String>, status: (UiStatus<Boolean>) -> Unit)
    suspend fun  listarLojas(uiStatus:(UiStatus<List<Loja>>) -> Unit)
}