package com.smart.wds.init.runtime.runnable

import android.content.Context
import com.smart.wds.init.runtime.Initializer
import com.smart.wds.init.runtime.dispatcher.PerformDispatcher
import com.smart.wds.init.runtime.model.InitSortStore

internal class InitRunnable(
    val context: Context,
    val initializer: Initializer<*>,
    private val store: InitSortStore,
    private val dispatcher: PerformDispatcher
) : Runnable {
    override fun run() {
        initializer.await()
        val result = initializer.onCreate(context)
        dispatcher.notifyChildren(initializer, result, store)

    }

}