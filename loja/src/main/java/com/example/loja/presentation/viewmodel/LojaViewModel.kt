package com.example.loja.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.uistatus.UiStatus
import com.example.data.constantes.IFoodConstantes
import com.example.data.repository.UploadRepository
import com.example.domain.models.Categoria
import com.example.domain.models.Loja
import com.example.domain.models.TipoImagemUpload
import com.example.domain.models.UploadLoja
import com.example.domain.repository.ICategoriaRepository
import com.example.domain.repository.ILojaRepository
import com.example.loja.model.ValidacacaoCadastroLojaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LojaViewModel @Inject constructor (
    private val validacao : ValidacacaoCadastroLojaUseCase,
    private val lojaRepository : ILojaRepository,
    private val uploadRepository: UploadRepository,
    private val repositoryCategorias: ICategoriaRepository
) :ViewModel() {
     private val _carregando = MutableLiveData<Boolean>()
     val carregando :LiveData<Boolean> =_carregando

    private val _resultadoValidacao= MutableLiveData<Boolean>()
    val resultadoValidacao : LiveData<Boolean> =_resultadoValidacao

    fun recuperarDadosLoja(idLoja:String ,uiStatus: (UiStatus<Loja>)->Unit){
        _carregando.value =true
         viewModelScope.launch {
             lojaRepository.recuperarDadosLoja(idLoja,uiStatus)
             _carregando.value =false
         }
    }

    fun recuperarCategorias(uiStatus: (UiStatus<List<Categoria>>)->Unit){
        _carregando.value =true
        viewModelScope.launch {
            repositoryCategorias.recuperarCategorias(uiStatus)
            _carregando.value =false
        }
    }
    fun uploadImagem(
        uploadLoja: UploadLoja,
        uiStatus: ( UiStatus<Boolean> )->Unit
    ){
        viewModelScope.launch {
           _carregando.value =true

            //Upload Storage
            val upload = async {
                uploadRepository.uploadImage(
                    uploadLoja.nome,
                    uploadLoja.uriImage,
                    IFoodConstantes.STORAGE_PRODUTO
                )
            }
            val uiStatusUpload = upload.await()

            if ( uiStatusUpload is UiStatus.Sucesso ){

                val urlUpload = uiStatusUpload.dados
                var dadosAtualizar = mapOf<String, String>()
                if( uploadLoja.tipoImagem == TipoImagemUpload.IMAGEM_CAPA){
                    dadosAtualizar = mapOf("urlCapa" to urlUpload)
                }else{
                    dadosAtualizar = mapOf("urlPerfil" to urlUpload)
                }

                lojaRepository.atualizarDadoLoja( dadosAtualizar, uiStatus )
                _carregando.value =false

            }else{//erro
                _carregando.value =false
                uiStatus.invoke(
                    UiStatus.Erro("Erro ao fazer upload da imagem")
                )
            }

        }
    }

   /* fun  uploadImgage(uploadLoja: UploadLoja,uiStatus: (UiStatus<List<String>>)->Unit){

        viewModelScope.launch {
            _carregando.value = true
           val uploadPerfil = async {
               uploadRepository.uploadImage(uploadLoja.nomePerfil,uploadLoja.uriImagePerfil,IFoodConstantes.STORAGE_CAPA)
           }
            val uploadCapa = async {
                uploadRepository.uploadImage(uploadLoja.nomeCapa,uploadLoja.uriImageCapa,IFoodConstantes.STORAGE_CAPA)
            }

            val uiStatusCapa = uploadCapa.await()
            val uiStatusPerfil = uploadPerfil.await()

            if (uiStatusCapa is UiStatus.Sucesso && uiStatusPerfil is UiStatus.Sucesso){
                 UiStatus.Sucesso(
                     listOf(uiStatusCapa.dados,uiStatusPerfil.dados)
                 )
                _carregando.value = false
            }else if (uiStatusCapa is UiStatus.Erro){
                UiStatus.Erro("Erro ao fazer upload da imagem de capa")
                _carregando.value = false
            }else if (uiStatusPerfil is UiStatus.Erro){
                UiStatus.Erro("Erro ao fazer upload da imagem de perfil")
                _carregando.value = false
            }
        }
    }
*/
    fun cadastrar(loja: Loja,status: (UiStatus<Boolean> ) -> Unit){
         _carregando.value = true
           _resultadoValidacao.value = validacao.validacaoCadastroLoja(loja)
         if (_resultadoValidacao.value == true){
            viewModelScope.launch {
                lojaRepository.cadastrar(loja,status)
            }
         }
       _carregando.value = false

    }



}