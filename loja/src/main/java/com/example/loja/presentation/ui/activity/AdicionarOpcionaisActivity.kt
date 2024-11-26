package com.example.loja.presentation.ui.activity

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core.exibirMensagem
import com.example.core.mensagens.AlertaMensagem
import com.example.core.uistatus.UiStatus
import com.example.data.constantes.IFoodConstantes
import com.example.domain.models.Opcional
import com.example.domain.models.Produto
import com.example.domain.models.UploadStorage
import com.example.loja.R
import com.example.loja.databinding.ActivityAdicionarOpcionaisBinding
import com.example.loja.presentation.ui.adapter.OpcionalAdapter
import com.example.loja.presentation.viewmodel.OpcionalViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.jamiltondamasceno.core.adicionarMascaraMoeda
import com.jamiltondamasceno.core.formatarComoMoeda
import com.jamiltondamasceno.core.moedaToDouble
import com.permissionx.guolindev.PermissionX
import java.util.UUID

class AdicionarOpcionaisActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAdicionarOpcionaisBinding.inflate( layoutInflater )
    }
    private val alertaCarregamento by lazy {
        AlertaMensagem(this)
    }

    private lateinit var opcionalAdapter: OpcionalAdapter
    private val opcionalViewModel by viewModels<OpcionalViewModel>()

    private lateinit var idProduto: String
    private var uriOpcional : Uri? = null

    private val selecionarImagemOpcional = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            binding.imageOpcional.setImageURI(uri)
            uriOpcional = uri
        } else {
            exibirMensagem("Nenhuma imagem selecionada")
        }
    }
    private lateinit var idUsuario :String

    private val listaOpcionais = emptyList<Opcional>()
       /* listOf(
        Opcional(
            nome =  "Hambúrguer extra",
            descricao = "Mussum Ipsum, cacilds vidis litro abertis. Paisis",
            preco = "R$ 5,90",
            "https://static.ifood-static.com.br/image/upload/t_medium/pratos/0a0d6cdd-f04f-4909-aae6-fabb497f08b7/202407300424_tbo1kguj1yf.png"
        ),
        Opcional(
            "Queijo Extra",
            "Mussum Ipsum, cacilds vidis litro abertis. Paisis",
            "R$ 2,90",
            "https://static.ifood-static.com.br/image/upload/t_medium/pratos/0a0d6cdd-f04f-4909-aae6-fabb497f08b7/202407300424_tbo1kguj1yf.png"
        ),
        Opcional(
            "Molho Extra",
            "Mussum Ipsum, cacilds vidis litro abertis. Paisis",
            "R$ 2,90",
            "https://static.ifood-static.com.br/image/upload/t_medium/pratos/0a0d6cdd-f04f-4909-aae6-fabb497f08b7/202407300424_tbo1kguj1yf.png"
        ),
        Opcional(
            "Batata Frita",
            "Mussum Ipsum, cacilds vidis litro abertis. Paisis",
            "R$ 7,90",
            "https://static.ifood-static.com.br/image/upload/t_medium/pratos/0a0d6cdd-f04f-4909-aae6-fabb497f08b7/202407300424_tbo1kguj1yf.png"
        ),
    )*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
         idUsuario = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        inicializar()

    }

    override fun onStart() {
        recuperarOpcionaias()
        super.onStart()
    }
    private fun recuperarOpcionaias() {
        if (idProduto.isEmpty()) return
         opcionalViewModel.listar( idLoja =idUsuario ,idProduto = idProduto) {uiStatus ->
            when(uiStatus){
                is UiStatus.Erro -> exibirMensagem(uiStatus.mensagem)
                is UiStatus.Sucesso -> {
                   val listaOpcionais  = uiStatus.dados
                    opcionalAdapter.adicionarLista(listaOpcionais)
                }
            }

        }
    }

    private fun inicializarOpcionais() {
        opcionalAdapter = OpcionalAdapter { opcional ->
          confimarExclusao(opcional)
        }
        opcionalAdapter.adicionarLista( listaOpcionais )
        binding.rvOpcionais.adapter = opcionalAdapter
        binding.rvOpcionais.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL, false
        )

    }

    private fun confimarExclusao(opcional: Opcional) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Deseja realmente excluir o opcional")
            .setMessage("[${opcional.nome}] ${opcional.preco.formatarComoMoeda()}")
            .setNegativeButton("Cancelar"){dialog ,_ ->
                dialog.dismiss()
            }
            .setPositiveButton("Confirmo exclusão"){dialog ,_ ->
                removerOpcional(opcional.idProduto,opcional.id)
                dialog.dismiss()
            }
            .show().create()
    }

    private fun removerOpcional(idProduto: String, id: String) {
        opcionalViewModel.remover(idProduto,id){ uiStatus->
            when(uiStatus){
                is UiStatus.Erro ->exibirMensagem(uiStatus.mensagem)
                is UiStatus.Sucesso -> {
                    recuperarOpcionaias()
                    exibirMensagem("Opcional  removido")
                }
            }
        }
    }


    private fun incializarEventoClique() {
        with(binding){
            btnSelecionarImagemOpcional.setOnClickListener {
                selecionarImagemOpcional.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
           edtNomeOpcional.clearFocus()
            edtPrecoOpcional.clearFocus()
            edtDescricaoOpcional.clearFocus()

            val nome = edtNomeOpcional.text.toString()
            val descricao = edtDescricaoOpcional.text.toString()
            val preco = edtPrecoOpcional.moedaToDouble()


            val opcional = Opcional(
                preco = preco,
                nome = nome,
                descricao = descricao,
                idProduto = idProduto
            )
            val validarDadosOpcional = validarDadosOpcional(opcional)
             if (validarDadosOpcional)cadastrarOpcional(opcional)
            else exibirMensagem("Preencha todos os campos e selecione uma imagem")
        }

    }

    private fun cadastrarOpcional(opcional: Opcional) {
     uriOpcional?.let {uri->
          opcionalViewModel.salvar(
              opcional = opcional,
              uploadStorage = UploadStorage(
                  nome = UUID.randomUUID().toString(),
                  uriImage = uri,
                  local = IFoodConstantes.STORAGE_OPCIONAIS
              ),
              uiStatus = { uiStatus->
                 when(uiStatus){
                     is UiStatus.Erro -> {exibirMensagem(uiStatus.mensagem)}
                     is UiStatus.Sucesso -> {
                         limparDadosPreenchidos()
                         recuperarOpcionaias()
                         exibirMensagem("Opcional salvo com sucesso")
                     }
                 }
              }
              )
     }
    }



    private fun limparDadosPreenchidos() {
        uriOpcional = null
        with(binding){
            edtNomeOpcional.setText("")
             edtDescricaoOpcional.setText("")
            edtPrecoOpcional.setText("")
            imageOpcional.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.nao_disponivel))
        }
    }

    private fun validarDadosOpcional(opcional: Opcional): Boolean {
       if (opcional.nome.isEmpty()) return false
       if (opcional.descricao.isEmpty()) return false
       if (opcional.preco == 0.0) return false
       if (uriOpcional == null) return false

        return true
    }

    private fun inicializar() {
        binding.edtPrecoOpcional.adicionarMascaraMoeda()
        inicializarDadosProduto()
        inicializarToolbar()
        inicializarOpcionais()
        incializarEventoClique()
        solicitarPermissoes()
    }

    private fun inicializarDadosProduto() {
        val bundle = intent.extras
        val produto  = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle?.getParcelable("produto", Produto::class.java)
        } else {
            bundle?.getParcelable("produto")
        }

        produto?.let { produto ->
            binding.txvDadosOpcional.setText("${produto.nome} - ${produto.preco}")
        }
    }
    private fun inicializarToolbar() {
        /*val toolbar = binding.includeTbOpcional.tbPrincipal
        setSupportActionBar( toolbar )

        supportActionBar?.apply {
            title = "Adicionar opcionais"
            setDisplayHomeAsUpEnabled(true)
        }*/

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
                message = "Necessario acesso a galeria de imagens para selecionar foto de opcional",
                positiveText = "ok",
                negativeText = "Não"
            )
        }.onForwardToSettings { scope, listaNegadas ->
            scope.showForwardToSettingsDialog(
                listaNegadas,
                message = "Necessesário dar permissião manualmente para seleciona image opcional",
                positiveText = "Abrir configurações",
                negativeText = "Não"
            )

        }
            .request { todasPermitidads, lisPermitidas, listaNegadas ->
                if (!todasPermitidads) exibirMensagem("Necessario permissões para acessar galeria de imagens")

            }

    }
}