package com.smart.wds.app.init

import android.content.Context
import androidx.startup.Initializer
import com.smart.wds.init.runtime.AbstractInitializer
import com.smart.wds.init.runtime.ThreadEnv

class AInitDep : AbstractInitializer<AInitDep.ADependency>() {


    class ADependency {
    }

    override fun onCreate(context: Context): ADependency? {
        println("AInitDep========")
        return ADependency()
    }

    override fun needWaitMain(): Boolean = false

    override fun callOnThread(): ThreadEnv = ThreadEnv.MAIN

}