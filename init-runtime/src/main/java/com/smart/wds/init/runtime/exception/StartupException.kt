package com.smart.wds.init.runtime.exception

import java.lang.RuntimeException

class StartupException : RuntimeException {
    constructor(message: String?) : super(message)


    constructor (throwable: Throwable) : super(throwable)


}