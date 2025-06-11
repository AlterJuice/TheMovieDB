package com.alterjuice.task.moviedb.errors

import com.alterjuice.utils.str.Str
import com.alterjuice.utils.str.StrRaw


class RootAppErrorMessagesProvider private constructor(
    private val registry: Set<AppErrorMessageProvider<AppError>>
) : AppErrorMessageProvider<AppError> {

    override fun canProvideMessage(e: AppError): Boolean {
        return registry.any { it.canProvideMessage(e) }
    }

    override fun provideMessage(e: AppError): Str {
        for (handler in registry) {
            if (handler.canProvideMessage(e)) {
                return handler.provideMessage(e)
            }
        }
        throw NotImplementedError("An unhandled error got; Do not forget to use #registryProvider upon error message provider")
    }

    fun provideMessageOr(e: Throwable, default: (Throwable) -> String): Str {
        if (e is AppError) {
            return provideMessage(e)
        }
        return StrRaw.Text(default(e))
    }



    class Builder() {
        private val registry = mutableSetOf<AppErrorMessageProvider<AppError>>()

        fun <T : AppError> registryProvider(provider: AppErrorMessageProvider<T>) = apply {
            registry.add(provider as AppErrorMessageProvider<AppError>)
        }

        fun build(): RootAppErrorMessagesProvider {
            return RootAppErrorMessagesProvider(registry)
        }
    }
}
