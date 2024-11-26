package com.example.ifood_clone2.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.uistatus.UiStatus
import com.example.domain.models.Loja
import com.example.domain.repository.ILojaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LojaViewModel @Inject constructor (
    private val lojaRepository : ILojaRepository,
) :ViewModel() {
     private val _carregando = MutableLiveData<Boolean>()
     val carregando :LiveData<Boolean> =_carregando


    fun listar( uiStatus: (UiStatus<List<Loja>>)->Unit){
        _carregando.value =true
         viewModelScope.launch {
             lojaRepository.listarLojas( uiStatus)
             _carregando.value =false
         }
    }fun recuperarDadosLoja( idLoja :String,uiStatus: (UiStatus<Loja>)->Unit){
        _carregando.value =true
         viewModelScope.launch {
             lojaRepository.recuperarDadosLoja( idLoja = idLoja,uiStatus)
             _carregando.value =false
         }
    }

}