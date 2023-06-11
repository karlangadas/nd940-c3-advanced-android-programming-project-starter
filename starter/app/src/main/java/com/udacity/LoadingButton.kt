package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates
import android.graphics.RectF
import android.util.Log
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import kotlinx.android.synthetic.main.content_detail.view.status_description

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonTextResId = R.string.button_download

    private var downloadBackgroundColor = 0
    private var downloadingBackgroundColor = 0
    private var textColor = 0
    private var downloadingCircleBackgroundColor = 0

    private var textXPos = 0f
    private var textYPos = 0f
    private val circleRect: RectF = RectF(0f, 0f, 0f, 0f)
    private val animDuration = 3000L
    private var loadingAngle = 0f
    private val
            circleAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
        duration = animDuration
        interpolator = AccelerateInterpolator(1f)
        addUpdateListener {
            loadingAngle = animatedValue as Float
            invalidate()
        }
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { _, _, new ->
        when (new) {
            ButtonState.Clicked -> {
                // trigger animation
                buttonState = ButtonState.Loading
            }

            ButtonState.Loading -> {
                buttonTextResId = R.string.button_loading
                contentDescription = resources.getString(R.string.button_loading)
                startAnimation()
            }

            ButtonState.Completed -> {
                buttonTextResId = R.string.button_download
                contentDescription = resources.getString(R.string.button_download)
            }
        }
        invalidate()
    }

    private fun startAnimation() {
        circleAnimator?.cancel()
        circleAnimator?.start()
    }


    init {
        buttonState = ButtonState.Completed
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            downloadBackgroundColor = getColor(R.styleable.LoadingButton_downloadBackgroundColor, 0)
            downloadingBackgroundColor =
                getColor(R.styleable.LoadingButton_downloadingBackgroundColor, 0)
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
            downloadingCircleBackgroundColor =
                getColor(R.styleable.LoadingButton_downloadingCircleBackgroundColor, 0)
        }
        setBackgroundColor(downloadBackgroundColor)
        circleAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                loadingAngle = 0f
                buttonState = buttonState.next()
            }
        })
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = textColor
        canvas.drawText(
            resources.getString(buttonTextResId),
            textXPos,
            textYPos,
            paint
        )

        paint.color = downloadingCircleBackgroundColor
        canvas.drawArc(
            circleRect,
            0f,
            loadingAngle,
            true,
            paint
        )
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.NORMAL)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun performClick(): Boolean {
        if (!super.performClick()) return false
        buttonState = buttonState.next()
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // positioning logic from https://stackoverflow.com/questions/11120392/android-center-text-on-canvas
        textXPos = width / 2f
        textYPos = (height / 2 - (paint.descent() + paint.ascent()) / 2)
        val circleXPos = width * 5f / 7f
        val circleYPos = height / 3f
        val circleDiameter = height / 3f
        circleRect.set(
            circleXPos,
            circleYPos,
            circleXPos + circleDiameter,
            circleYPos + circleDiameter
        )
    }
}