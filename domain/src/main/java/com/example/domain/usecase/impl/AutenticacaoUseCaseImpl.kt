package com.example.domain.usecase.impl

import com.example.core.uistatus.UiStatus
import com.example.domain.models.Usuario
import com.example.domain.repository.IAutenticacaoRepository
import com.example.domain.usecase.IAutenticacaoUseCase
import com.example.domain.models.ResultadoAutenticao
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import javax.inject.Inject

class AutenticacaoUseCaseImpl @Inject constructor(
    private val autenticacaoRepository: IAutenticacaoRepository
) : IAutenticacaoUseCase {

    override fun validarCadastroUsuario(usuario: Usuario): ResultadoAutenticao {
        val resultadoAutenticao  = ResultadoAutenticao()

        if (!usuario.nome.nonEmpty())resultadoAutenticao.nomeInvalid =true

        if (!usuario.email.validEmail())resultadoAutenticao.emailInvalid =true

        val senha = usuario.senha.validator()
            .nonEmpty().minLength(5).check()

        if (!senha)resultadoAutenticao.senhaInvalid =true

        if (!usuario.email.nonEmpty())resultadoAutenticao.emailInvalid =true

        if (!usuario.telefone.nonEmpty())resultadoAutenticao.telefoneInvalid =true
        return  resultadoAutenticao
    }

    override fun validarLoginUsuario(usuario: Usuario): ResultadoAutenticao {
        val resultadoAutenticao  = ResultadoAutenticao()

        val senha = usuario.senha.validator().nonEmpty().minLength(5).check()
        val email = usuario.email.validEmail()

        if (!senha)resultadoAutenticao.senhaInvalid =true

        if (!email)resultadoAutenticao.emailInvalid =true
        if (!usuario.email.nonEmpty())resultadoAutenticao.emailInvalid =true


        return  resultadoAutenticao
    }

    override suspend fun logarUsuario(usuario: Usuario,status:(UiStatus<Boolean>)->Unit)= autenticacaoRepository.logarUsuario(
        usuario = usuario,
        status = status
    )


    override suspend fun cadastrarUsuario(usuario: Usuario , status:(UiStatus<Boolean>)->Unit) = autenticacaoRepository.cadastrarUsuario(
        usuario = usuario,
        status = status
    )

    override  fun isLogged() = autenticacaoRepository.isLogged()
    override fun logout() = autenticacaoRepository.logout()

}