package com.fphoenixcorneae.widget.keyboard

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

typealias OnNumericChangedListener = (result: String, reachedMaxInputCount: Boolean) -> Unit

/**
 * @desc：虚拟数字键盘
 * @date：2022/02/14 11:30
 */
class VirtualNumericKeyboard @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(mContext, attrs, defStyleAttr) {

    /**
     * 按键高度
     */
    var keyHeight: Float = 21f

    /**
     * 按键文本大小
     */
    var keyTextSize: Float = 11f

    /**
     * 按键背景：正常颜色、按压颜色、选择颜色、圆角大小
     */
    @ColorInt
    var keyBgNormal: Int = Color.parseColor("#FF92A1AD")

    @ColorInt
    var keyBgPressed: Int = Color.parseColor("#FF596670")

    @ColorInt
    var keyBgSelected: Int = Color.parseColor("#FF596670")

    var keyBgCornerRadius: Float = 1f

    /**
     * 按键之间的空隙大小
     */
    var horizontalSpace: Float = 4f
    var verticalSpace: Float = 4f

    /**
     * 可输入最大数字个数
     */
    var maxNumericCount = MAX_NUMERIC_COUNT

    private val mNumerics = listOf("1", "2", "3", "0", "4", "5", "6", "7", "8", "9")
    private val mNumericsChecked = mutableListOf<String>()
    private var mInputResult = ""
    private var mOnNumericChangedListener: OnNumericChangedListener? = null

    init {
        isNestedScrollingEnabled = false
        overScrollMode = OVER_SCROLL_NEVER
        setHasFixedSize(true)
        layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        adapter = object : RecyclerView.Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return object : ViewHolder(if (viewType == ITEM_VIEW_TYPE_DELETE) {
                    // 删除按键
                    FrameLayout(mContext).apply {
                        layoutParams = LayoutParams(-1, (keyHeight * 2 - verticalSpace).dp())
                        background = getKeyBackground()
                        addView(ImageView(mContext).apply {
                            layoutParams = FrameLayout.LayoutParams(keyTextSize.dp() * 4, keyTextSize.dp() * 2).apply {
                                gravity = Gravity.CENTER
                            }
                        })
                    }
                } else {
                    // 数字按键
                    TextView(mContext).apply {
                        layoutParams = LayoutParams(-1, keyHeight.dp())
                        gravity = Gravity.CENTER
                        background = getKeyBackground()
                        textSize = keyTextSize
                        setTextColor(Color.WHITE)
                        includeFontPadding = false
                    }
                }) {}
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                when (holder.itemView) {
                    is TextView -> {
                        (holder.itemView as TextView).apply {
                            val tempPosition = if (position > 7) {
                                position - 1
                            } else {
                                position
                            }
                            isSelected = mNumericsChecked.contains(mNumerics[tempPosition])
                            text = mNumerics[tempPosition]
                            (layoutParams as MarginLayoutParams).apply {
                                when (tempPosition) {
                                    3 -> {
                                        leftMargin = 0
                                        topMargin = 0
                                        rightMargin = 0
                                        bottomMargin = verticalSpace.dp()
                                    }
                                    else -> {
                                        leftMargin = 0
                                        topMargin = 0
                                        rightMargin = horizontalSpace.dp()
                                        bottomMargin = verticalSpace.dp()
                                    }
                                }
                            }
                            // 数字按键点击
                            setOnClickListener {
                                if (mInputResult.length < maxNumericCount) {
                                    mInputResult = mInputResult.plus(mNumerics[tempPosition])
                                    it.isSelected = true
                                    mNumericsChecked.add(mNumerics[tempPosition])
                                }
                                mOnNumericChangedListener?.invoke(
                                    mInputResult,
                                    mInputResult.length == maxNumericCount
                                )
                            }
                        }
                    }
                    is ViewGroup -> {
                        ((holder.itemView as ViewGroup).getChildAt(0) as ImageView).setImageResource(R.drawable.vnk_ic_delete)
                        // 删除按键点击
                        holder.itemView.setOnClickListener {
                            if (mInputResult.isNotEmpty()) {
                                val deleteNumeric =
                                    mInputResult.subSequence(mInputResult.length - 1, mInputResult.length)
                                mNumerics.forEachIndexed { index, s ->
                                    if (deleteNumeric == s) {
                                        mNumericsChecked.removeLast()
                                        val tempIndex = if (index >= 7) {
                                            index + 1
                                        } else {
                                            index
                                        }
                                        notifyItemChanged(tempIndex)
                                    }
                                }
                                mInputResult = mInputResult.subSequence(0, mInputResult.length - 1).toString()
                            }
                            mOnNumericChangedListener?.invoke(mInputResult, false)
                        }
                    }
                }
            }

            override fun getItemCount(): Int {
                return mNumerics.size + 1
            }

            override fun getItemViewType(position: Int): Int {
                return if (position == 7) {
                    ITEM_VIEW_TYPE_DELETE
                } else {
                    0
                }
            }
        }
    }

    private fun getKeyBackground(): GradientDrawable {
        return GradientDrawable().apply {
            color = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_pressed, -android.R.attr.state_selected),
                    intArrayOf(android.R.attr.state_pressed),
                    intArrayOf(android.R.attr.state_selected)
                ),
                intArrayOf(
                    keyBgNormal,
                    keyBgPressed,
                    keyBgSelected
                )
            )
            cornerRadius = keyBgCornerRadius
        }
    }

    /**
     * 设置数字变化监听
     */
    fun setOnNumericChangedListener(onNumericChangedListener: OnNumericChangedListener) {
        this.mOnNumericChangedListener = onNumericChangedListener
    }

    /**
     * 清空数字
     */
    fun clearNumeric() {
        mNumericsChecked.clear()
        adapter?.let {
            it.notifyItemRangeChanged(0, it.itemCount)
        }
        mInputResult = ""
        mOnNumericChangedListener?.invoke(mInputResult, false)
    }

    /**
     * dp to px
     */
    private fun Float.dp(): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

    companion object {
        private const val MAX_NUMERIC_COUNT = 4
        private const val ITEM_VIEW_TYPE_DELETE = 1
    }
}