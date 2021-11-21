package com.smart.wds.init.runtime

import android.content.Context
import com.smart.wds.init.runtime.execute.ExecutorManager
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor

abstract class AbstractInitializer<T> : Initializer<T> {

    private val n1 = dependencies()?.size ?: 0
    private val countDownLatch = CountDownLatch(n1)
    override fun await() {
        try {
            countDownLatch.await()
        } catch (e: InterruptedException) {

        }
    }

    override fun countDown() {
        countDownLatch.countDown()
    }

    override fun dependencies(): List<Class<out Initializer<*>>>? = null

    override fun classDependencies(): List<String>? = null

    override fun onDependenciesCompleted(initializer: Initializer<*>, result: Any?) {
    }

    override fun createExecutor(): Executor = ExecutorManager.instance.ioExecutor
}