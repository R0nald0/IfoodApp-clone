package com.example.ifood_clone2.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.helpers.TipoLayout
import com.example.domain.models.Produto
import com.example.ifood_clone2.R
import com.example.ifood_clone2.databinding.FragmentFinalizarPedidoBinding
import com.example.ifood_clone2.presentation.adapters.ProdutosAdapter

class FinalizarPedidoFragment : Fragment() {
     private val binding  by lazy {
         FragmentFinalizarPedidoBinding.inflate(layoutInflater)
     }

    private lateinit var produtosAdapter : ProdutosAdapter

    private val listaProddutor = listOf(
        Produto(
            id = "",
           nome =  "CHOPP BRAHMA OUTBACK 1L COM 25% DE DESCONTO",
            descricao = "O Chopp Brahma Outback com o sabor....",
            preco =  23.40,
            precoDestaque = 20.40,
            urlImagem = "https://static.ifood-static.com.br/image/upload/t_medium/pratos/5221af98-5ad4-42e2-a767-23d1545b82d5/202011181213_E09M_.jpeg"
        ),


    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inicializarRecyclerViewProduto()
        return binding.root
    }

    fun inicializarRecyclerViewProduto(){
        val activity  = activity as AppCompatActivity

        binding.includeToolbar.tbPrincipal.apply {
            activity.setSupportActionBar(this)

            activity.supportActionBar?.apply {
                isTitleCentered =true
                title="Confirmar dados do pedido"
                setDisplayHomeAsUpEnabled(true)
                setNavigationIcon(R.drawable.back_ios_new_24)
                setNavigationOnClickListener {
                    findNavController().popBackStack()
                }
            }

        }
        produtosAdapter = ProdutosAdapter(TipoLayout.VERTICAL){
            findNavController().navigate(R.id.produtosFragment)
        }
      produtosAdapter.adicionarLista(listaProddutor)
        binding.rvItemsPedidos.adapter =produtosAdapter

        binding.rvItemsPedidos.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL,false
        )


    }
}