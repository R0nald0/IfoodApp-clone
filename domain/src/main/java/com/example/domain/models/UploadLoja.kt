package com.example.domain.models

import android.net.Uri

enum class TipoImagemUpload{
    IMAGEM_PERFIL, IMAGEM_CAPA
}


data class UploadLoja (
    val nome :String,
    val uriImage:Uri,
    val tipoImagem: TipoImagemUpload

)