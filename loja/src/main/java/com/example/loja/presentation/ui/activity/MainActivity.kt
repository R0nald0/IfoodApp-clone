package com.example.loja.presentation.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuCompat
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.core.navegarPara
import com.example.loja.R
import com.example.loja.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
     private  val binding by lazy {
         ActivityMainBinding.inflate(layoutInflater)
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        iniicializarMunuProvider()
        inicializarToolbar()
    }

    private fun iniicializarMunuProvider() {
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate( R.menu.menu_principal_loja,menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.id_dados_loja ->{
                        navegarPara(LojaActivity::class.java)
                    }
                    R.id.id_cardapio ->{}
                    R.id.id_local_de_atuacao ->{}
                    R.id.id_sair->{
                        FirebaseAuth.getInstance().signOut()
                        navegarPara(LoginActivity::class.java)
                        finish()
                    }
                }
                return true
            }

        })
    }


    private fun inicializarToolbar(){
        binding.includeToolbarHome.tbPrincipal.apply {
            setSupportActionBar(this)
            supportActionBar?.apply {
                title="Cadastrar Parceiro"

            }
        }
    }
}