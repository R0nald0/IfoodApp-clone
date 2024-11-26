package com.example.domain.usecase.impl

import com.example.core.uistatus.UiStatus
import com.example.domain.models.Produto
import com.example.domain.repository.IProdutoRepository
import com.example.domain.usecase.IRecuperarProdutosUseCase
import javax.inject.Inject

class RecuperarProdutosImpl @Inject  constructor(
    private val produtoRepository: IProdutoRepository,
) :IRecuperarProdutosUseCase {
    override suspend fun invoke(idLoga: String, uiStatus: (UiStatus<List<ProdutoSeparados>>) -> Unit) {
        produtoRepository.listar(idLoga){statusRecuperado ->
            when(statusRecuperado){
                is UiStatus.Erro -> uiStatus.invoke(statusRecuperado)
                is UiStatus.Sucesso -> {
                    val listaProdutos = statusRecuperado.dados
                    val produtoEmDestaque = listaProdutos.filter { it.produtoEmDestaque }
                    val produtos = listaProdutos.filter { !it.produtoEmDestaque }

                    val produtoSeparados = listOf(
                        ProdutoSeparados(TipoProduto.PRODUTO_EM_DESTAQUE,produtoEmDestaque),
                        ProdutoSeparados(TipoProduto.PRODUTO,produtos),
                    )

                    uiStatus.invoke(UiStatus.Sucesso(produtoSeparados))

                }
            }

        }

    }
}

enum class TipoProduto {
    PRODUTO_EM_DESTAQUE,
    PRODUTO
}

data class ProdutoSeparados(
    val tipo :TipoProduto,
    val listaDeProdutos:List<Produto>
)