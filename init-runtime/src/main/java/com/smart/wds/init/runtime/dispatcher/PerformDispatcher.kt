package com.smart.wds.init.runtime.dispatcher

import com.smart.wds.init.runtime.Initializer
import com.smart.wds.init.runtime.model.InitSortStore

interface PerformDispatcher {

    fun dispatch(initializer: Initializer<*>, store: InitSortStore)

    fun notifyChildren(dependencyParent: Initializer<*>,result: Any?, store: InitSortStore)
}