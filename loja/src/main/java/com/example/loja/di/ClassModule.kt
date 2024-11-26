package com.example.loja.di

import com.example.data.repository.CategoriaRepositoryImpl
import com.example.data.repository.LojaRepositoryImpl
import com.example.data.repository.OpcionalRepositoryImpl
import com.example.data.repository.ProdutoRepositoryImpl
import com.example.data.repository.UploadRepository
import com.example.data.repository.AutenticacaoRepositoryImpl
import com.example.domain.repository.IOpcionalRepository
import com.example.domain.repository.IProdutoRepository
import com.example.domain.repository.IAutenticacaoRepository
import com.example.domain.usecase.impl.AutenticacaoUseCaseImpl
import com.example.loja.model.ValidacacaoCadastroLojaUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ClassModule {
    @Provides
    @Singleton
    fun providerAuntenticaoUseCase(autenticaoRepositoryImpl: IAutenticacaoRepository): AutenticacaoUseCaseImpl {
        return AutenticacaoUseCaseImpl(autenticaoRepositoryImpl)
    }
    @Provides
    @Singleton
    fun providerOpcionalRepository(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore): IOpcionalRepository {
        return OpcionalRepositoryImpl(firestore,firebaseAuth)
    }

    @Provides
    @Singleton
    fun providerAuntenticaoRepository(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore): AutenticacaoRepositoryImpl {
        return AutenticacaoRepositoryImpl(firebaseAuth,firestore)
    }
    @Provides
    @Singleton
    fun providerLojaRepository(firestore: FirebaseFirestore, firebaseAuth: FirebaseAuth): LojaRepositoryImpl {
        return LojaRepositoryImpl(firestore = firestore, auth = firebaseAuth)
    }

    @Provides
    @Singleton
    fun providerProdutoRepository(firestore: FirebaseFirestore, firebaseAuth: FirebaseAuth): IProdutoRepository {
        return ProdutoRepositoryImpl(firestore = firestore, auth = firebaseAuth)
    }

    @Provides
    @Singleton
    fun providerUploadRepository(firebaseStorage: FirebaseStorage,firebaseAuth: FirebaseAuth):UploadRepository {
        return UploadRepository(firebaseStorage,firebaseAuth)
    }
    @Provides
    @Singleton
    fun providerCategoriaRepository(firebaseFirestore: FirebaseFirestore):CategoriaRepositoryImpl {
        return CategoriaRepositoryImpl(firebaseFirestore)
    }


    @Provides
    @Singleton
    fun provideValidacaoLojaUseCase(): ValidacacaoCadastroLojaUseCase {
        return ValidacacaoCadastroLojaUseCase()
    }


}