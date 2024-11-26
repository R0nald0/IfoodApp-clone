package com.example.ifood_clone2.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.core.helpers.TipoLayout
import com.example.core.mensagens.AlertaMensagem
import com.example.core.uistatus.UiStatus
import com.example.domain.models.Categoria
import com.example.ifood_clone2.R
import com.example.ifood_clone2.databinding.FragmentHomeBinding
import com.example.ifood_clone2.model.LojaView
import com.example.ifood_clone2.presentation.adapters.CategoriasAdapter
import com.example.ifood_clone2.presentation.adapters.UltimasLojasAdapter
import com.example.ifood_clone2.presentation.viewmodel.LojaViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapterLojas: UltimasLojasAdapter
    private lateinit var adapterLojasUltimas: UltimasLojasAdapter
    private lateinit var categoriaAdapter: CategoriasAdapter
    private val lojaViewModel by viewModels<LojaViewModel>()
    private val alertaMensagem by lazy {
        AlertaMensagem(requireActivity())
    }

    private val listCategoria = listOf<Categoria>(
        Categoria(
            id = "",
            nome = "Promoção",
            urlImagem =  "https://static.ifood-static.com.br/image/upload/t_medium/discoveries/bagcupones1_eqF1.png?imwidth=128"

        ),
        Categoria(
            id = "",
            nome = "Gourmet",
            urlImagem = "https://static.ifood-static.com.br/image/upload/t_medium/discoveries/gourmet_qo1l.png?imwidth=128"
        ),
        Categoria(
            id = "",
            nome = "Saudável",
            urlImagem = "https://static.ifood-static.com.br/image/upload/t_medium/discoveries/saudaveis_aTKz.png?imwidth=128"
        ),
        Categoria(
             id = "",
            nome = "Brasileiras",
            urlImagem = "https://static.ifood-static.com.br/image/upload/t_medium/discoveries/brasileira_1XfT.png?imwidth=128"
        ),
        Categoria(
            id = "",
            nome = "Prato Feito",
            urlImagem = "https://static.ifood-static.com.br/image/upload/t_medium/discoveries/chamada_2exc.png?imwidth=128"
        ),
        Categoria(
            id = "",
            nome = "Carne",
            urlImagem = "https://static.ifood-static.com.br/image/upload/t_medium/discoveries/carnes_T64X.png?imwidth=128"
        ),
        Categoria(
            id = "",
            nome = "Vegetariana",
            urlImagem = "https://static.ifood-static.com.br/image/upload/t_medium/discoveries/vegetariana_XGvO.png?imwidth=128"
        ),
        Categoria(
            id = "",
            nome = "Arabe",
            urlImagem = "https://static.ifood-static.com.br/image/upload/t_medium/discoveries/arabe_8LSW.png?imwidth=128"
        ),
    )

    val listaSlides = mutableListOf<SlideModel>(
        SlideModel("https://static.ifood-static.com.br/image/upload/t_high/discoveries/1506famososnoifoodprincipal_Qdzl.png?imwidth=1920"),
        SlideModel(
            "https://static.ifood-static.com.br/image/upload/t_high/discoveries/3008diadaesfirraprincipal_FFgW.png?imwidth=1920",
            ScaleTypes.CENTER_CROP
        ),
        SlideModel(
            "https://static.ifood-static.com.br/image/upload/t_high/discoveries/0201restaurantesqueridinhos4principal_8IE6.png?imwidth=1920",
            ScaleTypes.CENTER_CROP
        ),
        SlideModel(
            "https://static.ifood-static.com.br/image/upload/t_high/discoveries/CapaPrincipalGenericoPedeiFoodJaRoxo_vUFs.png?imwidth=1920",
            ScaleTypes.CENTER_CROP
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        inicialiarUltimasLoja()
        inicializarLojaRc()
        inicializarSlider()
        inicialiarRecyclerVireFiltrosCategorias()
        inicializarFiltrosOrdenacao()
        inicializarAvisoNotificacoes()

        return binding.root
    }

    private fun inicializarAvisoNotificacoes() {
        with(binding) {
            val menuItem = tbHome.menu.findItem(R.id.item_notificacao)
            //TODO verificar erro ao receber valor da notificar
            // val textNotificacao = menuItem.actionView?.findViewById<TextView>(R.id.item_notificacao)
            // textNotificacao?.setText("5")
        }
    }

    private fun inicializarSlider() {

        binding.sliderPromocional.setImageList(listaSlides)

        /*binding.sliderPromocional.setItemClickListener(object: ItemClickListener{
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
                TODO("Not yet implemented")
            }

        })*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicialiarUltimasLoja()
        inicializarLojaRc()
        listarLojas()
    }

    private fun inicialiarUltimasLoja() {
        adapterLojasUltimas = UltimasLojasAdapter(TipoLayout.HORIZONTAL) {loja->
            val acao = HomeFragmentDirections.actionHomeFragmentToLojaFragment(loja)
            findNavController().navigate(acao)
        }
        binding.rvUltimasLojas.adapter = adapterLojasUltimas

        binding.rvUltimasLojas.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )

    }

    private fun inicializarLojaRc() {
        adapterLojas = UltimasLojasAdapter(TipoLayout.VERTICAL) {loja->

            val acao = HomeFragmentDirections.actionHomeFragmentToLojaFragment(loja)
            findNavController().navigate(acao)
        }

        binding.idRcvLojas.adapter = adapterLojas
        binding.idRcvLojas.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
    }

    private fun inicialiarRecyclerVireFiltrosCategorias() {

        categoriaAdapter = CategoriasAdapter()
        categoriaAdapter.adicionarLista(listCategoria)

        binding.rvFiltros.adapter = categoriaAdapter
        binding.rvFiltros.layoutManager = GridLayoutManager(
            context, 4
        )

    }

    private fun inicializarFiltrosOrdenacao() {

        val checkedPraRetirar = binding.chPraRetirar.isChecked
        val checkedEntregaGratis = binding.chEntregaGratis.isChecked
        binding.chOrdenaccao.setOnClickListener {
            val listOrdenacao = arrayOf("Ordem Padrao", "Crescente", "Decresente")
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Escolha Uma Ordenaçao")
                .setCancelable(false)
                .setItems(listOrdenacao) { _, posicao ->
                    val itemSelecionado = listOrdenacao[posicao]
                    if (posicao == 0) binding.chOrdenaccao.text = "Ordenação"
                    else binding.chOrdenaccao.text = itemSelecionado
                }.show()
        }
    }


    fun listarLojas(){
        lojaViewModel.listar { uiStatus ->
            when(uiStatus){
                is UiStatus.Erro -> {}
                is UiStatus.Sucesso -> {
                    val lojas = uiStatus.dados
                    adapterLojas.adicionarLista(lojas)
                    adapterLojasUltimas.adicionarLista(lojas)
                }
            }
        }
    }
}