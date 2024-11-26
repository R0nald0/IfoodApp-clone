package com.example.ifood_clone2.di
import com.example.data.repository.AutenticacaoRepositoryImpl
import com.example.domain.repository.IAutenticacaoRepository
import com.example.domain.usecase.impl.AutenticacaoUseCaseImpl
import com.example.domain.usecase.IAutenticacaoUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface InterfaceModule {

  @Singleton
  @Binds
  fun bindAutenticationUseCase( autenticacaoUseCaseImpl: AutenticacaoUseCaseImpl):IAutenticacaoUseCase

  @Singleton
  @Binds
  fun bindAutenticacaoRepository( autenticacaoRepository: AutenticacaoRepositoryImpl):IAutenticacaoRepository
}