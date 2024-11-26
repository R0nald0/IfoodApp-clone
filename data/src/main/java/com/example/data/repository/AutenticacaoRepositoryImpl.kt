package com.example.data.repository

import android.util.Log
import com.example.core.uistatus.UiStatus
import com.example.data.constantes.IFoodConstantes
import com.example.domain.models.Usuario
import com.example.domain.repository.IAutenticacaoRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AutenticacaoRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : IAutenticacaoRepository {

    override suspend fun cadastrarUsuario(usuario: Usuario, status: (UiStatus<Boolean>) -> Unit) {
        try {
            val result = auth.createUserWithEmailAndPassword(
                usuario.email,
                usuario.senha
            ).await() != null

            if (!result) {
                return status.invoke(UiStatus.Erro("erro ao cadastrar usuário"))
            }

            val idUsuario = auth.currentUser?.uid
                ?: return status.invoke(UiStatus.Erro("erro ao buscar dados usuário"))

            val usuarioRef = firestore.collection(IFoodConstantes.STORAGE_USUARIO)
                .document(idUsuario)
                .set(usuario.copy(id = idUsuario))
                .await()

            status.invoke(UiStatus.Sucesso(result))

        } catch (invalidUserExecption: FirebaseAuthInvalidUserException) {
            Log.e(
                "TAG",
                "cadastrarUsuario: ${invalidUserExecption.printStackTrace()}",
                invalidUserExecption
            )
            status.invoke(
                UiStatus.Erro("Email inválido,verifique o email digitado")
            )
        } catch (weakPasswordException: FirebaseAuthWeakPasswordException) {
            Log.e(
                "TAG",
                "cadastrarUsuario: ${weakPasswordException.printStackTrace()}",
                weakPasswordException
            )
            status.invoke(
                UiStatus.Erro("senha fraca,por favor insira uma senha com no minino 5 caracteres")
            )
        } catch (colisionExecption: FirebaseAuthUserCollisionException) {
            Log.e(
                "TAG",
                "cadastrarUsuario: ${colisionExecption.printStackTrace()}",
                colisionExecption
            )
            status.invoke(
                UiStatus.Erro("Email já em uso,por favor utilize um novo email")
            )
        } catch (ex: Exception) {
            Log.e("TAG", "cadastrarUsuario: ${ex.printStackTrace()}", ex)
            status.invoke(
                UiStatus.Erro("Algo deu errado,entre em contato com o supporte")
            )
        }
    }

    override suspend fun logarUsuario(usuario: Usuario, status: (UiStatus<Boolean>) -> Unit) {
        try {
            val result = auth.signInWithEmailAndPassword(
                usuario.email,
                usuario.senha
            ).await() != null
            status.invoke(
                UiStatus.Sucesso(result)
            )
        } catch (invalidUserExecption: FirebaseAuthInvalidUserException) {
            Log.e("TAG", "logar: ${invalidUserExecption.printStackTrace()}", invalidUserExecption)
            status.invoke(
                UiStatus.Erro("Email inválido,verifique o email digitado")
            )
        } catch (userException: FirebaseAuthInvalidUserException) {
            Log.e("TAG", "cadastrarUsuario: ${userException.printStackTrace()}", userException)
            status.invoke(
                UiStatus.Erro("senha fraca,por favor insira uma senha com no mínino 5 caracteres")
            )
        } catch (ex: Exception) {
            Log.e("TAG", "cadastrarUsuario: ${ex.printStackTrace()}", ex)
            status.invoke(
                UiStatus.Erro("Algo deu errado,entre em contato com o supporte")
            )
        }

    }

    override fun isLogged(): String? {
        return auth.currentUser?.uid
    }

    override fun logout() {
        auth.signOut()
    }


}