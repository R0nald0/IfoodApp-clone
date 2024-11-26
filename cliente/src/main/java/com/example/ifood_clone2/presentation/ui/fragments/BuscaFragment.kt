package com.example.ifood_clone2.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.helpers.TipoLayout
import com.example.domain.models.Categoria
import com.example.domain.models.Produto
import com.example.ifood_clone2.R
import com.example.ifood_clone2.databinding.FragmentBuscaBinding
import com.example.ifood_clone2.databinding.FragmentHomeBinding
import com.example.ifood_clone2.databinding.FragmentLojaBinding
import com.example.ifood_clone2.model.LojaView
import com.example.ifood_clone2.presentation.adapters.CategoriasAdapter
import com.example.ifood_clone2.presentation.adapters.ProdutosAdapter
import com.example.ifood_clone2.presentation.adapters.UltimasLojasAdapter


class BuscaFragment : Fragment() {
    private  val binding by lazy {
        FragmentBuscaBinding.inflate(layoutInflater)
    }
    private lateinit var adapterLojas : UltimasLojasAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        inicializarLojaRc()
        return binding.root
    }

    fun inicializarLojaRc(){
        adapterLojas =UltimasLojasAdapter(TipoLayout.VERTICAL){
            findNavController().navigate(R.id.action_homeFragment_to_lojaFragment)
            // Toast.makeText(this.context, "clicou", Toast.LENGTH_SHORT).show()
        }
       // adapterLojas.adicionarLista(listaLojas)
        binding.rvBuscaLojas.adapter =adapterLojas
        binding.rvBuscaLojas.layoutManager = LinearLayoutManager(
            context,LinearLayoutManager.VERTICAL,false
        )
    }
}