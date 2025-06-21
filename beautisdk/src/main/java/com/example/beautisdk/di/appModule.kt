package com.example.beautisdk.di

import com.example.aperoaiservice.ServiceFactory
import com.example.aperoaiservice.network.repository.AiArtRepository
import com.example.beautisdk.ui.screen.art.preview.VslArtPreviewViewModel
import com.example.beautisdk.ui.screen.splash.VslSplashViewModel
import com.example.beautisdk.utils.repository.ArtRepository
import com.example.beautisdk.utils.repository.ArtRepositoryImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val appModule = module {
    factory<AiArtRepository> { ServiceFactory.getService() }
    factory<ArtRepository> { ArtRepositoryImpl(get()) }

    viewModel { VslSplashViewModel(get()) }
    viewModel { VslArtPreviewViewModel(get(), get()) }
}

