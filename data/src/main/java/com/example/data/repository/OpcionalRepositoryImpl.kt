package com.example.data.repository

import com.example.core.uistatus.UiStatus
import com.example.data.constantes.IFoodConstantes
import com.example.domain.models.Opcional
import com.example.domain.repository.IOpcionalRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class OpcionalRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) :IOpcionalRepository  {
    override suspend fun salvar(opcional: Opcional, status: (UiStatus<String>) -> Unit) {
        val idLoja = auth.currentUser?.uid

        try {
            if (idLoja != null){
                val opcionalRef = firestore
                    .collection(IFoodConstantes.FIRESTORE_PRODUTO)
                    .document(idLoja)
                    .collection(IFoodConstantes.FIRESTORE_ITEMS)
                    .document(opcional.idProduto)
                    .collection(IFoodConstantes.FIRESTORE_OPCIONAIS)
                    .document()

                val idOpcional = opcionalRef.id
                opcional.copy(id = idOpcional)
                opcionalRef.set(opcional).await()

                status.invoke(UiStatus.Sucesso(idOpcional))

            }else{
                status.invoke(UiStatus.Erro(mensagem = "Usuario não está logado"))
            }
        }catch (e :Exception){
            status.invoke(UiStatus.Erro(mensagem = "Erro ao salvar opcional"))
        }
    }

    override suspend fun remover(idProduto:String,idOpcional: String, status: (UiStatus<Boolean>) -> Unit) {
        try {

            val idLoja = auth.currentUser?.uid ?: return status.invoke((UiStatus.Erro("usuario não esta logado")))
            val opcionalRef = firestore
                .collection(IFoodConstantes.FIRESTORE_PRODUTO)
                .document(idLoja)
                .collection(IFoodConstantes.FIRESTORE_ITEMS)
                .document(idProduto)
                .collection(IFoodConstantes.FIRESTORE_OPCIONAIS)
                .document(idOpcional)

            opcionalRef.delete().await()

            status.invoke(UiStatus.Sucesso(true))

        }catch (erro :Exception){
            status.invoke(UiStatus.Erro("Erro ao remover o opcional"))
        }
    }

    override suspend fun listar(
        idLoja: String,
        idProduto: String,
        status: (UiStatus<List<Opcional>>) -> Unit
    ) {
        try {
          /*  val idUsuario = auth.currentUser?.uid ?: return status.invoke(
                UiStatus.Erro("usuario não esta logado")
            )*/

            val  opcionalRef = firestore
                .collection(IFoodConstantes.FIRESTORE_PRODUTO)
                .document(idLoja)
                .collection(IFoodConstantes.FIRESTORE_ITEMS)
                .document(idProduto)
                .collection(IFoodConstantes.FIRESTORE_OPCIONAIS)

            val querySnapshot = opcionalRef.get().await()

            if (querySnapshot.documents.isEmpty()) {
                status.invoke(
                    UiStatus.Sucesso(emptyList())
                )
                return
            }

            val listaProdutos = querySnapshot.documents.mapNotNull { documentSnapshot ->
                documentSnapshot.toObject(Opcional::class.java)
            }


            status.invoke(
                UiStatus.Sucesso(listaProdutos)
            )

        } catch (e: Exception) {
            status.invoke(
                UiStatus.Erro("Não foi possivel converter as opcionais")
            )
        }
    }


}