package com.smart.wds.init.runtime

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ProviderInfo
import com.smart.wds.init.runtime.exception.StartupException
import com.smart.wds.init.runtime.extensions.getUniqueKey
import com.smart.wds.init.runtime.model.InitializerStore
import com.smart.wds.init.runtime.provider.InitializationProvider
import java.util.HashSet

class AppInitializer {

    private val mInitialized: MutableList<String> by lazy {
        mutableListOf()
    }
    private val result: MutableList<AbstractInitializer<*>> by lazy {
        mutableListOf()
    }
    private val mDiscovered: MutableSet<Class<out Initializer<*>>> by lazy {
        HashSet()
    }
    private val sLock = Any()

    companion object {
        @JvmStatic
        val mInstance by lazy {
            AppInitializer()
        }
    }

    internal fun discoverAndInitialize(context: Context): InitializerStore {
        val mContext: Context = context.applicationContext
        try {
            val provider = ComponentName(
                mContext.packageName,
                InitializationProvider::class.java.name
            )
            val providerInfo: ProviderInfo = mContext.packageManager
                .getProviderInfo(provider, PackageManager.GET_META_DATA)
            val startup: String = mContext.getString(R.string.android_startup)

            providerInfo.metaData?.let {
                val initializing: MutableSet<String> = HashSet()

                it.keySet().forEach { key ->
                    val value = it.getString(key, null)
                    if (startup == value) {
                        val clazz = Class.forName(key)
                        if (Initializer::class.java.isAssignableFrom(clazz)) {
                            val component = clazz as Class<out Initializer<*>?>
                            doInitialize(
                                component.getDeclaredConstructor()
                                    .newInstance() as AbstractInitializer<*>, initializing
                            )
                        }
                    }
                }
            }

            return InitializerStore(result)


        } catch (exception: Throwable) {
            throw StartupException(exception)
        }
    }

    private fun doInitialize(
        initializer: AbstractInitializer<*>, initializing: MutableSet<String>
    ) {
        synchronized(sLock) {
            val uniqueKey = initializer::class.java.getUniqueKey()
            if (initializing.contains(uniqueKey)) {
                throw IllegalStateException("have circle dependencies.")
            }
            if (!mInitialized.contains(uniqueKey)) {
                initializing.add(uniqueKey)
                result.add(initializer)
                initializer.dependencies()?.forEach {
                    doInitialize(
                        it.getDeclaredConstructor().newInstance() as AbstractInitializer<*>,
                        initializing
                    )
                }

                initializing.remove(uniqueKey)
                mInitialized.add(uniqueKey)
            } else {
                mInitialized.add(uniqueKey)
            }
        }
    }
}