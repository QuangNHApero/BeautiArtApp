package com.example.aperoaiservice

import com.example.aperoaiservice.utils.ServiceProvider

object ServiceFactory {

    @JvmStatic
    @PublishedApi
    internal val serviceInstances = mutableMapOf<Class<out Any>, Any>()

    @JvmStatic
    @PublishedApi
    internal fun <T : Any> createInstance(clazz: Class<T>): T {
        val providerMethodName = "provide${clazz.simpleName}"
        val method = ServiceProvider::class.java.methods.find {
            it.name == providerMethodName
        } ?: throw IllegalArgumentException(
            "No provider method found for ${clazz.simpleName}"
        )

        return try {
            @Suppress("UNCHECKED_CAST")
            method.invoke(ServiceProvider) as T
        } catch (e: Exception) {
            throw IllegalStateException(
                "Failed to create instance of ${clazz.simpleName}",
                e
            )
        }
    }

    @JvmStatic
    inline fun <reified T : Any> getService(isCache: Boolean = true): T {
        val clazz = T::class.java
        return if (isCache) {
            val existing = serviceInstances[clazz]
            if (existing != null) {
                existing as T
            } else {
                val created = createInstance(clazz)
                serviceInstances[clazz] = created
                created
            }
        } else {
            serviceInstances[clazz] as? T ?: createInstance(clazz)
        }
    }
}