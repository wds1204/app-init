package com.smart.wds.init.runtime.dispatcher

import android.content.Context
import com.smart.wds.init.runtime.Initializer
import com.smart.wds.init.runtime.ThreadEnv
import com.smart.wds.init.runtime.extensions.getUniqueKey
import com.smart.wds.init.runtime.model.InitSortStore
import com.smart.wds.init.runtime.runnable.InitRunnable

class InitPerformDispatcher(
    val context: Context,

    ) : PerformDispatcher {
    override fun dispatch(initializer: Initializer<*>, store: InitSortStore) {

        val runnable = InitRunnable(context, initializer, store, this)
        if (initializer.callOnThread() == ThreadEnv.MAIN) {
            runnable.run()
        } else {
            initializer.createExecutor().execute(runnable)
        }

    }

    override fun notifyChildren(
        dependencyParent: Initializer<*>,
        result: Any?,
        store: InitSortStore
    ) {
        store.initChildrenMap[dependencyParent::class.java.getUniqueKey()]?.forEach {
            store.allStartInit[it]?.run {
                onDependenciesCompleted(dependencyParent, result)
                countDown()
            }
        }


    }
}