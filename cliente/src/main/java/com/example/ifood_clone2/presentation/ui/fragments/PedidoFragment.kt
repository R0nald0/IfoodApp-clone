package com.example.ifood_clone2.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.helpers.TipoLayout
import com.example.domain.models.Pedido
import com.example.ifood_clone2.R
import com.example.ifood_clone2.databinding.FragmentPedidoBinding
import com.example.ifood_clone2.presentation.adapters.PedidoAdapter
import com.example.ifood_clone2.presentation.adapters.UltimasLojasAdapter


class PedidoFragment : Fragment() {

    private val binding by lazy {
        FragmentPedidoBinding.inflate(layoutInflater)
    }

    private lateinit var pedidoAdapter : PedidoAdapter

    val listaPedido = listOf(
        Pedido(
              imagemLoja = "https://static.ifood-static.com.br/image/upload/t_medium/logosgde/854928e6-3ce7-45aa-9558-20b9e548cf31/202211041241_DDGZ_i.jpg?imwidth=96",
            tituloLoja = "Mcdonald's",
              dataPedido = "23/12/2010",
             itens = listOf("Huburguer duplo","coca")
        ),
        Pedido(
            imagemLoja = "https://static.ifood-static.com.br/image/upload/t_medium/logosgde/854928e6-3ce7-45aa-9558-20b9e548cf31/202211041241_DDGZ_i.jpg?imwidth=96",
            tituloLoja = "Mcdonald's",
            dataPedido = "23/12/2010",
            itens = listOf("Huburguer duplo","coca")
        ),
        Pedido(
            imagemLoja = "https://static.ifood-static.com.br/image/upload/t_medium/logosgde/854928e6-3ce7-45aa-9558-20b9e548cf31/202211041241_DDGZ_i.jpg?imwidth=96",
            tituloLoja = "Mcdonald's",
            dataPedido = "23/12/2010",
            itens = listOf("Huburguer duplo","coca")
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inicialiazarRecyclerView()
        return binding.root
    }

    override fun onStart() {
        pedidoAdapter.adicionarPediddo(listaPedido)
        super.onStart()

    }
    fun inicialiazarRecyclerView(){
        pedidoAdapter = PedidoAdapter {  }

        binding.rvPedidos.adapter =pedidoAdapter

        binding.rvPedidos.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL,false
        )

    }


}