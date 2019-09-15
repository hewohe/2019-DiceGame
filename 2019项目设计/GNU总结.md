# GUI总结

## 控件参数

1. BUTTON
  * style: (NO.2) Widget.AppCompat.Button.Borderless
  * text appearence : Widget.PopupMenu.Large
  * Textsize：36sp

2. TEXTVIEW
  * style: Holo.Widget.TextView
  * Textsize：36sp

## 中英文切换

1. 关于使用 locale 方法实现还是存在困难；
  * 参考[Android 中英文切换（点击按钮切换语言） - yijiaodingqiankun的博客 - CSDN博客](https://blog.csdn.net/yijiaodingqiankun/article/details/80910470)
  * 最后强调一下 strings.xml 文件的写法。这个不是使用的 ID ，是用的界面上的显示的字符就可以。参考[Android 多语言适配 - 简书](https://www.jianshu.com/p/0243b5915a61)
  * 尽管自己到最后还是没有实现使用 `Locale` 方法实现切换，但是使用 text 的解决思路仍然有可取之处

## 增加字符跳动效果

1. 中文字体增加字体颜色变化，英文字体实现增加点的跳动和第一个单词的跳动；参考[Android-利用LinearGradient实现文字一闪一闪 - 七号座先生的博客 - CSDN博客](https://blog.csdn.net/zouzhigang96/article/details/50559914)
2. 这个有点不符合我的预期，这个可以修改显示的内容，尽管强大，但是呢，现在用不上。我决定使用线性效果[GitHub - hanks-zyh/HTextView: Animation effects to text, not really textview](https://github.com/hanks-zyh/HTextView)

## 添加掷色子的动画图

1. 参考链接[Android 自定义View之3D骰子旋转 - 简书](https://www.jianshu.com/p/20d0cb50229c)

## 字体跳动

1. 参考链接 [Android实现TextView字符串波浪式跳动 - Zhang Phil - CSDN博客](https://blog.csdn.net/zhangphil/article/details/49836089)

2. 这篇博客里有 Github 链接，先参照 Github 中的方法修改两个 Gradle 文件，添加代码；

3. Github 中给出了最后要使用 .stopJump() 的方法结束代码；这个为了保证效果，还是不要加了；

4. 这个提供了两个方法；一个是添加跳动的三个点，还有是第一个字的跳动；第一次使用的时候这两个都使用了，但是 APP 直接无响应

5. 这个是因为调用第一个字的跳动的时候，是检测空格的，所以必须要有空格；否则会因为检测不到而报错；

## 隐藏下方的导航栏

* 在mainfest.xml中添加这行代码；
```android
android:theme="@style/Theme.AppCompat.NoActionBar"
```

## Dialog 设计

1. 参考链接[android 8种对话框（Dialog）使用方法汇总 - 呆尐兔兔 - 博客园](https://www.cnblogs.com/gzdaijie/p/5222191.html) 这个链接的内容是子函数的实现，也有很多功能；

2. 这个[Android常见的三种弹框 - qq_35698774的博客 - CSDN博客](https://blog.csdn.net/qq_35698774/article/details/79779238)是对于他的函数做了一下讲解，这个是直接在函数中使用，没有封装；
3. 另外就是一般点击按钮之后使用的是 Toast ，自己也是使用的这个方法；

## 定时器

* 定时器的讲解和例程[Android Timer和TimerTask - 星辰之力 - 博客园](https://www.cnblogs.com/zhujiabin/p/4212806.html)
