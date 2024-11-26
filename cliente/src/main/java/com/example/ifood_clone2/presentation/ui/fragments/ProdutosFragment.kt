package com.example.ifood_clone2.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.core.uistatus.UiStatus
import com.example.ifood_clone2.R
import com.example.ifood_clone2.databinding.FragmentProdutosBinding
import com.example.ifood_clone2.presentation.adapters.OpcionalAdapter
import com.example.ifood_clone2.presentation.viewmodel.ProdutoViewModel
import com.jamiltondamasceno.core.formatarComoMoeda
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProdutosFragment : Fragment() {
    private lateinit var binding: FragmentProdutosBinding
    private lateinit var opcionalAdapter: OpcionalAdapter
    private val args: ProdutosFragmentArgs by navArgs()
    private val produtoViewModel by viewModels<ProdutoViewModel>()
    private lateinit var idProduto: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProdutosBinding.inflate(inflater, container, false)
        inicializarRecyclerViewAdicionais()
        inicializarToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exibirDadosDeProduto()
        exibirOpcionais()
    }

    private fun exibirOpcionais() {
        produtoViewModel.listarOpcionais(
            idLoja = args.loja.idLoja,
            idProduto = args.produto.id
        ) { uiStatus ->
            when (uiStatus) {
                is UiStatus.Erro -> {}
                is UiStatus.Sucesso -> {
                    opcionalAdapter.adicionarLista(uiStatus.dados)
                }
            }

        }
    }

    private fun exibirDadosDeProduto() {
        val produto = args.produto
        idProduto = produto.id
        if (produto.urlImagem.isNotEmpty()) {
            Glide.with(this).load(produto.urlImagem).into(binding.imgProdutoCapa)
        }
        binding.txvTituloProduto.text = produto.nome
        binding.txvDescricaoProduto.text = produto.descricao
        if (produto.produtoEmDestaque) {
            binding.txvPrecoProduto.text = produto.preco.formatarComoMoeda()

        } else {
            binding.txvPrecoProduto.text = produto.precoDestaque.formatarComoMoeda()
        }


    }

    private fun inicializarRecyclerViewAdicionais() {
        opcionalAdapter = OpcionalAdapter()

        binding.rvAdicionais.adapter = opcionalAdapter
        binding.rvAdicionais.layoutManager = LinearLayoutManager(context)
    }

    private fun inicializarToolbar() {

        with(binding) {
            btnNavProdutoVoltar.setOnClickListener {
                findNavController().navigate(R.id.lojaFragment)
            }
            btnAdicionarProdudto.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        //navControler.currentDestination?.label = ""
        //toolbar.setupWithNavController(navControler)
        //NavigationUI.setupWithNavController(toolbar, navControler)

    }

}