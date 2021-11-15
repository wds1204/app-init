package com.smart.wds.init.runtime.execute

import java.util.concurrent.Executor

interface InitExecute {
    fun createExecutor():Executor
}