package com.example.loja.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.core.esconderTeclado
import com.example.core.exibirMensagem
import com.example.core.mensagens.AlertaMensagem
import com.example.core.navegarPara
import com.example.core.uistatus.UiStatus
import com.example.domain.models.Usuario
import com.example.loja.databinding.ActivityLoginBinding
import com.example.loja.presentation.viewmodel.AutenticacaoViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val alertMessage by lazy {
        AlertaMensagem(this)
    }
    private val autenticacaoViewModel by viewModels<AutenticacaoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inicializar()
    }

    override fun onStart() {
        super.onStart()
        val isLogged = autenticacaoViewModel.isLogged()
        if (isLogged !=null ){
            navegarPara(MainActivity::class.java)
        }
        false
    }
    private fun inicializar() {
        inicializarEventoClick()
        inicializarObservables()
    }

    private fun inicializarObservables() {
        autenticacaoViewModel.resultadoValidacao.observe(this){
            with(binding){
                edtEmail.error =
                    if (it.emailInvalid)"preencha o Email" else null
                edtLoginSenha.error =
                    if (it.senhaInvalid)"preencha o Senha" else null
            }
        }

        autenticacaoViewModel.carregando.observe(this){carregando ->
            if (carregando){
                alertMessage.call("Validando Login..")
            }else{
                alertMessage.hide()
            }
        }
    }

    fun inicializarEventoClick(){
        with(binding){
            btnCadastre.setOnClickListener {
                startActivity(Intent(this@LoginActivity, CadastroActivity::class.java))
            }
            btnLogin.setOnClickListener { view->
                view.esconderTeclado()
                edtEmail.clearFocus()
                edtLoginSenha.clearFocus()

                val usuario = Usuario(
                    email = edtEmail.text.toString(),
                    senha = edtLoginSenha.text.toString()
                )
                autenticacaoViewModel.logarUsuario(usuario){ uiStatus ->
                    when (uiStatus){
                        is UiStatus.Erro -> exibirMensagem(uiStatus.mensagem)
                        is UiStatus.Sucesso -> {
                            if (uiStatus.dados) navegarPara( MainActivity::class.java)
                        }
                    }
                }

            }
        }
    }

}