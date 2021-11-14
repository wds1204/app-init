package com.smart.wds.init.runtime

import android.content.Context
import java.util.concurrent.CountDownLatch

abstract class AbstractInitializer<T> : Initializer<T> {

    private val countDownLatch = CountDownLatch(dependencies()?.size ?: 0)
    override fun await() {
        try {
            countDownLatch.await()
        }catch (e:InterruptedException){

        }
    }

    override fun countDown() {
        countDownLatch.countDown()
    }


    override fun dependencies(): List<Class<out Initializer<*>>>?=null
    //TODO
    override fun classDependencies(): List<String>? =null

    override fun onDependenciesCompleted(initializer: Initializer<*>, result: Any?) {
    }
}