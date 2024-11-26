package com.example.loja.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.uistatus.UiStatus
import com.example.data.repository.UploadRepository
import com.example.domain.models.Produto
import com.example.domain.models.UploadStorage
import com.example.domain.repository.IProdutoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProdutoViewModel @Inject constructor(
    private val produtoRepository: IProdutoRepository,
    private val uploadRepository: UploadRepository,

    ) : ViewModel() {
    private val _carregando = MutableLiveData<Boolean>()
    val carregando : LiveData<Boolean> =_carregando

    fun  removerProduto(idProduto: String,uiStatus: (UiStatus<Boolean>) -> Unit){
        viewModelScope.launch {
            _carregando.value =true
             produtoRepository.remover(idProduto,uiStatus)
            _carregando.value =false
        }
    }

    fun recuperarProdutoPeloid(idUsuario: String,idProduto :String,uiStatus: (UiStatus<Produto>) -> Unit){
        viewModelScope.launch {
            _carregando.value =true
            produtoRepository.recuperarPeloId(idUsuario,idProduto, status = uiStatus)
            _carregando.value =false
        }
    }

    fun listar(idLoja: String, status:(UiStatus<List<Produto>>)-> Unit){
        viewModelScope.launch {
            produtoRepository.listar(idLoja,status)
        }
    }

    fun uploadImagem(upload: UploadStorage, idProduto: String, uiStatus: (UiStatus<String>) -> Unit) {
        viewModelScope.launch {
         val uploadImage  = async {
               uploadRepository.uploadImage(
                   upload.nome,
                   upload.uriImage,
                   upload.local
               )
           }

            val uiStatusUpload = uploadImage.await()

            if (uiStatusUpload is UiStatus.Sucesso) {

                val urlUpload = uiStatusUpload.dados
                var dadosAtualizar = mapOf<String, String>()
                val produto = Produto(
                    urlImagem = urlUpload,
                     id =  idProduto
                    )

                if (idProduto.isEmpty()) produtoRepository.salvar(produto,uiStatus)
                else produtoRepository.atualizar(produto,uiStatus)
                _carregando.value = false

            } else {//erro
                _carregando.value = false
                uiStatus.invoke(
                    UiStatus.Erro("Erro ao fazer upload da imagem")
                )
            }
        }
    }

    fun salvar(produto : Produto, uiStatus: (UiStatus<String> ) -> Unit){
        _carregando.value = true
        if (produto.id.isEmpty()){
            viewModelScope.launch {
                produtoRepository.salvar(produto, uiStatus)
            }
        }else{
            viewModelScope.launch {
                produtoRepository.atualizar(produto, uiStatus)
            }
        }
        _carregando.value = false
    }
    }