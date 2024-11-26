package com.example.loja.presentation.ui.activity

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.core.esconderTeclado
import com.example.core.exibirMensagem
import com.example.core.mensagens.AlertaMensagem
import com.example.core.navegarPara
import com.example.core.uistatus.UiStatus
import com.example.data.constantes.IFoodConstantes
import com.example.domain.models.Loja
import com.example.domain.models.Produto
import com.example.domain.models.UploadStorage
import com.example.domain.models.Usuario
import com.example.loja.R
import com.example.loja.databinding.ActivityAdicionarProdutoBinding
import com.example.loja.presentation.viewmodel.ProdutoViewModel
import com.google.firebase.auth.FirebaseAuth
import com.jamiltondamasceno.core.adicionarMascaraMoeda
import com.jamiltondamasceno.core.formatarComoMoeda
import com.jamiltondamasceno.core.moedaToDouble
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class AdicionarProdutoActivity : AppCompatActivity() {
    private val produtoViewModel by viewModels<ProdutoViewModel>()
    private var idProduto =""
    lateinit var  idUsuario :String

    private val binding by lazy {
        ActivityAdicionarProdutoBinding.inflate( layoutInflater )
    }
    private val alertaCarregamento by lazy {
        AlertaMensagem(this)
    }

    private val selecionarImagemPerfil = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            binding.imageProdutoCapa.setImageURI(uri)
            binding.imageProdutoCapa.setImageURI(uri)
        } else {
            exibirMensagem("Nenhuma imagem selecionada")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
         idUsuario =  FirebaseAuth.getInstance().currentUser?.uid  ?: ""
         inicializar()
    }
    private fun inicializar() {

        recuperarDadosDoPproduto()
        InicializarMascaraModeda()
        inicializarEventosClique()
        inicializarObservers()
        solicitarPermissoes()
    }



    private fun inicializarObservers() {
       produtoViewModel.carregando.observe(this){carregando ->
           if (carregando) {
               alertaCarregamento.call("removendo produto")
           }else{
               alertaCarregamento.hide()
           }

       }
    }

    private fun recuperarDadosDoPproduto() {
        val bundle = intent.extras
        idProduto = bundle?.getString("idProduto") ?: ""
        if (idProduto.isNotEmpty()){

           produtoViewModel.recuperarProdutoPeloid( idUsuario,idProduto){uiStatus ->
               when(uiStatus){
                   is UiStatus.Erro -> {exibirMensagem(uiStatus.mensagem)}
                   is UiStatus.Sucesso -> {
                    val produto = uiStatus.dados
                       exibirDadosProduto(produto)
                   }
               }
           }

        }
    }

    private fun exibirDadosProduto(produto: Produto) {
        if (produto.urlImagem.isNotEmpty()) {
            Glide.with(this).load(produto.urlImagem)
                .into(binding.imageProdutoCapa)
                .onLoadStarted(getDrawable(
                R.drawable.nao_disponivel_perfil))
        }

        if (produto.nome.isNotEmpty()) {
            binding.edtProdutoNome.setText(produto.nome)
        }

        if (produto.preco.toString().isNotEmpty()) {
            binding.edtProdutoNome.setText(produto.preco.formatarComoMoeda())
        }
        if (produto.precoDestaque.toString().isNotEmpty()) {
            binding.edtProdutoNome.setText(produto.precoDestaque.formatarComoMoeda())
        }

        if (produto.produtoEmDestaque) {
            binding.switchProdutoDestaque.isChecked = true
            binding.tlPrecoProdutoDestaque.isVisible =true

            binding.edtPrecoProdutoDestaque.setText(produto.precoDestaque.formatarComoMoeda())
        }else{
            binding.switchProdutoDestaque.isChecked = false
            binding.tlPrecoProdutoDestaque.isVisible =false

            binding.edtPrecoProdutoDestaque.setText("")
        }


    }

    private fun InicializarMascaraModeda() {
        binding.edtPrecoProduto.adicionarMascaraMoeda()
        binding.edtPrecoProdutoDestaque.adicionarMascaraMoeda()
    }


    private fun solicitarPermissoes() {
        val listaPermissões = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            listaPermissões.add(android.Manifest.permission.READ_MEDIA_IMAGES)
            listaPermissões.add(android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listaPermissões.add(android.Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            listaPermissões.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        PermissionX.init(this).permissions(
            listaPermissões
        ).onExplainRequestReason { scope, listaNegada ->


            scope.showRequestReasonDialog(
                listaNegada,
                message = "Necessario acesso a galeria de imagens para selecionar foto de capa e perfil",
                positiveText = "ok",
                negativeText = "Não"
            )
        }.onForwardToSettings { scope, listaNegadas ->
            scope.showForwardToSettingsDialog(
                listaNegadas,
                message = "Necessesário dar permissião manualmente para seleciona image de capa e perfil",
                positiveText = "Abrir configurações",
                negativeText = "Não"
            )

        }
            .request { todasPermitidads, lisPermitidas, listaNegadas ->
                if (!todasPermitidads) exibirMensagem("Necessario permissões para acessar galeria de imagens")

            }

    }
    private fun uploadImagemProduto(uri: Uri) {

        produtoViewModel.uploadImagem(
            UploadStorage(
                nome = UUID.randomUUID().toString(),
                uriImage = uri,
                IFoodConstantes.STORAGE_LOJA
            ),idProduto
        ) { uiStatus ->
            when (uiStatus) {
                is UiStatus.Sucesso -> {
                    exibirMensagem("Upload feito com sucesso")
                    idProduto = uiStatus.dados
                }

                is UiStatus.Erro -> {

                    exibirMensagem(uiStatus.mensagem)
                }
            }
        }

    }


    private fun inicializarEventosClique() {
        with(binding){
            btnVoltarProduto.setOnClickListener {
                //navegarPara(CardapioActivity::class.java)
            }

            binding.btnSelecionarImagemProduto.setOnClickListener {
              selecionarImagemPerfil.launch(
                  PickVisualMediaRequest(
                      ActivityResultContracts.PickVisualMedia.ImageOnly
                  )
              )
            }
            binding.btnSalvarProduto.setOnClickListener {
                it.esconderTeclado()
                edtPrecoProdutoDestaque.clearFocus()
                edtPrecoProduto.clearFocus()
                edtDescricaoProduto.clearFocus()
                edtProdutoNome.clearFocus()

                cadastrarProduto()
            }
            binding.switchProdutoDestaque.setOnClickListener {
                if (switchProdutoDestaque.isChecked) {
                     binding.tlPrecoProdutoDestaque.isVisible = true
                     binding.edtPrecoProdutoDestaque.setText("")
                }
                else {
                    binding.tlPrecoProdutoDestaque.isVisible = false
                    binding.edtPrecoProdutoDestaque.setText("")
                }

            }
        }
    }



    private fun cadastrarProduto() {

        with(binding) {

            val nome = edtProdutoNome.text.toString()
            val precoProduto = edtPrecoProduto.moedaToDouble()
            val precoProdutoDestaque = edtPrecoProdutoDestaque.moedaToDouble()
            val descricaoProduto = edtDescricaoProduto.text.toString()
            val produtoEmDestaque = switchProdutoDestaque.isChecked

            val produto = Produto(
                nome = nome,
                id = idProduto,
                descricao = descricaoProduto,
                preco = precoProduto,
                precoDestaque = precoProdutoDestaque,
                produtoEmDestaque = produtoEmDestaque,
            )

            produtoViewModel.salvar(produto) { uiStatus ->
                when (uiStatus) {
                    is UiStatus.Sucesso -> {
                        exibirMensagem("Produto Atualizado salva com sucesso")
                        idProduto = uiStatus.dados

                        /*startActivity(
                            Intent(applicationContext, HomeActivity::class.java)
                        )*/
                    }

                    is UiStatus.Erro -> {

                        exibirMensagem(uiStatus.mensagem)
                    }
                }
            }

        }

    }

}