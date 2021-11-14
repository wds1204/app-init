package com.smart.wds.app.init

import android.content.Context
import androidx.startup.Initializer

class BInitDep:Initializer<BInitDep.Dependency> {

    class Dependency{

    }

    override fun create(context: Context): Dependency {
        println("BInitDep========")

        return Dependency()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}