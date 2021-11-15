package com.smart.wds.init.runtime

import android.content.Context
import com.smart.wds.init.runtime.dispatcher.Dispatcher
import com.smart.wds.init.runtime.execute.InitExecute

interface Initializer<T> : Dispatcher , InitExecute {
    /**
     * [context] The application context
     */
    fun onCreate(context: Context): T?

    /**
     * A list of dependencies that this  [Initializer] depends on. This is
     * used to determine initialization order of [Initializer]s.
     * <br/>
     * For e.g. if a  [Initializer] `B` defines another
     *  [Initializer] `A` as its dependency, then `A` gets initialized before `B`.
     */
    fun dependencies(): List<Class<out Initializer<*>>>?
    fun classDependencies(): List<String>?

    fun onDependenciesCompleted(initializer: Initializer<*>, result: Any?)


}