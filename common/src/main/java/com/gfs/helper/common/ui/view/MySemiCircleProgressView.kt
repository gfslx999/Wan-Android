package com.gfs.helper.common.ui.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.gfs.helper.common.R
import com.gfs.test.base.util.AppUtil
import com.gfs.test.base.util.LogUtil
import com.gfs.test.base.util.ScreenUtils

/**
 * 半圆进度条 View
 */
class MySemiCircleProgressView : View {

    private lateinit var mContext: Context
    //布局宽高
    private var mWidth = 0
    private var mHeight = 0
    //厚度(线条宽度)
    private var mThickness = 0f
    //顶层进度颜色
    private var mTopProgressColor = 0
    //顶层进度颜色2，设置此颜色可达到渐变效果
    private var mTopProgressSecondColor = 0
    //底层圆圈颜色
    private var mBottomProgressColor = 0
    //上层圆弧终点
    private var mTopProgress = 0f
    //圆弧开口角度
    private var mStartAngle = 180f

    private var mRectF: RectF? = null
    private var mTopProgressPaint: Paint? = null
    private var mBottomProgressPaint: Paint? = null

    constructor(context: Context) : super(context) {
        mContext = context
        initSize(context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initAttr(context, attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        initAttr(context, attributeSet)
    }

    private fun initAttr(context: Context, attributeSet: AttributeSet) {
        mContext = context
        val typedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.MySemiCircleProgressView)

        typedArray.apply {
            mTopProgressColor = getColor(
                R.styleable.MySemiCircleProgressView_customTopSemiProgressColor,
                Color.BLUE
            )
            mTopProgressSecondColor = getColor(
                R.styleable.MySemiCircleProgressView_customTopSecondSemiProgressColor, 0
            )
            mBottomProgressColor =
                getColor(R.styleable.MySemiCircleProgressView_customBottomSemiProgressColor, Color.WHITE)
            mThickness = getDimension(R.styleable.MySemiCircleProgressView_customSemiProgressThickness, 10f)

            recycle()
        }

        initSize(context)
        initPaint()
    }

    /**
     * 更新上层进度条颜色
     *
     * [firstColor] 首个颜色，必须设置
     * [secondColor] 第二个颜色，设置为空的话将仅使用 [firstColor]，不为空使用渐变色
     */
    @JvmOverloads
    fun updateTopProgressFullColor(firstColor: Int?, secondColor: Int? = null) {
        if (mTopProgressColor == firstColor && mTopProgressSecondColor == secondColor) {
            return
        }
        if (firstColor != null && firstColor != 0) {
            mTopProgressColor = firstColor
        }
        mTopProgressSecondColor = secondColor ?: 0
        updateTopProgressColor()
        invalidate()
    }

    /**
     * 初始化画笔
     */
    private fun initPaint() {
        mTopProgressPaint = Paint()
        mBottomProgressPaint = Paint()

        mTopProgressPaint?.apply {
            //是否需要渐变效果
            updateTopProgressColor()

            style = Paint.Style.STROKE
            strokeWidth = mThickness
            isAntiAlias = true
            //设置两端顶点为圆角
            strokeCap = Paint.Cap.ROUND
        }
        mBottomProgressPaint?.apply {
            style = Paint.Style.STROKE
            strokeWidth = mThickness
            isAntiAlias = true
            color = mBottomProgressColor
            //设置两端顶点为圆角
            strokeCap = Paint.Cap.ROUND
        }
    }

    private fun updateTopProgressColor() {
        if (mTopProgressSecondColor != 0) {
            val colorArray = intArrayOf(
                mTopProgressColor,
                mTopProgressSecondColor,
            )
            mTopProgressPaint?.shader = LinearGradient(0f, 0f, 100f, 100f, colorArray, null, Shader.TileMode.CLAMP)
        } else {
            mTopProgressPaint?.color = mTopProgressColor
        }
    }

    private fun initSize(context: Context) {
        mWidth = ScreenUtils.dipToPx(context, 150f).toInt()
        mHeight = ScreenUtils.dipToPx(context, 100f).toInt()
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var width = measureHandler(widthMeasureSpec, mWidth.toInt())
        var height = measureHandler(heightMeasureSpec, mWidth.toInt())
        when {
            widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY -> {
                if (width > height * 2) {
                    width = height * 2
                } else {
                    height = width / 2
                }
            }

            widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY -> {
                height = width / 2
            }

            widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY -> {
                width = height * 2
            }
        }

        mWidth = width
        mHeight = height

        setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY))

