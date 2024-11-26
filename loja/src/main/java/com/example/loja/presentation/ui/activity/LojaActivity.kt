package com.example.loja.presentation.ui.activity

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.core.exibirMensagem
import com.example.core.mensagens.AlertaMensagem
import com.example.core.navegarPara
import com.example.core.uistatus.UiStatus
import com.example.domain.models.Categoria
import com.example.domain.models.Loja
import com.example.domain.models.TipoImagemUpload
import com.example.domain.models.UploadLoja
import com.example.loja.R
import com.example.loja.databinding.ActivityLojaBinding
import com.example.loja.presentation.viewmodel.LojaViewModel
import com.google.firebase.auth.FirebaseAuth
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LojaActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLojaBinding.inflate(layoutInflater)
    }
    private val alertMessage by lazy {
        AlertaMensagem(this)
    }
    lateinit var categorais  : List<Categoria>

    val selecionarImagemCapa = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            binding.imgCapa.setImageURI(uri)
            uploadImagemCapa(uri)
        } else {
            exibirMensagem("Nenhuma imagem selecionada")
        }

    }

    private val selecionarImagemPerfil = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            binding.imgPerfilLoja.setImageURI(uri)
            uploadImagemPerfil(uri)
        } else {
            exibirMensagem("Nenhuma imagem selecionada")
        }
    }

    private lateinit var categoraiasAdapter: ArrayAdapter<String>


    private val lojaViewModel by viewModels<LojaViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        solicitarPermissoes()
        inicializarAdapterCategorias()
        inicializarEventoDeClick()
        inicializarObservaveis()
    }


    override fun onStart() {
        super.onStart()
        carregarDados()
    }

    private fun carregarDados() {
        val idUsuario = FirebaseAuth.getInstance().currentUser?.uid ?:""
        lojaViewModel.recuperarDadosLoja(idUsuario) { uiStatus ->
            when (uiStatus) {
                is UiStatus.Sucesso -> {
                    val loja = uiStatus.dados
                    exibirDadosLoja(loja)
                    carregarDadosCateoria(loja.idCategoria)
                }

                is UiStatus.Erro -> {
                    exibirMensagem(uiStatus.mensagem)
                }
            }
        }
    }

    private fun carregarDadosCateoria(catedgoriaId :String) {
        lojaViewModel.recuperarCategorias {uiStatus ->
            when(uiStatus){
                is UiStatus.Erro -> exibirMensagem(uiStatus.mensagem)
                is UiStatus.Sucesso -> {
                     categorais  = uiStatus.dados
                    val categoriasSpinner = mutableListOf("Selecione uma categoria")

                    categoriasSpinner.addAll(
                        categorais.map { categoria -> categoria.nome }
                    )

                     categoraiasAdapter.clear()
                    categoraiasAdapter.addAll(categoriasSpinner)

                     val selctedItem =categorais.indexOfFirst { it.id == catedgoriaId } + 1
                     binding.spinner.setSelection(selctedItem)
                }
            }

        }
    }

    private fun exibirDadosLoja(loja: Loja) {
        if (loja.urlPerfil.isNotEmpty()) {
            Glide.with(this).load(loja.urlPerfil).into(binding.imgPerfilLoja).onLoadStarted(getDrawable(R.drawable.nao_disponivel_perfil))
        }
        if (loja.urlCapa.isNotEmpty()) {
            Glide.with(this).load(loja.urlCapa).into(binding.imgCapa).onLoadStarted(ContextCompat.getDrawable(this,R.drawable.nao_disponivel))
        }
        if (loja.nome.isNotEmpty()) {
            binding.edtNome.setText(loja.nome)
        }

        if (loja.cnpj.isNotEmpty()) {
            binding.edtCnpj.setText(loja.cnpj)
        }
        if (loja.telefone.isNotEmpty()) {
            binding.edtTelefone.setText(loja.telefone)
        }

        if (loja.sobreEmpresa.isNotEmpty()) {
            binding.edtSobre.setText(loja.telefone)
        }

        if (loja.razaoSocial.isNotEmpty()) {
            binding.edtRazaoSocial.setText(loja.telefone)
        }
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

    private fun inicializarObservaveis() {
        lojaViewModel.resultadoValidacao.observe(this) { successo ->
            if (!successo) {
                exibirMensagem("Preencha os dados corretamente")
            }

        }
        lojaViewModel.carregando.observe(this) { carregando ->
            if (carregando) {
                alertMessage.call("Fazendo upload de imagem...")
            } else {
                alertMessage.hide()
            }
        }
    }

    private fun inicializarEventoDeClick() {
        with(binding) {
            btnDadosLojaVolar.setOnClickListener {
                navegarPara(MainActivity::class.java)
                finish()
            }
            binding.btnImgCapa.setOnClickListener {
                selecionarImagemCapa.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }

            binding.btnImgPerfil.setOnClickListener {
                selecionarImagemPerfil.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }

            btnCriarDadosLoja.setOnClickListener {
                val nome = edtNome.text.toString()
                val cnpj = edtCnpj.text.toString()
                val telefone = edtTelefone.text.toString()
                val sobre = edtSobre.text.toString()
                val razaoSocial = edtRazaoSocial.text.toString()

                edtNome.clearFocus()
                edtCnpj.clearFocus()
                edtTelefone.clearFocus()
                edtSobre.clearFocus()
                edtRazaoSocial.clearFocus()

                cadastrarLoja()

            }
        }
    }

    private fun uploadImagemCapa(uri: Uri) {

        lojaViewModel.uploadImagem(
            UploadLoja(
                "imagem_capa",
                uri,
                TipoImagemUpload.IMAGEM_CAPA
            )
        ) { uiStatus ->
            when (uiStatus) {
                is UiStatus.Sucesso -> {

                    exibirMensagem("Upload feito com sucesso")
                }

                is UiStatus.Erro -> {

                    exibirMensagem(uiStatus.mensagem)
                }
            }
        }

    }

    private fun uploadImagemPerfil(uri: Uri) {

        lojaViewModel.uploadImagem(
            UploadLoja(
                "imagem_perfil",
                uri,
                TipoImagemUpload.IMAGEM_PERFIL
            )
        ) { uiStatus ->
            when (uiStatus) {

                is UiStatus.Sucesso -> {
                    exibirMensagem("Upload feito com sucesso")
                }

                is UiStatus.Erro -> {

                    exibirMensagem(uiStatus.mensagem)
                }
            }
        }

    }

    private fun cadastrarLoja() {

        with(binding) {

            val nome = edtNome.text.toString()
            val razaoSocial = edtRazaoSocial.text.toString()
            val cnpj = edtCnpj.text.toString()
            val sobreEmpresa = edtSobre.text.toString()
            val telefone = edtTelefone.text.toString()

            var categoria =""
            var cateogoriId = ""
            val selectedItemPosition = binding.spinner.selectedItemPosition
            if (selectedItemPosition > 0){
                val categoriaSeleiconada= categorais[selectedItemPosition - 1]
                  cateogoriId = categoriaSeleiconada.id ?: ""
                  categoria  = categoriaSeleiconada.nome
                 }

            val loja = Loja(
                idLoja = "",
                email = "",
                categoria = categoria,
                idCategoria = cateogoriId,
                nome = nome,
                razaoSocial = razaoSocial,
                cnpj = cnpj,
                sobreEmpresa = sobreEmpresa,
                telefone = telefone,
            )

            lojaViewModel.cadastrar(loja) { uiStatus ->
                when (uiStatus) {
                    is UiStatus.Sucesso -> {
                        exibirMensagem("Loja salva com sucesso")

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

    private fun inicializarAdapterCategorias() {
         categoraiasAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            mutableListOf()
        )

        binding.spinner.adapter = categoraiasAdapter
    }
}

