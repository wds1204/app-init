package com.smart.wds.init.runtime.extensions

import com.smart.wds.init.runtime.Initializer


/**
 * Created by idisfkj on 2020/8/10.
 * Email: idisfkj@gmail.com.
 */

const val DEFAULT_KEY = "com.smart.wds.init.runtime.defaultKey"

fun Class<out Initializer<*>>.getUniqueKey(): String {
    val canonicalName = this.canonicalName
    requireNotNull(canonicalName) { "Local and anonymous classes can not be Startup" }
    return "$DEFAULT_KEY:$canonicalName"
}