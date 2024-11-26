package com.example.data.repository

import com.example.core.uistatus.UiStatus
import com.example.data.constantes.IFoodConstantes
import com.example.domain.models.Categoria
import com.example.domain.repository.ICategoriaRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CategoriaRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ICategoriaRepository {
    override suspend fun recuperarCategorias(uiStatus: (UiStatus<List<Categoria>>) -> Unit) {
        try {
            val categoriasRef = firestore.collection(IFoodConstantes.FIRESTORE_CATEGORIA)
            val querySnapshot = categoriasRef.get().await()
            if (querySnapshot.documents.isEmpty()) {
                uiStatus.invoke(
                    UiStatus.Erro("Não existem categorias")
                )
                return
            }

            val listaDeCategorias = querySnapshot.documents.mapNotNull { documentSnapshot ->
                documentSnapshot.toObject(Categoria::class.java)
            }

            if (listaDeCategorias.isEmpty()) {
                uiStatus.invoke(
                    UiStatus.Erro("Não foi possivel converter as categorias")
                )
                return
            }


            uiStatus.invoke(
                UiStatus.Sucesso(listaDeCategorias)
            )


        } catch (e: Exception) {
            uiStatus.invoke(
                UiStatus.Erro("Não foi possivel converter as categorias")
            )
        }
    }
}