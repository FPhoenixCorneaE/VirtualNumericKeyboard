package com.example.virtualnumerickeyboard

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fphoenixcorneae.widget.keyboard.VirtualNumericKeyboard

class MainActivity : AppCompatActivity() {

    private var mTvResult: TextView? = null
    private var mRvVirtualNumeric: VirtualNumericKeyboard? = null
    private var mBtnConfirm: Button? = null
    private var mBtnClear: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTvResult = findViewById(R.id.tvResult)
        mRvVirtualNumeric = findViewById(R.id.rvVirtualNumeric)
        mBtnConfirm = findViewById(R.id.btnConfirm)
        mBtnClear = findViewById(R.id.btnClear)
        mBtnClear?.setOnClickListener {
            // 清空数字
            mRvVirtualNumeric?.clearNumeric()
        }
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
    }
}