package com.smart.wds.app.init

import android.content.Context
import androidx.startup.Initializer

class CInitDep:Initializer<CInitDep.Dependency> {

    class Dependency{

    }

    override fun create(context: Context): Dependency {
        println("CInitDep========")

        return Dependency()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(AInitDep::class.java,BInitDep::class.java)
    }
}