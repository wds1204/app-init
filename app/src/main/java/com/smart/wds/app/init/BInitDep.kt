package com.smart.wds.app.init

import android.content.Context
import androidx.startup.Initializer
import com.smart.wds.init.runtime.AbstractInitializer
import com.smart.wds.init.runtime.ThreadEnv

class BInitDep: AbstractInitializer<BInitDep.BDependency>() {

    class BDependency{

    }

    override fun onCreate(context: Context): BDependency? {
        println("BInitDep========sleep2000")
        Thread.sleep(2000)
        return BDependency()
    }

    override fun needWaitMain(): Boolean =true

    override fun callOnThread(): ThreadEnv = ThreadEnv.IO


}