package com.example.ifood_clone2.presentation.ui.fragments

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.core.exibirMensagem
import com.example.core.mensagens.AlertaMensagem
import com.example.core.navegarPara
import com.example.core.uistatus.UiStatus
import com.example.data.constantes.IFoodConstantes
import com.example.domain.models.UploadStorage
import com.example.ifood_clone2.databinding.FragmentPerfilBinding
import com.example.ifood_clone2.presentation.ui.LoginActivity
import com.example.ifood_clone2.presentation.viewmodel.AutenticacaoViewModel
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class PerfilFragment : Fragment() {
     lateinit var binding  : FragmentPerfilBinding
     private val autenticacaoViewModel by viewModels<AutenticacaoViewModel>()
     private var idUsuarioLogado :String? = null

    private val alertMessage by lazy {
        AlertaMensagem(this.requireActivity())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val selecionarImagemPerfil = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            binding.imgPerfil.setImageURI(uri)
        } else {
           requireActivity().exibirMensagem("Nenhuma imagem selecionada")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPerfilBinding.inflate(inflater,container,false)
        InicializarObservers()
        InicializareventoDeclick()
        solicitarPermissoes()
        return binding.root
    }

    private fun InicializarObservers() {
        autenticacaoViewModel.carregando.observe(viewLifecycleOwner){carregando ->
            if (carregando){
                alertMessage.call("Carregando..")
            }else{
                alertMessage.hide()
            }
        }
    }

    private fun InicializareventoDeclick() {
        binding.btnDeslogar.setOnClickListener {
            autenticacaoViewModel.logout()
            requireActivity().navegarPara(LoginActivity::class.java)
        }
        binding.btnAlterarFoto.setOnClickListener {  }
        binding.btnSalvarAlteracao.setOnClickListener {
                selecionarImagemPerfil.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exibirDadosDoPerfil()
        recuperarIdUsuarioLogado()

    }

    private fun recuperarIdUsuarioLogado() {
        idUsuarioLogado = autenticacaoViewModel.isLogged()
    }

    private fun exibirDadosDoPerfil() {

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
                message = "Necessario acesso a galeria de imagens para selecionar foto de perfil",
                positiveText = "ok",
                negativeText = "Não"
            )
        }.onForwardToSettings { scope, listaNegadas ->
            scope.showForwardToSettingsDialog(
                listaNegadas,
                message = "Necessesário dar permissião manualmente para seleciona image de perfil",
                positiveText = "Abrir configurações",
                negativeText = "Não"
            )

        }
            .request { todasPermitidads, lisPermitidas, listaNegadas ->
                if (!todasPermitidads) requireActivity().exibirMensagem("Necessario permissões para acessar galeria de imagens")

            }

    }
    private fun uploadImagemPerfil(uri: Uri) {
        if (idUsuarioLogado == null) {
            requireActivity().navegarPara(LoginActivity::class.java)
        }

        autenticacaoViewModel.uploadImagem(
            UploadStorage(
                nome = UUID.randomUUID().toString(),
                uriImage = uri,
                IFoodConstantes.STORAGE_USUARIO
            ),idUsuarioLogado!!
        ) { uiStatus ->
            when (uiStatus) {
                is UiStatus.Sucesso -> {
                   requireActivity().exibirMensagem("Upload feito com sucesso")
                 //   idProduto = uiStatus.dados
                }

                is UiStatus.Erro -> {

                   requireActivity().exibirMensagem(uiStatus.mensagem)
                }
            }
        }

    }

}