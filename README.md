# app-init

#### 使用

###### 初始化

```xml

<provider android:name="com.smart.wds.init.runtime.provider.InitializationProvider"
    android:authorities="${applicationId}.android_startup">
    <meta-data android:name="com.smart.wds.app.init.CInitDep" android:value="android_startup" />
</provider>
```

###### 初始化任务

```kotlin

class CInitDep : AbstractInitializer<CInitDep.Dependency>() {

    class Dependency {

    }

    override fun dependencies(): List<Class<out Initializer<*>>>? {
        return mutableListOf(AInitDep::class.java, BInitDep::class.java)
    }
    override fun onCreate(context: Context): Dependency? {
        println("CInitDep========")
        return Dependency()
    }
     //依赖任务完成后的回调   
    override fun onDependenciesCompleted(initializer: Initializer<*>, result: Any?) {
        super.onDependenciesCompleted(initializer, result)
        println("CInitDep========initializer====${initializer}  result===${result}")

    }

    override fun needWaitMain(): Boolean = false

    override fun callOnThread(): ThreadEnv = ThreadEnv.MAIN
}

class BInitDep : AbstractInitializer<BInitDep.BDependency>() {

    class BDependency {

    }

    override fun onCreate(context: Context): BDependency? {
        println("BInitDep========sleep2000")
        Thread.sleep(2000)
        return BDependency()
    }

    //需要main线程等待
    override fun needWaitMain(): Boolean = true

    //B任务运行在子线程中
    override fun callOnThread(): ThreadEnv = ThreadEnv.IO


}

class AInitDep : AbstractInitializer<AInitDep.ADependency>() {


    class ADependency {
    }

    override fun onCreate(context: Context): ADependency? {
        println("AInitDep========")
        return ADependency()
    }

    //不需要main线程等待
    override fun needWaitMain(): Boolean = false

    //A任务运行在子线程中
    override fun callOnThread(): ThreadEnv = ThreadEnv.IO

}
```

任务初始化

一个相对复杂的app在启动时通常会初始化很多第三方任务，而app的启动时间又是很敏感，这是我们把这些任务放到子线程中执行。
比如说app的启动需要初始化三个异步任务，应用第一屏需要这三个任务都结束才显示。最愚蠢的方式就是三个异步任务嵌套执行，这样明显会导致
APP的首屏幕变慢。更好的方式可以采用CountdownLatch来解决，在主线程中直接await吗，每个异步任务执行完调用CountdownLatch.countDown()方法，
这样只需等耗时最长的异步任务执行完，首屏幕加载出来。这样显然能解决这类问题，但一个大型的APP有很多SDK需要初始化，而且很有可能存在任务之前的依赖，
这样初始化就变得复杂，那就需要一个初始化组件统一管理。

#### 有向无环图

app启动的任务之间就像个有向无环无环

优先找到入度为零的顶点，放入到队列中。
![有向无环图](/imgs/有向无环图.gif)

#### 拓扑排序算法

BFS

现在你总共有 numCourses 门课需要选，记为 0 到 numCourses - 1。给你一个数组 prerequisites ， 其中 prerequisites[i] = [ai, bi]
，表示在选修课程 ai 前 必须 先选修 bi 。

例如，想要学习课程 0 ，你需要先完成课程 1 ，我们用一个匹配来表示：[0,1] 。 返回你为了学完所有课程所安排的学习顺序。可能会有多个正确的顺序，你只要返回 任意一种
就可以了。如果不可能完成所有课程， 返回 一个空数组 。

示例 1：

> 输入：numCourses = 2, prerequisites = [[1,0]]
> 输出：[0,1]
> 解释：总共有 2 门课程。要学习课程 1，你需要先完成课程 0。因此，正确的课程顺序为 [0,1] 。

示例 2：

> 输入：numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
> 输出：[0,2,1,3]
> 解释：总共有 4 门课程。要学习课程 3，你应该先完成课程 1 和课程 2。并且课程 1 和课程 2 都应该排在课程 0 之后。
> 因此，一个正确的课程顺序是 [0,1,2,3] 。另一个正确的排序是 [0,2,1,3] 。

示例 3：

> 输入：numCourses = 1, prerequisites = []
> 输出：[0]


[leetode算法题](https://leetcode-cn.com/problems/course-schedule-ii/)

```java 
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        if(numCourses==0)return new int[0];

        int[] array = new int[numCourses];

         //找各班级的入度
        int[] intDeps=new int[numCourses];
        for(int[] arry : prerequisites){
            intDeps[arry[0]]++;
        }
        [0,1,1,2]
        /*把入度为0的班级放入到栈中 */
        ArrayDeque<Integer> deque =new ArrayDeque();
    
        for(int i=0;i < intDeps.length; i++){
            if(intDeps[i] == 0){
                deque.add(i);
            }
        }
        //deque 0 1 2 3
        ArrayList<Integer> temp= new ArrayList<>();

        while(!deque.isEmpty()){
           Integer key= deque.poll();
           temp.add(key);

            for(int[] arry : prerequisites){
                if(arry[1]==key){
                   intDeps[arry[0]]--;
                    if(intDeps[arry[0]]==0){
                        deque.add(arry[0]);
                    }
                }
                     
            }

        }
        if(temp.size()!=numCourses)return new int[0];
        int index=0;
        for(int i=0;i < temp.size();i++){
            array[index++]=temp.get(i);
        }

        return array;
    }
```

#### 任务启动

场景一： 任务启动先从简单的情况A、B（async 需要await）、C（async）、D四个任务，B、C任务为async，而且主线程依赖B任务结果。 这种情况相对简单

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
![场景二](/imgs/img1.png)
D任务的依赖与A、B、C三个任务的 C依赖A、B B依赖A

B和C为async，且B需要让主线程await。这四个任务最外测线程控制的吗和场景一是一致的。只是如何管理任务之间的block和notify。

任务和任务之间的关系其实就是以下四种
![场景二](/imgs/img2.png)

1）这种情况无需考虑 2）先执行a,因为是异步的的，b接下来也就执就执行了。所以说在b执行之前先await()， 当b执行完notify它的child(b) countDown()
。如果count为0接下来执行b. 3）而1中是一样的 4）和2的情况一致

tip:小优化 1、把排序后的任务分成异步和同步两个集合 2、先执行异步任务集合 3、在执行同步任务集合










