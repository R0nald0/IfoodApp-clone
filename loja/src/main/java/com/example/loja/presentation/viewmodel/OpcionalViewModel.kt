package com.example.loja.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.uistatus.UiStatus
import com.example.data.repository.UploadRepository
import com.example.domain.models.Opcional
import com.example.domain.models.UploadStorage
import com.example.domain.repository.IOpcionalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OpcionalViewModel @Inject constructor(
    private val opcionalRepository: IOpcionalRepository,
    private val uploadRepository: UploadRepository,

    ) : ViewModel() {



    fun salvar(opcional: Opcional,uploadStorage: UploadStorage ,uiStatus: (UiStatus<String>) -> Unit){
      //  _carregando.value = true
         viewModelScope.launch {

             val uploadImage  = async {
                 uploadRepository.uploadImage(
                     uploadStorage.nome,
                     uploadStorage.uriImage,
                     uploadStorage.local
                 )
             }

             val uiStatusUpload = uploadImage.await()

             if (uiStatusUpload is UiStatus.Sucesso) {
                 opcional.copy(uriImagem = uiStatusUpload.dados)
                 opcionalRepository.salvar(opcional, status = uiStatus)
             } else {//erro
                 //_carregando.value = false
                 uiStatus.invoke(
                     UiStatus.Erro("Erro ao fazer upload da imagem")
                 )
             }

         }
        //_carregando.value = false
    }

    fun listar( idLoja :String ,idProduto :String,status:(UiStatus<List<Opcional>>)-> Unit){
        viewModelScope.launch {
            opcionalRepository.listar(idLoja, idProduto ,status)
        }
    }
    fun  remover(idProduto: String, idOpcional:String, uiStatus: (UiStatus<Boolean>) -> Unit){
        viewModelScope.launch {
          //  _carregando.value =true
            opcionalRepository.remover(idProduto,idOpcional,uiStatus)
          //  _carregando.value =false
        }
    }
}
