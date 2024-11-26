package com.example.ifood_clone2.di

import com.example.data.repository.ProdutoRepositoryImpl
import com.example.data.repository.AutenticacaoRepositoryImpl
import com.example.data.repository.LojaRepositoryImpl
import com.example.data.repository.OpcionalRepositoryImpl
import com.example.domain.repository.IProdutoRepository
import com.example.domain.repository.IAutenticacaoRepository
import com.example.domain.repository.ILojaRepository
import com.example.domain.repository.IOpcionalRepository
import com.example.domain.usecase.IRecuperarProdutosUseCase
import com.example.domain.usecase.impl.AutenticacaoUseCaseImpl
import com.example.domain.usecase.impl.RecuperarProdutosImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ClassModule {

    @Provides
    fun providerProdutoRepository(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore): IProdutoRepository {
        return ProdutoRepositoryImpl(firestore,firebaseAuth)
     }

    @Provides
    fun providerLojaRepository(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore): ILojaRepository {
        return LojaRepositoryImpl(firestore,firebaseAuth)
    }

    @Provides
    fun providerOpcioanalRepository(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore): IOpcionalRepository {
        return OpcionalRepositoryImpl(firestore,firebaseAuth)
    }

    @Provides
    fun  provideRecuperarProdutoRepository(iProdutoRepository: IProdutoRepository):IRecuperarProdutosUseCase{
        return  RecuperarProdutosImpl(iProdutoRepository)
    }


    @Provides
    fun providerAuntenticaoUseCase(autenticaoRepositoryImpl: IAutenticacaoRepository): AutenticacaoUseCaseImpl {
        return AutenticacaoUseCaseImpl(autenticaoRepositoryImpl)
    }

    @Provides
    fun providerAuntenticaoRepository(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore): AutenticacaoRepositoryImpl {
        return AutenticacaoRepositoryImpl(firebaseAuth, firestore)
    }

}