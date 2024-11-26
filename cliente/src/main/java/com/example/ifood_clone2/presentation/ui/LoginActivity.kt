package com.example.ifood_clone2.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.core.esconderTeclado
import com.example.core.exibirMensagem
import com.example.core.mensagens.AlertaMensagem
import com.example.core.navegarPara
import com.example.core.uistatus.UiStatus
import com.example.domain.models.Usuario
import com.example.ifood_clone2.databinding.ActivityLoginBinding
import com.example.ifood_clone2.presentation.viewmodel.AutenticacaoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

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
        installSplashScreen().setKeepOnScreenCondition{
             val isLogged = autenticacaoViewModel.isLogged()
            if (isLogged != null){
                navegarPara(HomeActivity::class.java)
            }
            false
        }
        setContentView(binding.root)
        inicializar()


    }

    override fun onStart() {
        super.onStart()

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
                            if (uiStatus.dados) navegarPara( HomeActivity::class.java)
                        }
                    }
                }

            }
        }
    }


}