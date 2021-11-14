package com.smart.wds.app.init

import android.content.Context
import androidx.startup.Initializer
import com.smart.wds.init.runtime.AbstractInitializer
import com.smart.wds.init.runtime.ThreadEnv

class AInitDep : AbstractInitializer<AInitDep.Dependency>() {


    class Dependency {
    }

    override fun onCreate(context: Context): Dependency? {
        println("AInitDep========")
        return Dependency()
    }

    override fun needWaitMain(): Boolean = false

    override fun callOnThread(): ThreadEnv = ThreadEnv.MAIN

}