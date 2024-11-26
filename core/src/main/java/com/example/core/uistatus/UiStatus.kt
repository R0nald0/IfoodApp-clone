package com.example.core.uistatus

sealed interface UiStatus<out  T> {
   class Sucesso<T> (val dados :T) : UiStatus<T>
    class Erro(val mensagem : String) : UiStatus<Nothing>
}