package com.smart.wds.app.init

import android.content.Context
import androidx.startup.Initializer

class AInitDep : Initializer<AInitDep.Dependency> {
    override fun create(context: Context): Dependency {

        println("AInitDep========")
        return Dependency()

    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf();
    }

    class Dependency{
    }

}