package com.example.ifood_clone2.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.uistatus.UiStatus
import com.example.data.repository.OpcionalRepositoryImpl
import com.example.data.repository.UploadRepository
import com.example.domain.models.Opcional
import com.example.domain.models.Produto
import com.example.domain.models.UploadStorage
import com.example.domain.repository.IOpcionalRepository
import com.example.domain.repository.IProdutoRepository
import com.example.domain.usecase.IRecuperarProdutosUseCase
import com.example.domain.usecase.impl.ProdutoSeparados
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProdutoViewModel @Inject constructor(
    private val produtoRepository: IProdutoRepository,
    private val produtosUseCase: IRecuperarProdutosUseCase,
    private val opcionalRepositoryImpl: IOpcionalRepository
    ) : ViewModel() {
    private val _carregando = MutableLiveData<Boolean>()
    val carregando : LiveData<Boolean> =_carregando


    fun recuperarProdutoPeloid(idLoja :String,idProduto :String,uiStatus: (UiStatus<Produto>) -> Unit){
        viewModelScope.launch {
            _carregando.value =true
            produtoRepository.recuperarPeloId(idLoja,idProduto, status = uiStatus)
            _carregando.value =false
        }
    }

    fun listar( idLoja :String  ,status:(UiStatus<List<ProdutoSeparados>>)-> Unit){
        viewModelScope.launch {
            _carregando.value =true
             produtosUseCase.invoke(idLoga = idLoja, uiStatus = status) //produtoRepository.listar( idloja = idLoja,status)
            _carregando.value =false
        }
    }
    fun listarOpcionais( idLoja :String,idProduto :String,status:(UiStatus<List<Opcional>>)-> Unit){
        _carregando.value = true
        viewModelScope.launch {
            opcionalRepositoryImpl.listar(idLoja = idLoja, idProduto =  idProduto , status =  status)
            _carregando.value = false
        }
    }


    }