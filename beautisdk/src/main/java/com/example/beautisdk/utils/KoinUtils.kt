package com.example.beautisdk.utils

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier


internal object KoinUtils {
    fun isKoinStarted(): Boolean {
        return try {
            GlobalContext.get()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun startKoinIfNeeded(application: Context, modules: List<Module>) {
        if (!isKoinStarted()) {
            startKoin {
                androidContext(application)
                modules(modules)
            }
        } else {
            loadKoinModules(modules)
        }
    }

    fun getKoin() = GlobalContext.get()

    inline fun <reified T : Any> get(
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition? = null
    ): T {
        return GlobalContext.get().get<T>(qualifier, parameters)
    }
}