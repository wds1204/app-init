package com.smart.wds.init.runtime.model

import com.smart.wds.init.runtime.AbstractInitializer

data class InitializerStore(
    val result: List<AbstractInitializer<*>>,
)
