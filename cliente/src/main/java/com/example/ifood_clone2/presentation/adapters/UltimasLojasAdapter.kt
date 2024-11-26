package com.example.ifood_clone2.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.core.helpers.TipoLayout
import com.example.domain.models.Loja
import com.example.ifood_clone2.databinding.ItemRvLojasBinding
import com.example.ifood_clone2.databinding.ItemRvUltimasLojasBinding
import com.example.ifood_clone2.model.LojaView

class UltimasLojasAdapter(
    private val tipoLayout : TipoLayout,
    private val onClick: (Loja) ->Unit
) : RecyclerView.Adapter<ViewHolder>() {

    private var listLojas= emptyList<Loja>()

    fun adicionarLista(lista:List<Loja>){
        listLojas = lista
        notifyDataSetChanged()
    }


    inner class UltimasLojasViewHolder(ultimasLojasItemLayout : ItemRvUltimasLojasBinding):ViewHolder(ultimasLojasItemLayout.root){
        val  binding : ItemRvUltimasLojasBinding
        init {
            binding = ultimasLojasItemLayout
        }
        fun bind(loja :Loja){
            binding.tvXNomeLoja.text = loja.nome
            if (loja.urlPerfil.isNotEmpty()){
                Glide.with(this.itemView).load(loja.urlPerfil).into(binding.imvLoja)
            }
            binding.clLojaUltima.setOnClickListener {
                onClick(loja)
            }

        }
    }

    inner class LojasViewHolder(lojaitemLayout : ItemRvLojasBinding):ViewHolder(lojaitemLayout.root){
        val  binding :ItemRvLojasBinding
        init {
            binding = lojaitemLayout
        }

        fun bind(loja : Loja){
            binding.nomeLoja.text = loja.nome
            if (loja.urlPerfil.isNotEmpty()){
                Glide.with(this.itemView).load(loja.urlPerfil).into(binding.imvLojaFoto)
            }
            binding.clLoja.setOnClickListener {
                onClick(loja)
            }

        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (tipoLayout == TipoLayout.HORIZONTAL){
            val view = ItemRvUltimasLojasBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return UltimasLojasViewHolder(view)
        }

        val view = ItemRvLojasBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LojasViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listLojas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val loja = listLojas[position]
        when(holder){
            is UltimasLojasViewHolder ->{
                holder.bind(loja)
            }
            is LojasViewHolder->{
                holder.bind(loja)
            }

        }

    }

}