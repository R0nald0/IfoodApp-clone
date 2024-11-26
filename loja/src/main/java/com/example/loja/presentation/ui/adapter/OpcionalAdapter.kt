package com.example.loja.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.domain.models.Opcional
import com.example.loja.databinding.ItemRvOpcionaisBinding
import com.jamiltondamasceno.core.formatarComoMoeda

class OpcionalAdapter(
   private val onclickExcluir: (Opcional) -> Unit) :
    RecyclerView.Adapter<OpcionalAdapter.OpcionalViewHolder>() {

    private var listaOpcionais = emptyList<Opcional>()
    fun adicionarLista( lista: List<Opcional>){
        listaOpcionais = lista
        notifyDataSetChanged()
    }

    inner class OpcionalViewHolder(private val binding: ItemRvOpcionaisBinding) : ViewHolder(binding.root) {
        fun bind(opcional: Opcional) {

            binding.textTituloOpcional.text = opcional.nome
            binding.textDescricaoOpcional.text = opcional.descricao
            binding.textPrecoOpcional.text = opcional.preco.formatarComoMoeda()

            if (opcional.uriImagem.isNotEmpty()) {
                Glide.with(binding.root.context)
                    .load(opcional.uriImagem)
                    .into(binding.imageOpcional)
            }
            binding.btnExcluirOpcional.setOnClickListener {
                onclickExcluir(opcional)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpcionalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemRvOpcionalProduto = ItemRvOpcionaisBinding.inflate(inflater, parent, false)
        return OpcionalViewHolder(itemRvOpcionalProduto)
    }

    override fun getItemCount(): Int {
        return listaOpcionais.size
    }

    override fun onBindViewHolder(holder: OpcionalViewHolder, position: Int) {
        val opcional = listaOpcionais[position]
        holder.bind(opcional)
    }
}
