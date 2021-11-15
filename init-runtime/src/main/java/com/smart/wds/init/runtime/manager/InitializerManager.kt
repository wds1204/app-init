package com.smart.wds.init.runtime.manager

import android.content.Context
import com.smart.wds.init.runtime.AbstractInitializer
import com.smart.wds.init.runtime.ThreadEnv
import com.smart.wds.init.runtime.dispatcher.InitPerformDispatcher
import com.smart.wds.init.runtime.dispatcher.PerformDispatcher
import com.smart.wds.init.runtime.topologySort.Solution
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class InitializerManager(
    private val context: Context,
    private val mInitList: MutableList<AbstractInitializer<*>>,
    private val mNeedAwaitCount: AtomicInteger
) {
    private var mCountDownLatch: CountDownLatch? = null
    private val performDispatcher: PerformDispatcher by lazy {
        InitPerformDispatcher(context)
    }

    fun start() = apply {
        mCountDownLatch = CountDownLatch(mNeedAwaitCount.get())
        Solution.topSort(mInitList).run {
            result.forEach {
                performDispatcher.dispatch(it, this)
            }
        }

    }

    fun await() {
        try {
            mCountDownLatch?.await(1500, TimeUnit.MILLISECONDS)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }


    class Builder {

        private var mInitList = mutableListOf<AbstractInitializer<*>>()
        private var mNeedAwaitCount = AtomicInteger()

        fun addInitializer(initializer: AbstractInitializer<*>) = apply {
            this@Builder.mInitList.add(initializer)
        }

        fun addAllInitializer(initializers: List<AbstractInitializer<*>>) = apply {
            initializers.forEach {
                addInitializer(it)
            }
        }

        fun build(context: Context): InitializerManager {
            mInitList.forEach {
                if (it.needWaitMain() && it.callOnThread() == ThreadEnv.IO) {
                    mNeedAwaitCount.incrementAndGet()
                }
            }

            return InitializerManager(context, mInitList, mNeedAwaitCount)

        }
    }
}