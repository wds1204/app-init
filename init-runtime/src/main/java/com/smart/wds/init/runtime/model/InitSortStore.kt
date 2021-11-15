package com.smart.wds.init.runtime.model

import com.smart.wds.init.runtime.Initializer

data class InitSortStore(
    val result: MutableList<Initializer<*>>,
    val allStartInit: Map<String, Initializer<*>>,
    val initChildrenMap: Map<String, MutableList<String>>
)
