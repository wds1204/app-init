package com.smart.wds.init.runtime.topological

import java.util.*


fun main(args:Array<String>){
//    val prerequisites =
//        arrayOf(intArrayOf(1, 0), intArrayOf(2, 0), intArrayOf(3, 1), intArrayOf(3, 2))
//    val num = 4
//    val orders = Solution.topSort(num, prerequisites)
//    for (order in orders!!) {
//        print(order.toString() + "\t")
//    }
//    print("\n")

}
internal object Solution {

    fun topSort(num: Int, prerequisites: Array<IntArray>): IntArray? {
        if (num == 0) return IntArray(0)
        val inDegree = IntArray(num)
        for (array in prerequisites) {
            inDegree[array[0]]++
        }
        val queue: Queue<Int> = LinkedList()
        for (i in inDegree.indices) {
            if (inDegree[i] == 0) {
                queue.add(i)
            }
        }
        val result = ArrayList<Int>()
        while (!queue.isEmpty()) {
            val key = queue.poll()
            result.add(key)
            //再次遍历课程
            for (p in prerequisites) {
                if (key == p[1]) {
                    inDegree[p[0]]--
                    if (inDegree[p[0]] == 0) {
                        queue.add(p[0])
                    }
                }
            }
        }
        if (result.size != num) return IntArray(0)
        val array = IntArray(num)
        for (i in result.indices) {
            array[i] = result[i]
        }
        return array
    }

}