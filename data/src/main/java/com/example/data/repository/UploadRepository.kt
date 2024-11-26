package com.example.data.repository

import android.net.Uri
import com.example.core.uistatus.UiStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UploadRepository @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun uploadImage(name :String, uri :Uri, local:String) : UiStatus<String>{
         try {
             val idUsuario = firebaseAuth.currentUser?.uid
                 ?: return  UiStatus.Erro("Erro ao fazer upload,dados incompletos")

             val lojaRef = firebaseStorage.getReference( local)
                 .child(idUsuario)
                 .child(name)

               lojaRef.putFile(uri).await()
               val downLoadUrl = lojaRef.downloadUrl.await().toString()
             return   UiStatus.Sucesso(downLoadUrl)

         }catch (e : Exception){
           return  UiStatus.Erro("erro ao fazer o upload da imagem")
         }
    }
}