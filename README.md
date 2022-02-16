# VirtualNumericKeyboard

虚拟数字键盘
=======

[![](https://jitpack.io/v/FPhoenixCorneaE/VirtualNumericKeyboard.svg)](https://jitpack.io/#FPhoenixCorneaE/VirtualNumericKeyboard)


<div align="center">
    <img src="https://github.com/FPhoenixCorneaE/VirtualNumericKeyboard/blob/main/screenshot/virtual-numeric-keyboard-1.png" width="200" align="top"/>
	<img src="https://github.com/FPhoenixCorneaE/VirtualNumericKeyboard/blob/main/screenshot/virtual-numeric-keyboard-2.png" width="200" align="top" style="margin-left:100px"/>
</div>


-----------------------------------------


How to include it in your project:
--------------
**Step 1.** Add the JitPack repository to your build file

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

**Step 2.** Add the dependency

```groovy
dependencies {
    implementation 'com.github.FPhoenixCorneaE:VirtualNumericKeyboard:latest'
}
```

How to use:
--------------

**1. xml中添加控件**
```xml

<com.fphoenixcorneae.widget.keyboard.VirtualNumericKeyboard android:id="@+id/rvVirtualNumeric"
    android:layout_width="match_parent" android:layout_height="wrap_content" android:layout_marginStart="32dp"
    android:layout_marginEnd="32dp" app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintTop_toTopOf="parent" />
```

**2. 代码中设置属性**
```kotlin
mRvVirtualNumeric?.apply {
    // 按键高度
    keyHeight = 40f
    // 按键文本大小
    keyTextSize = 20f
    // 按键背景圆角大小
    keyBgCornerRadius = 8f
    // 按键背景正常颜色
    keyBgNormal = Color.LTGRAY
    // 按键背景按压颜色
    keyBgPressed = Color.DKGRAY
    // 按键背景选择颜色
    keyBgSelected = Color.BLUE
    // 可输入最大数字个数
    maxNumericCount = 6
    // 按键之间的空隙大小
    horizontalSpace = 12f
    verticalSpace = 12f
    // 设置数字变化监听
    setOnNumericChangedListener { result, reachedMaxInputCount ->
        mTvResult?.text = Editable.Factory().newEditable(result)
        mBtnConfirm?.isEnabled = reachedMaxInputCount
    }
}
```

```kotlin
// 清空数字
mRvVirtualNumeric?.clearNumeric()
```