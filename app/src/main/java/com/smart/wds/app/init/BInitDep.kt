package com.smart.wds.app.init

import android.content.Context
import androidx.startup.Initializer
import com.smart.wds.init.runtime.AbstractInitializer
import com.smart.wds.init.runtime.ThreadEnv

class BInitDep: AbstractInitializer<BInitDep.Dependency>() {

    class Dependency{

    }

    override fun onCreate(context: Context): Dependency? {
        println("BInitDep========")
        return Dependency()
    }

    override fun needWaitMain(): Boolean =true

    override fun callOnThread(): ThreadEnv = ThreadEnv.IO


}