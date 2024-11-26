package com.example.ifood_clone2.presentation.adapters

import android.view.LayoutInflater
import com.example.core.helpers.TipoLayout
import com.example.domain.models.Produto
import com.example.ifood_clone2.databinding.ItemRvProutosDestaqueBinding
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.models.Loja
import com.example.ifood_clone2.databinding.ItemRvProdutosBinding
import com.jamiltondamasceno.core.formatarComoMoeda

class ProdutosAdapter(
    private val tipoLayout : TipoLayout,
    private val onClick: (Produto) ->Unit
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listProduto= emptyList<Produto>()

    fun adicionarLista(lista:List<Produto>){
        listProduto = lista
        // notifyDataSetChanged()
    }

    inner class ProdutosDestaqueViewHolder(produtosDestaqueItemLayout : ItemRvProutosDestaqueBinding):
        RecyclerView.ViewHolder(produtosDestaqueItemLayout.root){
        val  binding : ItemRvProutosDestaqueBinding = produtosDestaqueItemLayout

        fun bind(produto :Produto){
            binding.textTituloDestaque.text = produto.nome

            if( produto.precoDestaque.toString().isNotEmpty()){
                binding.textPreco1Destaque.text = produto.precoDestaque.toString()
                binding.textPreco2Destaque.text = produto.preco.toString()
            }else{
                binding.textPreco1Destaque.text = produto.preco.toString()
            }

            if (produto.urlImagem.isNotEmpty()){
                Glide.with(this.itemView).load(produto.urlImagem).into(binding.imageProdutoDestaque)
            }

            binding.clItemProdutoDestaque.setOnClickListener {
                onClick(produto)
            }


        }
    }

    inner class ProdutosViewHolder(itemProdutoLayout : ItemRvProdutosBinding):
        RecyclerView.ViewHolder(itemProdutoLayout.root){
        val  binding : ItemRvProdutosBinding
        init {
            binding = itemProdutoLayout
        }
        fun bind(produto : Produto){

            binding.textTituloProduto.text = produto.nome
            binding.textDescricaoProduto.text = produto.descricao
            if (produto.preco != 0.0){
                binding.textPrecoProduto.text = produto.preco.formatarComoMoeda()
               // binding.textPrecoProduto.text = produto.preco.toString()
            }else{
                binding.textPrecoProduto.text = produto.preco.formatarComoMoeda()
            }

            if (produto.urlImagem.isNotEmpty()){
                Glide.with(this.itemView).load(produto.urlImagem).into(binding.imageProduto)
            }
            binding.clItemProdutoDestaque.setOnClickListener {
                onClick(produto)
            }
        }
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val produto = listProduto[position]
        when(holder){
            is ProdutosAdapter.ProdutosDestaqueViewHolder ->{
                holder.bind(produto)
            }
            is ProdutosAdapter.ProdutosViewHolder ->{
                holder.bind(produto)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (tipoLayout == TipoLayout.HORIZONTAL){
            val view = ItemRvProutosDestaqueBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return ProdutosDestaqueViewHolder(view)
        }

        val view = ItemRvProdutosBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProdutosViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listProduto.size
    }

}
