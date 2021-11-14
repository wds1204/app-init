package com.smart.wds.init.runtime.init

import android.content.Context
import androidx.startup.Initializer


class LibInitDep : androidx.startup.Initializer<LibInitDep.Dependency> {

    class Dependency {

    }

    override fun create(context: Context): Dependency {
        println("LibInitDep=======init")

        return Dependency()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}