package com.smart.wds.init.runtime.dispatcher

import com.smart.wds.init.runtime.ThreadEnv

interface Dispatcher {
    /**
     * to await dependencies initializer
     */
    fun await()

    /**
     * to countDown the initializer  when dependencies initializer completed
     */
    fun countDown()

    /**
     * if [callOnThread] return [ThreadEnv.MAIN] main thread default block
     */
    fun needWaitMain(): Boolean


    fun callOnThread(): ThreadEnv
}