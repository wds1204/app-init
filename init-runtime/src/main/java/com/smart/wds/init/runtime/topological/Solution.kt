package com.smart.wds.init.runtime.topologySort

import com.smart.wds.init.runtime.AbstractInitializer
import com.smart.wds.init.runtime.Initializer
import com.smart.wds.init.runtime.ThreadEnv
import com.smart.wds.init.runtime.exception.StartupException
import com.smart.wds.init.runtime.extensions.getUniqueKey
import com.smart.wds.init.runtime.model.InitSortStore
import java.util.*

fun main(args: Array<String>) {

}

internal object Solution {

    fun topSort(initList: List<Initializer<*>>): InitSortStore {
        val sortList = arrayListOf<Initializer<*>>()
        val mainResult = arrayListOf<Initializer<*>>()
        val ioResult = arrayListOf<Initializer<*>>()
        val inDegreeMap = hashMapOf<String, Int>()
        val zeroDequen = LinkedList<String>()
        val allStartInit = hashMapOf<String, Initializer<*>>()
        val initChildrenMap = hashMapOf<String, MutableList<String>>()
        initList.forEach {
            val uniqueKey = it::class.java.getUniqueKey()
            if (allStartInit[uniqueKey] == null) {
                allStartInit[uniqueKey] = it
                inDegreeMap[uniqueKey] = it.dependencies()?.size ?: 0

                if (it.dependencies().isNullOrEmpty()) {
                    zeroDequen.add(uniqueKey)
                } else {
                    it.dependencies()?.forEach { parent ->
                        val parentUniqueKey = parent.getUniqueKey()
                        if (initChildrenMap[parentUniqueKey] == null) {
                            initChildrenMap[parentUniqueKey] = arrayListOf()
                        }
                        initChildrenMap[parentUniqueKey]?.add(uniqueKey)
                    }
                }
            }
        }


        while (!zeroDequen.isEmpty()) {
            zeroDequen.poll()?.let {
                allStartInit[it]?.let { initializer ->
                    sortList.add(initializer)
                    //区分main 、io
                    if (initializer.callOnThread() == ThreadEnv.IO) {
                        ioResult.add(initializer)
                    } else {
                        mainResult.add(initializer)
                    }
                }
                //children的入度减一
                initChildrenMap[it]?.forEach { children ->
                    inDegreeMap[children] = inDegreeMap[children]?.minus(1) ?: 0
                    if (inDegreeMap[children] == 0) {
                        zeroDequen.add(children)
                    }
                }
            }
        }

        if (sortList.size != allStartInit.size) {
            throw StartupException("lack of dependencies or have circle dependencies.")
        }

        val result = mutableListOf<Initializer<*>>().apply {
            addAll(ioResult)
            addAll(mainResult)
        }

        return InitSortStore(result, allStartInit, initChildrenMap)


    }


}