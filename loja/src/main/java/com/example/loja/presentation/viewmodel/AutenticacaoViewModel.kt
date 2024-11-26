package com.example.loja.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.uistatus.UiStatus
import com.example.domain.models.Usuario
import com.example.domain.usecase.impl.AutenticacaoUseCaseImpl
import com.example.domain.models.ResultadoAutenticao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AutenticacaoViewModel @Inject constructor(
    private val autenticacaoUseCaseImpl: AutenticacaoUseCaseImpl
): ViewModel() {
    private val _carregando = MutableLiveData<Boolean>()
    val carregando : LiveData<Boolean>
        get() = _carregando

   private val _resultadoValidacao  = MutableLiveData<ResultadoAutenticao>()
    val resultadoValidacao  : LiveData<ResultadoAutenticao>
        get()= _resultadoValidacao


    fun  logarUsuario(usuario: Usuario,status:(UiStatus<Boolean>)->Unit){
        val resultadoAutenticao = autenticacaoUseCaseImpl.validarLoginUsuario(usuario)
        _resultadoValidacao.value = resultadoAutenticao

        if(resultadoAutenticao.sucessoLogin){
            viewModelScope.launch {
                _carregando.value =true
                 autenticacaoUseCaseImpl.logarUsuario(usuario, status = status)
                _carregando.value =false
            }
        }
    }
    fun cadastroUsuario(usuario: Usuario,status:(UiStatus<Boolean>)->Unit){

        val resultadoAutenticao = autenticacaoUseCaseImpl.validarCadastroUsuario(usuario)
        _resultadoValidacao.value = resultadoAutenticao

        if (resultadoAutenticao.sucessoCadastro){
            viewModelScope.launch() {
                _carregando.value =true
                 autenticacaoUseCaseImpl.cadastrarUsuario(usuario, status = status)
                _carregando.value =false
            }
        }
    }
    fun isLogged() =autenticacaoUseCaseImpl.isLogged()
}


