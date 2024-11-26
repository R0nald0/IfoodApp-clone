package com.example.domain.usecase

import com.example.core.uistatus.UiStatus
import com.example.domain.models.ResultadoAutenticao
import com.example.domain.models.Usuario


interface IAutenticacaoUseCase {
    fun validarCadastroUsuario(usuario: Usuario): ResultadoAutenticao
    fun validarLoginUsuario(usuario: Usuario): ResultadoAutenticao
    suspend fun logarUsuario(usuario: Usuario,status:(UiStatus<Boolean>)->Unit)
    suspend fun cadastrarUsuario(usuario: Usuario,status:(UiStatus<Boolean>)->Unit)
     fun isLogged():String?
     fun logout()
}