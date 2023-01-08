package com.gfs.test.base.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.gfs.test.base.R
import com.gfs.test.base.databinding.BaseActionBarViewBinding
import com.gfs.test.base.util.StatusBarUtil
import com.gfs.test.base.util.ToastUtil

/**
 * 顶部导航栏 View
 */
class MyActionBarView : ConstraintLayout {

    private val mContext: Context
    private var mTitle: String
    private var isShowBackIcon: Boolean = true
    private var mOnClickBackListener: OnClickListener? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        mContext = context

        mTitle = mContext.getString(R.string.base_app_name)
        initAttribute(attributeSet)
        initActionBarView()
    }

    private fun initAttribute(attributeSet: AttributeSet?) {
        if (attributeSet == null) {
            return
        }
        val typedArray =
            mContext.obtainStyledAttributes(attributeSet, R.styleable.MyActionBarView)

        for (i in 0 until typedArray.indexCount) {
            when (val attr = typedArray.getIndex(i)) {
                R.styleable.MyActionBarView_actionBarTitle -> {
                    val assignTitle = typedArray.getString(attr)
                    if (!assignTitle.isNullOrEmpty()) {
                        mTitle = assignTitle
                    }
                }
                R.styleable.MyActionBarView_actionBarShowBackIcon -> {
                    isShowBackIcon = typedArray.getBoolean(attr, isShowBackIcon)
                }
            }
        }

        typedArray.recycle()
    }

    private fun initActionBarView() {
        val inflate = LayoutInflater.from(mContext).inflate(R.layout.base_action_bar_view, this, true)
        val binding = BaseActionBarViewBinding.bind(inflate)

        if (mContext is FragmentActivity) {
            StatusBarUtil.setTransparentForImageView(mContext)
            val statusBarHeight = StatusBarUtil.getStatusBarHeight(mContext)
            binding.root.post {
                val layoutParams: ConstraintLayout.LayoutParams =
                    binding.tvTitle.layoutParams as LayoutParams
                layoutParams.setMargins(
                    layoutParams.leftMargin,
                    layoutParams.topMargin + statusBarHeight,
                    layoutParams.rightMargin,
                    layoutParams.bottomMargin
                )
                binding.tvTitle.layoutParams = layoutParams

                val rootLayoutParams = binding.innerIvBackground.layoutParams
                rootLayoutParams.height += statusBarHeight
                binding.innerIvBackground.layoutParams = rootLayoutParams
            }
        }

        binding.tvTitle.text = mTitle
        binding.ivBack.visibility = if (isShowBackIcon) {
            binding.ivBack.setOnClickListener {
                // 如果用户设置了点击事件，则回调出去，否则尝试结束当前页面
                if (mOnClickBackListener != null) {
                    mOnClickBackListener!!.onClick(it)
                } else if (mContext is FragmentActivity) {
                    mContext.finish()
                }
            }

            View.VISIBLE
        } else {
            View.GONE
        }
    }

    /**
     * 点击【返回】按钮时的回调
     */
    fun setOnClickBackListener(onClickListener: OnClickListener) {
        mOnClickBackListener = onClickListener
    }

}