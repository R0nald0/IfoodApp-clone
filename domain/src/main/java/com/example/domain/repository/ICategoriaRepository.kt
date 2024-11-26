package com.example.domain.repository

import com.example.core.uistatus.UiStatus
import com.example.domain.models.Categoria

interface ICategoriaRepository {
    suspend fun recuperarCategorias(uiStatus : (UiStatus<List<Categoria>>) -> Unit)
}