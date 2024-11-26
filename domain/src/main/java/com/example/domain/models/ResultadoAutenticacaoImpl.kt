package com.example.domain.models

data class ResultadoAutenticao(
    var nomeInvalid: Boolean =false,
    var emailInvalid: Boolean =false,
    var senhaInvalid: Boolean =false,
    var telefoneInvalid: Boolean =false
)     {
    val sucessoCadastro : Boolean
        get() = !(nomeInvalid || emailInvalid || senhaInvalid || telefoneInvalid)

    val sucessoLogin : Boolean
        get() = !( emailInvalid || senhaInvalid )
}