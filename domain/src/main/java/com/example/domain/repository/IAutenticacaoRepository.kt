package com.example.domain.repository

import com.example.core.uistatus.UiStatus
import com.example.domain.models.Usuario

interface IAutenticacaoRepository {
    suspend fun cadastrarUsuario(usuario: Usuario, status: (UiStatus<Boolean>)->Unit)
    suspend  fun logarUsuario(usuario: Usuario,status: (UiStatus<Boolean>)->Unit)
    fun isLogged():String?
    fun logout();
}