        return
        when {
            layoutParams.width == WRAP_CONTENT && layoutParams.height == WRAP_CONTENT -> {
                setMeasuredDimension(mWidth, mHeight)
            }
            layoutParams.width != WRAP_CONTENT && layoutParams.height != WRAP_CONTENT -> {
                mWidth = layoutParams.width
                mHeight = layoutParams.height
                setMeasuredDimension(layoutParams.width, layoutParams.height)
            }
            layoutParams.width == WRAP_CONTENT && layoutParams.height != WRAP_CONTENT -> {
                setMeasuredDimension(mWidth, layoutParams.height)
            }
            layoutParams.width != WRAP_CONTENT && layoutParams.height == WRAP_CONTENT -> {
                setMeasuredDimension(layoutParams.width, mHeight)
            }
        }
        mRectF = RectF(mThickness, mThickness, mWidth - mThickness, mHeight - mThickness)
    }

    private fun measureHandler(measureSpec: Int, defaultSize: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        return when (specMode) {
            MeasureSpec.EXACTLY -> {
                specSize
            }
            MeasureSpec.AT_MOST -> {
                Math.min(defaultSize, specSize)
            }
            else -> {
                defaultSize
            }
        }
    }

    /**
     * 设置顶层进度条颜色
     */
    fun setTopProgressColor(color: Int?) {
        if (color == null) {
            return
        }
        if (mTopProgressColor == color) {
            return
        }
        mTopProgressColor = color
        mTopProgressPaint?.color = mTopProgressColor
        postInvalidate()
    }

    /**
     * 设置底层进度条颜色
     */
    fun setBottomProgressColor(color: Int?) {
        if (color == null) {
            return
        }
        if (mBottomProgressColor == color) {
            return
        }
        mBottomProgressColor = color
        mBottomProgressPaint?.color = mTopProgressColor
        postInvalidate()
    }

    /**
     * 更新进度值
     *
     * 必须在主线程中调用！
     *
     * [percentProgress] 百分比进度，值 0~100，若超出100，将强制指定为 100
     * [useAnimation] 是否使用动画
     */
    fun updateProgress(percentProgress: Float, useAnimation: Boolean) {
        if (!AppUtil.checkIsInMainThread()) {
            Log.e("MyCircleProgressView", "updateProgress.error: must be called on the main thread!")
            return
        }
        if (percentProgress < 0) {
            Log.e("MyCircleProgressView", "updateProgress.error: progress is 0 ~ 100!")
            return
        }
        val usePercentProgress = if (percentProgress <= 100) {
            percentProgress
        } else {
            100f
        }
        // 将百分比值转换为 圆弧所需要的值（0~360）
        var circleProgress = usePercentProgress / 100f * 180
        // 进度为 99 时，再减去2，防止出现看起来粘连的效果
        if (usePercentProgress == 99f) {
            circleProgress -= 2
        }
        if (mTopProgress == circleProgress) {
            return
        }
        if (useAnimation) {
            startAnimation(circleProgress)
        } else {
            mTopProgress = circleProgress
            postInvalidate()
        }
    }

    /**
     * 更新进度，不使用动画
     */
    fun updateProgress(percentProgress: Float) {
        updateProgress(percentProgress, false)
    }

    private fun startAnimation(process: Float) {
        val valueAnimator = ValueAnimator.ofFloat(process)
        valueAnimator.duration = 500
        valueAnimator.start()
        valueAnimator.addUpdateListener {
            try {
                mTopProgress = it.animatedValue as Float
                postInvalidate()
            } catch (e : Exception) {
                mTopProgress = process
            }
        }
    }

    private var mCenterX = 0
    private var mCenterY = 0
    private var mRadius = 0
    private var mArcWidth = 0

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mCenterX = w / 2
        mCenterY = h
        mArcWidth = h / 2

        mRadius = h - mArcWidth - 20
    }
    override fun onDraw(canvas: Canvas?) {

        super.onDraw(canvas)
//        val left = (mCenterX - mRadius - mArcWidth / 2).toFloat()
//        val top = (mCenterY - mRadius - mArcWidth / 2).toFloat()
//        val right = (mCenterX + mRadius + mArcWidth / 2).toFloat()
//        val bottom = (mCenterY - (-mRadius - mArcWidth / 2)).toFloat()
        LogUtil.logI("mHeight: $mHeight, $mCenterY, $mWidth")
        val left = mThickness
        val top = mThickness
        val right = mWidth - mThickness
        val bottom = mHeight * 2f
        mRectF = RectF(
            left, top, right, bottom
        )

        if (canvas == null || mRectF == null) {
            return
        }

        mBottomProgressPaint?.let {
            canvas.drawArc(mRectF!!, mStartAngle, 180f, false, it)
        }
        mTopProgressPaint?.let {
            canvas.drawArc(mRectF!!, mStartAngle, mTopProgress, false, it)
        }
    }

}