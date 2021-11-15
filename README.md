# app-init

任务初始化组建

一个相对复杂的app在启动时通常会初始化很多第三方任务，而app的启动时间又是很敏感，这是我们把这些任务放到子线程中执行。
比如说app的启动需要初始化三个异步任务，第一屏需要这三个任务都结束才显示。最愚蠢的方式就是三个人异步任务嵌套执行，这样明显会导致
APP的首屏幕变慢。更好的方式可以采用countdownLatch来解决，在主线程中直接await吗，每个异步任务执行完countDown，只需耗时
最长的异步任务执行完，首屏幕加载出来。这样显然能解决这类问题，但一个大型的APP有很多SDK，。

#### 有向无环图

#### 拓扑排序

#### 任务启动
场景一： 任务启动先从简单的情况A、B（async 需要await）、C（async）、D四个任务，B、C任务为async，而且主线程依赖B任务结果。
这种情况相对简单

```kotlin
    //伪代码
    fun onCreate() {
        var N: Int = 获取需要await任务的个数
        var initList = 获取所有任务
        val countDownLatch = CountDownLatch(N)
        initList.forEach { initializers ->
        initializers.excute()//excute内执行countDownLatch.countDown()

        }
        countDownLatch.await()
    }

```

场景二：四个任务A、B、C、D
(!)


1）a和b在一个集合中
2）a和b在一个集合中，先执行a,因为是异步的的，b接下来也就执就执行了。所以说在b执行之前先await()，
当b执行完notify它的child(b) countDown()。如果count为0接下来执行b.



小优化
1、把排序后的任务分成异步和同步两个集合
2、先执行异步任务集合
3、在执行同步任务集合






