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
            mRvVirtualNumeric?.clearNumeric()
        }
        mRvVirtualNumeric?.apply {
            keyHeight = 40f
            keyTextSize = 20f
            keyBgCornerRadius = 8f
            keyBgNormal = Color.LTGRAY
            keyBgPressed = Color.DKGRAY
            keyBgSelected = Color.BLUE
            maxNumericCount = 6
            horizontalSpace = 12f
            verticalSpace = 12f
            setOnNumericChangedListener { result, reachedMaxInputCount ->
                mTvResult?.text = Editable.Factory().newEditable(result)
                mBtnConfirm?.isEnabled = reachedMaxInputCount
            }
        }
    }
}