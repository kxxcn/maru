package dev.kxxcn.maru.view.custom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.os.Bundle
import android.os.Parcelable
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.extension.dpToPx
import dev.kxxcn.maru.util.extension.spToPx
import java.text.DecimalFormat
import kotlin.math.cos

class ArcProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val defaultFinishedColor = Color.WHITE
    private val defaultUnfinishedColor = Color.rgb(72, 106, 176)
    private val defaultTextColor = Color.rgb(66, 145, 241)
    private val defaultSuffixTextSize: Float
    private val defaultSuffixPadding: Float
    private val defaultBottomTextSize: Float
    private val defaultStrokeWidth: Float
    private val defaultSuffixText: String
    private val defaultMax = 100
    private val defaultArcAngle = 360 * 0.8f
    private val minSize = 100f.dpToPx.toInt()

    private val rectF = RectF()
    private var strokeWidth = 0f
    private var suffixTextSize = 0f
    private var bottomTextSize = 0f
    private var bottomText: String? = null
    private var text: String? = null
    private var textSize = 0f
    private var textColor = 0
    private var progress = 0f

    private var finishedStrokeColor = 0
    private var unfinishedStrokeColor = 0
    private var arcAngle = 0f
    private var suffixText: String? = "%"
    private var suffixTextPadding = 0f
    private var arcBottomHeight = 0f
    private var defaultTextSize = 18f.spToPx

    private lateinit var textPaint: Paint
    private lateinit var paint: Paint

    private val typeface = Typeface.createFromAsset(context.assets, "nixgon.ttf")

    private var max = 0
        set(max) {
            if (max > 0) {
                field = max
                invalidate()
            }
        }

    private fun initByAttributes(attributes: TypedArray) {
        finishedStrokeColor =
            attributes.getColor(R.styleable.ArcProgress_arc_finished_color, defaultFinishedColor)
        unfinishedStrokeColor = attributes.getColor(
            R.styleable.ArcProgress_arc_unfinished_color,
            defaultUnfinishedColor
        )
        textColor = attributes.getColor(R.styleable.ArcProgress_arc_text_color, defaultTextColor)
        textSize = attributes.getDimension(R.styleable.ArcProgress_arc_text_size, defaultTextSize)
        arcAngle = attributes.getFloat(R.styleable.ArcProgress_arc_angle, defaultArcAngle)
        max = attributes.getInt(R.styleable.ArcProgress_arc_max, defaultMax)
        setProgress(attributes.getFloat(R.styleable.ArcProgress_arc_progress, 0f))
        strokeWidth =
            attributes.getDimension(R.styleable.ArcProgress_arc_stroke_width, defaultStrokeWidth)
        suffixTextSize = attributes.getDimension(
            R.styleable.ArcProgress_arc_suffix_text_size,
            defaultSuffixTextSize
        )
        suffixText =
            if (TextUtils.isEmpty(attributes.getString(R.styleable.ArcProgress_arc_suffix_text))) defaultSuffixText else attributes.getString(
                R.styleable.ArcProgress_arc_suffix_text
            )
        suffixTextPadding = attributes.getDimension(
            R.styleable.ArcProgress_arc_suffix_text_padding,
            defaultSuffixPadding
        )
        bottomTextSize = attributes.getDimension(
            R.styleable.ArcProgress_arc_bottom_text_size,
            defaultBottomTextSize
        )
        bottomText = attributes.getString(R.styleable.ArcProgress_arc_bottom_text)
    }

    private fun initPainters() {
        textPaint = TextPaint().apply {
            color = this@ArcProgressView.textColor
            textSize = this@ArcProgressView.textSize
            isAntiAlias = true
            typeface = this@ArcProgressView.typeface
        }
        paint = Paint().apply {
            color = defaultUnfinishedColor
            isAntiAlias = true
            strokeWidth = this@ArcProgressView.strokeWidth
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            pathEffect = DashPathEffect(floatArrayOf(300f, 45f), 0f)
        }
    }

    override fun invalidate() {
        initPainters()
        super.invalidate()
    }

    fun getStrokeWidth(): Float {
        return strokeWidth
    }

    fun setStrokeWidth(strokeWidth: Float) {
        this.strokeWidth = strokeWidth
        this.invalidate()
    }

    private fun getSuffixTextSize(): Float {
        return suffixTextSize
    }

    private fun setSuffixTextSize(suffixTextSize: Float) {
        this.suffixTextSize = suffixTextSize
        this.invalidate()
    }

    private fun getBottomText(): String? {
        return bottomText
    }

    private fun setBottomText(bottomText: String?) {
        this.bottomText = bottomText
        this.invalidate()
    }

    private fun getProgress(): Float {
        return progress
    }

    fun setProgress(progress: Float) {
        this.progress = DecimalFormat("#.##").format(progress.toDouble()).toFloat()
        if (this.progress > max) {
            this.progress %= max.toFloat()
        }
        invalidate()
    }

    fun getBottomTextSize(): Float {
        return bottomTextSize
    }

    private fun setBottomTextSize(bottomTextSize: Float) {
        this.bottomTextSize = bottomTextSize
        this.invalidate()
    }

    private fun getText(): String? {
        return text
    }

    /**
     * Setting Central Text to custom String
     */
    private fun setText(text: String?) {
        this.text = text
        this.invalidate()
    }

    /**
     * Setting Central Text back to default one (value of the progress)
     */
    private fun setDefaultText() {
        text = getProgress().toString()
        invalidate()
    }

    private fun getTextSize(): Float {
        return textSize
    }

    private fun setTextSize(textSize: Float) {
        this.textSize = textSize
        this.invalidate()
    }

    private fun getTextColor(): Int {
        return textColor
    }

    private fun setTextColor(textColor: Int) {
        this.textColor = textColor
        this.invalidate()
    }

    private fun getFinishedStrokeColor(): Int {
        return finishedStrokeColor
    }

    private fun setFinishedStrokeColor(finishedStrokeColor: Int) {
        this.finishedStrokeColor = finishedStrokeColor
        this.invalidate()
    }

    private fun getUnfinishedStrokeColor(): Int {
        return unfinishedStrokeColor
    }

    private fun setUnfinishedStrokeColor(unfinishedStrokeColor: Int) {
        this.unfinishedStrokeColor = unfinishedStrokeColor
        this.invalidate()
    }

    private fun getArcAngle(): Float {
        return arcAngle
    }

    private fun setArcAngle(arcAngle: Float) {
        this.arcAngle = arcAngle
        this.invalidate()
    }

    private fun getSuffixText(): String? {
        return suffixText
    }

    private fun setSuffixText(suffixText: String?) {
        this.suffixText = suffixText
        this.invalidate()
    }

    private fun getSuffixTextPadding(): Float {
        return suffixTextPadding
    }

    private fun setSuffixTextPadding(suffixTextPadding: Float) {
        this.suffixTextPadding = suffixTextPadding
        this.invalidate()
    }

    override fun getSuggestedMinimumHeight(): Int {
        return minSize
    }

    override fun getSuggestedMinimumWidth(): Int {
        return minSize
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        rectF[strokeWidth / 2f, strokeWidth / 2f, width - strokeWidth / 2f] =
            MeasureSpec.getSize(heightMeasureSpec) - strokeWidth / 2f
        val radius = width / 2f
        val angle = (360 - arcAngle) / 2f
        arcBottomHeight =
            radius * (1 - cos(angle / 180 * Math.PI)).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val startAngle = 270 - arcAngle / 2f
        val finishedSweepAngle = progress / max.toFloat() * arcAngle
        var finishedStartAngle = startAngle
        if (progress == 0f) finishedStartAngle = 0.01f

        paint.color = unfinishedStrokeColor
        canvas.drawArc(rectF, startAngle, arcAngle, false, paint)

        paint.color = finishedStrokeColor
        canvas.drawArc(rectF, finishedStartAngle, finishedSweepAngle, false, paint)

        text = "${getProgress().toInt()}%"

        if (!TextUtils.isEmpty(text)) {
            textPaint.color = textColor
            textPaint.textSize = textSize
            canvas.drawText(
                text!!,
                (width - textPaint.measureText(text)) / 2.0f,
                height.toFloat() - 25,
                textPaint
            )
        }

        if (arcBottomHeight == 0f) {
            val radius = width / 2f
            val angle = (360 - arcAngle) / 2f
            arcBottomHeight =
                radius * (1 - cos(angle / 180 * Math.PI)).toFloat()
        }

        if (!TextUtils.isEmpty(getBottomText())) {
            textPaint.textSize = bottomTextSize
            val bottomTextBaseline =
                height - arcBottomHeight - (textPaint.descent() + textPaint.ascent()) / 2
            canvas.drawText(
                getBottomText()!!,
                (width - textPaint.measureText(getBottomText())) / 2.0f,
                bottomTextBaseline,
                textPaint
            )
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState())
        bundle.putFloat(INSTANCE_STROKE_WIDTH, getStrokeWidth())
        bundle.putFloat(INSTANCE_SUFFIX_TEXT_SIZE, getSuffixTextSize())
        bundle.putFloat(INSTANCE_SUFFIX_TEXT_PADDING, getSuffixTextPadding())
        bundle.putFloat(INSTANCE_BOTTOM_TEXT_SIZE, getBottomTextSize())
        bundle.putString(INSTANCE_BOTTOM_TEXT, getBottomText())
        bundle.putFloat(INSTANCE_TEXT_SIZE, getTextSize())
        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor())
        bundle.putFloat(INSTANCE_PROGRESS, getProgress())
        bundle.putInt(INSTANCE_MAX, max)
        bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, getFinishedStrokeColor())
        bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR, getUnfinishedStrokeColor())
        bundle.putFloat(INSTANCE_ARC_ANGLE, getArcAngle())
        bundle.putString(INSTANCE_SUFFIX, getSuffixText())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            val bundle = state
            strokeWidth = bundle.getFloat(INSTANCE_STROKE_WIDTH)
            suffixTextSize = bundle.getFloat(INSTANCE_SUFFIX_TEXT_SIZE)
            suffixTextPadding = bundle.getFloat(INSTANCE_SUFFIX_TEXT_PADDING)
            bottomTextSize = bundle.getFloat(INSTANCE_BOTTOM_TEXT_SIZE)
            bottomText = bundle.getString(INSTANCE_BOTTOM_TEXT)
            textSize = bundle.getFloat(INSTANCE_TEXT_SIZE)
            textColor = bundle.getInt(INSTANCE_TEXT_COLOR)
            max = bundle.getInt(INSTANCE_MAX)
            setProgress(bundle.getFloat(INSTANCE_PROGRESS))
            finishedStrokeColor = bundle.getInt(INSTANCE_FINISHED_STROKE_COLOR)
            unfinishedStrokeColor = bundle.getInt(INSTANCE_UNFINISHED_STROKE_COLOR)
            suffixText = bundle.getString(INSTANCE_SUFFIX)
            initPainters()
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE))
            return
        }
        super.onRestoreInstanceState(state)
    }

    companion object {
        private const val INSTANCE_STATE = "saved_instance"
        private const val INSTANCE_STROKE_WIDTH = "stroke_width"
        private const val INSTANCE_SUFFIX_TEXT_SIZE = "suffix_text_size"
        private const val INSTANCE_SUFFIX_TEXT_PADDING = "suffix_text_padding"
        private const val INSTANCE_BOTTOM_TEXT_SIZE = "bottom_text_size"
        private const val INSTANCE_BOTTOM_TEXT = "bottom_text"
        private const val INSTANCE_TEXT_SIZE = "text_size"
        private const val INSTANCE_TEXT_COLOR = "text_color"
        private const val INSTANCE_PROGRESS = "progress"
        private const val INSTANCE_MAX = "max"
        private const val INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color"
        private const val INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color"
        private const val INSTANCE_ARC_ANGLE = "arc_angle"
        private const val INSTANCE_SUFFIX = "suffix"
    }

    init {
        defaultTextSize = 40f.spToPx
        defaultSuffixTextSize = 15f.spToPx
        defaultSuffixPadding = 4f.dpToPx
        defaultSuffixText = "%"
        defaultBottomTextSize = 10f.spToPx
        defaultStrokeWidth = 6f.dpToPx
        val attributes = context.theme
            .obtainStyledAttributes(
                attrs,
                R.styleable.ArcProgress,
                defStyleAttr,
                0
            )
        initByAttributes(attributes)
        attributes.recycle()
        initPainters()
    }
}
