package com.example.loja.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core.exibirMensagem
import com.example.core.mensagens.AlertaMensagem
import com.example.core.uistatus.UiStatus
import com.example.domain.models.Produto
import com.example.loja.databinding.ActivityCardapioBinding
import com.example.loja.presentation.ui.adapter.ProdutoAdapter
import com.example.loja.presentation.viewmodel.ProdutoViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.jamiltondamasceno.core.formatarComoMoeda
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardapioActivity : AppCompatActivity() {
     private val binding by lazy {
         ActivityCardapioBinding.inflate(layoutInflater)
     }
    private val produtoViewModel by viewModels<ProdutoViewModel>()
    lateinit var  idLoja :String
    private val alertaCarregamento by lazy {
        AlertaMensagem(this)
    }
    private lateinit var produtoAdapter: ProdutoAdapter

    private var listaProdutos = emptyList<Produto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        idLoja =  FirebaseAuth.getInstance().currentUser?.uid  ?: ""
        inicializar()
    }

    override fun onStart() {
        recuperarProdutos()
        super.onStart()
    }

    private fun inicializar() {
        inicializarToolbar()
        inicializarProdutos()
        incializarEventoClique()
    }

    private fun inicializarProdutos() {
        produtoAdapter = ProdutoAdapter(
           onClickOpcional =  {

            },
           onClickEditar =  {produto->
             val intent = Intent(this,AdicionarProdutoActivity::class.java)
               intent.putExtra("idProduto",produto.id)
               startActivity(intent)
            },
           onClickExcluir =  {produto->
               confirmarExclusao(produto)
           }
        )
        produtoAdapter.adicionarLista( listaProdutos )
        binding.rvProdutosCardapio.adapter = produtoAdapter
        binding.rvProdutosCardapio.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL, false
        )
    }

    private fun confirmarExclusao(produto: Produto) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Deseja realmente excluir o produto")
            .setMessage("[${produto.nome}] ${produto.preco.formatarComoMoeda()}")
            .setNegativeButton("Cancelar"){dialog ,_ ->
                dialog.dismiss()
            }
            .setPositiveButton("Confirmo exclusão"){dialog ,_ ->
                 removerProduto(produto.id)
                dialog.dismiss()
            }
            .show().create()


    }

    private fun removerProduto(idProduto: String) {
        produtoViewModel.removerProduto(idProduto){uiStatus->
            when(uiStatus){
                is UiStatus.Erro ->exibirMensagem(uiStatus.mensagem)
                is UiStatus.Sucesso -> {
                    recuperarProdutos()
                    exibirMensagem("Produto removido")
                }
            }
        }
    }

    private fun incializarEventoClique() {
        with(binding){
            fabAdicionarProduto.setOnClickListener {
               // navegarPara(AdicionarProdutoActivity::class.java)

            }
        }
    }
    fun recuperarProdutos(){
            produtoViewModel.listar(idLoja) { uiStatus ->
                when(uiStatus){
                    is UiStatus.Erro -> exibirMensagem(uiStatus.mensagem)
                    is UiStatus.Sucesso -> {
                        listaProdutos  = uiStatus.dados
                        produtoAdapter.adicionarLista(listaProdutos)
                    }
                }

            }

    }

    private fun inicializarToolbar() {
       /* val toolbar = binding.includeTbCardapio.toolbar
        setSupportActionBar( toolbar )

        supportActionBar?.apply {
            title = "Cardápio de produtos"
            setDisplayHomeAsUpEnabled(true)
        }*/

    }
}