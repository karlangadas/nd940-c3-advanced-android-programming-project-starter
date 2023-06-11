package com.udacity

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


    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { _, _, new ->
        when (new) {
            ButtonState.Clicked -> {
                // trigger animation
                buttonState = ButtonState.Loading
            }

            ButtonState.Loading -> {
                buttonTextResId = R.string.button_loading
                contentDescription = resources.getString(R.string.button_loading)
            }

            else -> {
                buttonTextResId = R.string.button_download
                contentDescription = resources.getString(R.string.button_download)
            }
        }
        invalidate()
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
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = textColor
        // positioning logic from https://stackoverflow.com/questions/11120392/android-center-text-on-canvas
        val xPos = width / 2f
        val yPos = (height / 2 - (paint.descent() + paint.ascent()) / 2)
        canvas.drawText(
            resources.getString(buttonTextResId),
            xPos,
            yPos,
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

}