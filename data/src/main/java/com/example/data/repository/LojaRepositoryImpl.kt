package com.example.data.repository

import com.example.core.uistatus.UiStatus
import com.example.data.constantes.IFoodConstantes
import com.example.domain.models.Categoria
import com.example.domain.models.Loja
import com.example.domain.models.toMap
import com.example.domain.repository.ILojaRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LojaRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : ILojaRepository {

    override suspend fun atualizarDadoLoja(
        dados: Map<String, String>, status: (UiStatus<Boolean>) -> Unit
    ) {

        try {

            val idUsuario = auth.currentUser?.uid
            if( idUsuario != null ){

                val lojasRef = firestore
                    .collection( IFoodConstantes.FIRESTORE_LOJA )
                    .document( idUsuario )
                lojasRef.update( dados ).await()

                status.invoke(
                    UiStatus.Sucesso(true)
                )

            }
        }catch ( erroCadastrarLoja: Exception ){
            status.invoke(
                UiStatus.Erro("Erro ao salvar dados da loja")
            )
        }

    }

    override suspend fun listarLojas(uiStatus: (UiStatus<List<Loja>>) -> Unit){
        try {
            val lojasRef = firestore.collection(IFoodConstantes.FIRESTORE_CATEGORIA)
            val querySnapshot = lojasRef.get().await()
            if (querySnapshot.documents.isEmpty()) {
                uiStatus.invoke(
                    UiStatus.Erro("Não existem Lojas")
                )
                return
            }

            val listaDeLojas = querySnapshot.documents.mapNotNull { documentSnapshot ->
                documentSnapshot.toObject(Loja::class.java)
            }

            if (listaDeLojas.isEmpty()) {
                uiStatus.invoke(
                    UiStatus.Erro("Não foi possivel converter as lojas")
                )
                return
            }


            uiStatus.invoke(
                UiStatus.Sucesso(listaDeLojas)
            )


        } catch (e: Exception) {
            uiStatus.invoke(
                UiStatus.Erro("Não foi possivel converter as lojas")
            )
        }
    }

    override suspend fun cadastrar(loja: Loja, status: (UiStatus<Boolean>) -> Unit) {
        val idUser = auth.currentUser?.uid

        try {
            if (idUser != null){
                val lojaRef = firestore.collection(IFoodConstantes.FIRESTORE_LOJA)
                    .document(idUser)

                lojaRef.update(loja.toMap()).await()

                status.invoke(UiStatus.Sucesso(true))

            }
        }catch (e :Exception){
            status.invoke(UiStatus.Erro(mensagem = "Erro ao salvar Loja"))
        }

    }

    override suspend fun editar(loja: Loja, status: UiStatus<Boolean>) {


    }

    override suspend fun recuperarDadosLoja(idloja :String, status: (UiStatus<Loja>) -> Unit) {
        try {
           // val idUser = auth.currentUser?.uid ?: throw  Exception()
            val lojaRef = firestore.collection(IFoodConstantes.FIRESTORE_LOJA)
                .document(idloja)

            val documenteSnapshot = lojaRef.get().await()
            if (documenteSnapshot.exists()){
                 val dados = documenteSnapshot.toObject(Loja::class.java)
                if (dados != null){
                    status.invoke(UiStatus.Sucesso(dados))
                }
            }else{
                UiStatus.Erro("Nao existem dados para o usuairio")
            }

        }catch (erro :Exception){
            status.invoke(
                UiStatus.Erro("erro ao recuperar dados daloja")
            )
        }
    }


}