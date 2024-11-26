package com.example.ifood_clone2.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.core.helpers.TipoLayout
import com.example.domain.models.Pedido
import com.example.domain.models.Produto
import com.example.ifood_clone2.databinding.ItemRvPedidoBinding
import com.example.ifood_clone2.databinding.ItemRvProdutosBinding
import com.example.ifood_clone2.databinding.ItemRvProutosDestaqueBinding

class PedidoAdapter (
    private val onClick: () ->Unit
):RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>(){

    var listPedido = emptyList<Pedido>()

    fun adicionarPediddo(listaPedido : List<Pedido>){
        listPedido = listaPedido
    }

   inner  class  PedidoViewHolder(private val binding: ItemRvPedidoBinding):ViewHolder(binding.root){

       fun bind(pedido: Pedido){
             binding.textDataPedido.text = pedido.dataPedido
           binding.txtTitleLojaPedido.text = pedido.tituloLoja

           var textoItem = ""
           pedido.itens.forEach {
               textoItem += "1-$it"
           }
           binding.textListaPedido.text =textoItem
           if (pedido.imagemLoja.isNotEmpty()){
               Glide.with(binding.root).load(pedido.imagemLoja)
           }
       }
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = ItemRvPedidoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return  PedidoViewHolder(view)
    }

    override fun getItemCount() = listPedido.size

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = listPedido[position]
        holder.bind(pedido)
    }
}
