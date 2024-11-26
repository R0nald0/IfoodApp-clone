package com.example.loja.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.domain.models.Produto
import com.example.loja.databinding.ItemRvProdutosBinding
import com.jamiltondamasceno.core.formatarComoMoeda

class ProdutoAdapter(
   val onClickOpcional: (Produto) -> Unit,
    val onClickEditar: (Produto) -> Unit,
   val onClickExcluir: (Produto) -> Unit
) : RecyclerView.Adapter<ProdutoAdapter.ProdutoAdapterViewHolder>() {
    private var listaProdutos = emptyList<Produto>()
    fun adicionarLista( lista: List<Produto>){
        listaProdutos = lista
        notifyDataSetChanged()
    }

    inner class ProdutoAdapterViewHolder(val binding: ItemRvProdutosBinding) :
        ViewHolder(binding.root) {

        fun bind( produto: Produto ){
            binding.textTituloProduto.text = produto.nome


            var textoPreco = ""
            if (produto.produtoEmDestaque){
                binding.txtDestaqueProduto.isVisible = true
                textoPreco = "${produto.precoDestaque.formatarComoMoeda()} ${produto.preco.formatarComoMoeda()}"
              }
            else {
                binding.txtDestaqueProduto.isVisible = false
                textoPreco = produto.preco.formatarComoMoeda()

            }
            binding.btnEditarProduto.setOnClickListener {
                onClickEditar(produto)
            }
            binding.btnExcluirProduto.setOnClickListener {
              onClickExcluir(produto)
            }
            binding.btnAdicionarOpcionais.setOnClickListener {
                onClickOpcional(produto)
            }


            binding.textPrecoProduto.text = textoPreco

            if( produto.urlImagem.isNotEmpty() ){
                Glide.with(binding.root.context)
                    .load( produto.urlImagem)
                    .into( binding.imageProduto)
            }

            binding.btnAdicionarOpcionais.setOnClickListener {
                onClickOpcional( produto )
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRvProdutosBinding.inflate(inflater, parent, false)
        return ProdutoAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listaProdutos.size
    }

    override fun onBindViewHolder(holder: ProdutoAdapterViewHolder, position: Int) {
        val produto = listaProdutos[position]
        holder.bind(produto)
    }

}
