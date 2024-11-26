package com.example.data.repository

import com.example.core.uistatus.UiStatus
import com.example.data.constantes.IFoodConstantes
import com.example.domain.models.Produto
import com.example.domain.models.toMap
import com.example.domain.repository.IProdutoRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProdutoRepositoryImpl @Inject  constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) :IProdutoRepository {

    override suspend fun salvar(produto: Produto, status: (UiStatus<String>) -> Unit) {
        val idLoja = auth.currentUser?.uid

        try {
            if (idLoja != null){
                val produtoRef = firestore
                    .collection(IFoodConstantes.FIRESTORE_PRODUTO)
                    .document(idLoja)
                    .collection("itens")
                    .document()

                 val idProduto = produtoRef.id
                 produto.copy(id = idProduto)
                produtoRef.set(produto).await()

                status.invoke(UiStatus.Sucesso(produto.id))

            }else{
                status.invoke(UiStatus.Erro(mensagem = "Usuario não está logado"))
            }
        }catch (e :Exception){
            status.invoke(UiStatus.Erro(mensagem = "Erro ao salvar Produto"))
        }
    }

    override suspend fun atualizar(produto: Produto, status:(UiStatus<String>) -> Unit) {
        try {
            val idUsuario = auth.currentUser?.uid
            if( idUsuario != null ){
                val produtoRef = firestore
                    .collection( IFoodConstantes.FIRESTORE_PRODUTO )
                    .document( idUsuario )
                    .collection("itens")
                    .document(produto.id)

                produtoRef.update( produto.toMap() ).await()

                status.invoke(
                    UiStatus.Sucesso(produto.id)
                )
            }
        }catch ( erroCadastrarLoja: Exception ){
            status.invoke(
                UiStatus.Erro("Erro ao salvar dados da loja")
            )
        }
    }

    override suspend fun listar(idLoja:String,status: (UiStatus<List<Produto>>) -> Unit) {
        try {
          /*  val idLoja = auth.currentUser?.uid ?: return status.invoke(
                UiStatus.Erro("usuario não esta logado")
            )*/

          val  produtoRef = firestore
                .collection( IFoodConstantes.FIRESTORE_PRODUTO )
                .document( idLoja )
                .collection("itens")
            val querySnapshot = produtoRef.get().await()

            if (querySnapshot.documents.isEmpty()) {
                status.invoke(
                    UiStatus.Erro("Não existem produtos")
                )
                return
            }

          val listaProdutos = querySnapshot.documents.mapNotNull { documentSnapshot ->
              documentSnapshot.toObject(Produto::class.java)
          }


            status.invoke(
                UiStatus.Sucesso(listaProdutos)
            )

        } catch (e: Exception) {
            status.invoke(
                UiStatus.Erro("Não foi possivel converter as categorias")
            )
        }
    }

    override suspend fun recuperarPeloId(idLoja:String, idProduto: String, status: (UiStatus<Produto>) -> Unit) {
       try {

           //val idLoja = auth.currentUser?.uid ?: return status.invoke((UiStatus.Erro("usuario não esta logado")))
           val produtoRef = firestore.collection(IFoodConstantes.FIRESTORE_PRODUTO)
               .document(idLoja)
               .collection(IFoodConstantes.FIRESTORE_ITEMS)
               .document(idProduto)

            val documentSnapshot = produtoRef.get().await()
           if (documentSnapshot.exists()){
               val produto = documentSnapshot.toObject(Produto::class.java)
               if (produto != null){
                   status.invoke(UiStatus.Sucesso(produto))
               }else{
                   status.invoke(UiStatus.Erro("não exitem dados para o produto"))
               }
           }
       }catch (erro :Exception){
           status.invoke(UiStatus.Erro("Erro ao recuparar dados do produto"))
       }
    }

    override suspend fun remover(idProduto: String, status: (UiStatus<Boolean>) -> Unit) {
        try {

            val idLoja = auth.currentUser?.uid ?: return status.invoke((UiStatus.Erro("usuario não esta logado")))
            val produtoRef = firestore.collection(IFoodConstantes.FIRESTORE_PRODUTO)
                .document(idLoja)
                .collection(IFoodConstantes.FIRESTORE_ITEMS)
                .document(idProduto)

            produtoRef.delete().await()

            status.invoke(UiStatus.Sucesso(true))

        }catch (erro :Exception){
            status.invoke(UiStatus.Erro("Erro ao remover o produto"))
        }
    }
}