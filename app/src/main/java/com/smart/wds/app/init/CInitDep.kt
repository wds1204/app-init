package com.smart.wds.app.init

import android.content.Context
import com.smart.wds.init.runtime.AbstractInitializer
import com.smart.wds.init.runtime.Initializer
import com.smart.wds.init.runtime.ThreadEnv

class CInitDep: AbstractInitializer<CInitDep.Dependency>() {

    class Dependency{

    }


    override fun dependencies(): List<Class<out Initializer<*>>>? {
        return mutableListOf(AInitDep::class.java,BInitDep::class.java)
    }
    override fun onCreate(context: Context): Dependency? {
        println("CInitDep========")
        return Dependency()
    }

    override fun needWaitMain(): Boolean =false

    override fun callOnThread(): ThreadEnv =ThreadEnv.MAIN
}