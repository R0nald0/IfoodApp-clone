package com.example.ifood_clone2.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.models.Opcional
import com.example.ifood_clone2.databinding.ItemRvAdicionaisBinding
import com.jamiltondamasceno.core.formatarComoMoeda

class OpcionalAdapter : RecyclerView.Adapter<OpcionalAdapter.AdicionalViewHolder>(){
    private var listaAdcional = emptyList<Opcional>()

    fun adicionarLista(adcionalList: List<Opcional>) {
         listaAdcional  =  adcionalList
       notifyDataSetChanged()
    }

    inner class AdicionalViewHolder (val binding : ItemRvAdicionaisBinding): RecyclerView.ViewHolder(binding.root){

        fun  bind(opcional: Opcional){
            binding.textAdicionalTirulo.text = opcional.nome
            binding.textPreco.text = opcional.preco.formatarComoMoeda()
            binding.textDescricao.text = opcional.descricao
            Glide.with(binding.root)
                .load(opcional.uriImagem)
                .into(binding.imageAdcional)


            binding.btnRemover.setOnClickListener {  }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdicionalViewHolder {
       val itemAdcionalLayout =LayoutInflater.from(parent.context)
        val adicionaisBinding = ItemRvAdicionaisBinding.inflate(itemAdcionalLayout, parent, false)
        return  AdicionalViewHolder(adicionaisBinding)
    }

    override fun getItemCount() =listaAdcional.size

    override fun onBindViewHolder(holder: AdicionalViewHolder, position: Int) {
       val opcional = listaAdcional[position]
       holder.bind(opcional)
    }
}
