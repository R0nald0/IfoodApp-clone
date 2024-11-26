package com.example.ifood_clone2.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.core.esconderTeclado
import com.example.core.exibirMensagem
import com.example.core.mensagens.AlertaMensagem
import com.example.core.navegarPara
import com.example.core.uistatus.UiStatus

import com.example.domain.models.Usuario
import com.example.ifood_clone2.R
import com.example.ifood_clone2.databinding.ActivityCadastroBinding
import com.example.ifood_clone2.presentation.viewmodel.AutenticacaoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CadastroActivity : AppCompatActivity() {

    private   val binding by lazy{
         ActivityCadastroBinding.inflate(layoutInflater)
    }
    private val autenticacaoViewModel by viewModels<AutenticacaoViewModel>()
    private val alertMessage by lazy {
        AlertaMensagem(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        inicializar()
    }

    private fun inicializarToolbar(){
        binding.include2.tbPrincipal.apply {
            setSupportActionBar(this)
            supportActionBar?.apply {
                title="Cadastrar Usuario"
                setDisplayHomeAsUpEnabled(true)
                setNavigationIcon(R.drawable.back_ios_new_24)
            }
        }
    }
    private fun inicializar() {
        inicializarToolbar()
        inicializarListeners()
        inicializarObservables()
    }


    private fun inicializarObservables() {

        autenticacaoViewModel.resultadoValidacao.observe(this){
            with(binding){
                editCadastroNome.error =
                    if (it.nomeInvalid)"preencha o nome" else null
                editCadastroEmail.error =
                    if (it.emailInvalid)"preencha o email" else null
                editCadastroSenha.error=
                    if (it.senhaInvalid)"preencha a senha" else null
                editCadastroTelefone.error =
                    if (it.telefoneInvalid) "preencha o telefone" else null
            }
        }

        autenticacaoViewModel.carregando.observe(this){ carregando ->
            if (carregando){
                alertMessage.call("Cadastrando Usuario...")
            }else{
                alertMessage.hide()
            }
        }

    }


    private fun inicializarListeners() {
        with(binding){

            editCadastroTelefone.clearFocus()
            editCadastroNome.clearFocus()
            editCadastroSenha.clearFocus()
            editCadastroEmail.clearFocus()

            btnCadastrar.setOnClickListener {
                it.esconderTeclado()

               autenticacaoViewModel.cadastroUsuario(
                   Usuario(
                    nome= editCadastroNome.text.toString(),
                    email = editCadastroEmail.text.toString() ,
                    senha =editCadastroSenha.text.toString(),
                    telefone = editCadastroTelefone.text.toString()
                )
               ) { uiStatus ->
                   when(uiStatus) {
                       is UiStatus.Sucesso ->{
                            if (uiStatus.dados) navegarPara(HomeActivity::class.java)
                       }

                       is UiStatus.Erro -> {
                           exibirMensagem(uiStatus.mensagem)
                       }
                   }
               }
            }
        }
    }
}