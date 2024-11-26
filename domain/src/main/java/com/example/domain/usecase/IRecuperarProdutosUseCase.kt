package com.example.domain.usecase

import com.example.core.uistatus.UiStatus
import com.example.domain.models.Produto
import com.example.domain.usecase.impl.ProdutoSeparados

interface IRecuperarProdutosUseCase {
    suspend  operator fun invoke(idLoga:String,uiStatus: (UiStatus<List<ProdutoSeparados>>) ->Unit)
}