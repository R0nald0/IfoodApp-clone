package com.example.ifood_clone2.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.core.helpers.TipoLayout
import com.example.core.mensagens.AlertaMensagem
import com.example.core.uistatus.UiStatus
import com.example.domain.models.Loja
import com.example.domain.usecase.impl.TipoProduto
import com.example.ifood_clone2.R
import com.example.ifood_clone2.databinding.FragmentLojaBinding
import com.example.ifood_clone2.presentation.adapters.ProdutosAdapter
import com.example.ifood_clone2.presentation.viewmodel.ProdutoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LojaFragment : Fragment() {
    private lateinit var binding : FragmentLojaBinding
    private lateinit var produtosAdapter : ProdutosAdapter
    private lateinit var produtosAdapterDestaque : ProdutosAdapter
    private val args: LojaFragmentArgs by navArgs()

    private val produtoViewModel by viewModels<ProdutoViewModel>()
    private val alertaCar by lazy {
        AlertaMensagem(requireActivity())
    }

    lateinit var loja :Loja

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLojaBinding.inflate(inflater,container,false)
        inicializarToolbar()
        inicializarRecyclerViewProduto()
        inicializarRecyclerViewProdutoDestaque()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exibirDadosLoja()

    }

    private fun exibirDadosLoja() {
         loja = args.loja

        if (loja.urlPerfil.isNotEmpty()){
            Glide.with(this).load(loja.urlPerfil).into(binding.imgIconeLoja)
        }
        if (loja.urlCapa.isNotEmpty()){
            Glide.with(this).load(loja.urlPerfil).into(binding.imgCapa)
        }
        binding.txvTituuloLoja.text = loja.nome
        binding.txvCategoriaLoja.text = loja.categoria

        listarProdutos(loja.idLoja)

    }

    private fun listarProdutos(idLoja :String) {
      produtoViewModel.listar(idLoja){ uiStatus ->
          when(uiStatus){
              is UiStatus.Erro -> {}
              is UiStatus.Sucesso -> {
                  val produtos = uiStatus.dados
                  val produtosEmDestaque = produtos.find {  it.tipo == TipoProduto.PRODUTO_EM_DESTAQUE  }?.listaDeProdutos ?: emptyList()
                  val totalProdutos = produtos.find {  it.tipo == TipoProduto.PRODUTO  }?.listaDeProdutos ?: emptyList()

                  produtosAdapter.adicionarLista(totalProdutos)
                  produtosAdapterDestaque.adicionarLista(produtosEmDestaque)

              }
          }
      }

    }

    fun inicializarRecyclerViewProdutoDestaque(){
        produtosAdapterDestaque = ProdutosAdapter(TipoLayout.HORIZONTAL){ produto->

            val acao = LojaFragmentDirections.actionLojaFragmentToProdutosFragment(produto,loja)
            findNavController().navigate(acao)

        }

        binding.rvProdutosDestaque.adapter =produtosAdapter

        binding.rvProdutosDestaque.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL,false
        )

    }
    private fun inicializarRecyclerViewProduto(){
        produtosAdapter = ProdutosAdapter(TipoLayout.VERTICAL){produto->
            findNavController().navigate(R.id.produtosFragment)

        }

        binding.rvProdutos.adapter =produtosAdapter

        binding.rvProdutos.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL,false
        )

    }
    private fun inicializarToolbar() {

        with(binding){

            btnNavLojaVoltar.setOnClickListener {
                findNavController().navigate(R.id.homeFragment)
            }

            val appCompatActivity = (activity as AppCompatActivity)
            appCompatActivity.setSupportActionBar( collapsedToolbar )
            appCompatActivity.addMenuProvider(
                object : MenuProvider {
                    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                        menuInflater.inflate(R.menu.menu_loja_pesquisa, menu)
                    }

                    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                        return true
                    }

                },
                viewLifecycleOwner
            )

            //Configuração AppBar
            /*appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                println("AppBarChange : $verticalOffset")
                //0 - 357
                val navegacaoVertical = abs(verticalOffset)
                if( navegacaoVertical >= appBarLayout.totalScrollRange ){//Colapsado/fechado
                    textNavLojaTitulo.text = "Outback"
                }else if( navegacaoVertical == 0 ){//Expandido
                    textNavLojaTitulo.text = ""
                }else{//Scrool está acontecendo

                }
            }*/

        }

        //navControler.currentDestination?.label = ""
        //toolbar.setupWithNavController(navControler)
        //NavigationUI.setupWithNavController(toolbar, navControler)

    }
}