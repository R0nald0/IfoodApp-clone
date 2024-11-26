package com.example.loja.model

import com.example.domain.models.Loja
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator

class ValidacacaoCadastroLojaUseCase {

    fun validacaoCadastroLoja(loja: Loja) :Boolean{

        val nome = loja.nome.validator().nonEmpty().check()
        val categoria = loja.categoria.validator().nonEmpty().check()
        val razaoSocial = loja.razaoSocial.validator().nonEmpty().check()
        val cnpj = loja.cnpj.validator().nonEmpty().check()
        val sobre = loja.sobreEmpresa.validator().nonEmpty().check()
        val telefone = loja.telefone.validator().nonEmpty().check()


        return true
    }

}