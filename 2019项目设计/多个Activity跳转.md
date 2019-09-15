# 多个 Activity 跳转

2019/03/13 23:15

* 参照这个链接[android_定义多个Activity及跳转 - dgdisme - CSDN博客](https://blog.csdn.net/ycwol/article/details/39736075)的指示,基本实现了两个界面的跳转.
* 今天不断的去调试 android 代码,本来想实现一个计算器的功能,但是未能如愿,这周空闲时间需要继续去配置.
* 关于代码,自己发现主要是java中很基本的内容，有理解难度的就是类的继承。另外, android 代码库中,最为常用的就是 `setonclicklistener` 这个函数, 这是检测点击的重要途径. 这个函数往往和类的继承相关。

2019/03/13 23:17 

* 关于带值跳转
  * [Android 多个activity之间的切换（跳转） - u010696144的专栏 - CSDN博客](https://blog.csdn.net/u010696144/article/details/52180328) ；
  * [android_Intent对象初步(Activity传值) - dgdisme - CSDN博客](https://blog.csdn.net/ycwol/article/details/39859341)


2019/03/17 22:16 

* 关于多个 Activity 的跳转。
  * 这篇文章[android同时打开多个Activity的方式(应用于启动页面广告等场景) - 熊大巍搬砖记 - CSDN博客](https://blog.csdn.net/lanfei1027/article/details/50757607)给出了实现跳转的其他方法。

* 多个Button需要多个监听事件。
  * [初学android studio——如何同时监听多个button实现activity跳转 - Timber666的博客 - CSDN博客](https://blog.csdn.net/Timber666/article/details/80142062)
  * 思路就是利用一个函数，通过传递不同的值，调用不同的效果，达到多个Button的效果，这个需要用 switch 语句具体实现。
  * 其中，`ButtonListener` 直接放在 `MainActivity` 下面，`private class` 即可。
  * 经过演示，还存在的问题是按键重合。就是两个 Button 重叠在了一起，导致只用一个起作用并且可见。这个是因为空间的相对位置导致的

2019/03/18 00:08 

* 这个问题经过查询，主要是取决于布局。使用拖拽的方法，默认使用的是 Constraints layout , 但是呢，如果你改成 Linear layout , 并且是 Vertical ，即纵向，就会解决。
* 这里给出了两种布局的代码。

```xml
<!--方法一-->
<RelativeLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
        <Button
            android:id="@+id/previous_music"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_alignParentLeft="true"
            android:background="@drawable/before" />


        <Button
            android:id="@+id/play_music"
            <!--  注意这句-->
            android:layout_toRightOf="@+id//previous_music" 
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:gravity="center_horizontal"
            android:background="@drawable/play" />

</RelativeLayout>

<!--方法二-->
<LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="horizontal" >
<Button
            android:id="@+id/previous_music"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_alignParentLeft="true"
            android:background="@drawable/before" />


        <Button
            android:id="@+id/play_music"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:gravity="center_horizontal"
            android:background="@drawable/play" />

</LinearLayout> 

```

* 下面还有几篇有关的参考文章
  * [Andorid 点击按钮事件叠加时候的事件分析。 - 693的专栏 - CSDN博客](https://blog.csdn.net/luojiusan520/article/details/51241355)
  * [在studio中两个按钮只能显示一个的解决办法 - Vibugs的博客 - CSDN博客](https://blog.csdn.net/Vibugs/article/details/81006690)