package com.example.domain.repository

import com.example.core.uistatus.UiStatus
import com.example.domain.models.Opcional

interface IOpcionalRepository {
    suspend fun salvar(opcional: Opcional, status: (UiStatus<String>) -> Unit)
    suspend   fun remover(idProduto :String,idOpcional :String, status: (UiStatus<Boolean>) -> Unit)
    suspend   fun listar( idLoja : String, idProduto :String,status: ( UiStatus<List<Opcional>>) -> Unit)
}