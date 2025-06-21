package com.example.beautisdk.di

import com.example.aperoaiservice.ServiceFactory
import com.example.aperoaiservice.network.repository.AiArtRepository
import com.example.beautisdk.ui.screen.art.preview.VslArtPreviewViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val appModule = module {
    factory<AiArtRepository> { ServiceFactory.getService() }
    viewModel { VslArtPreviewViewModel(get()) }
}

