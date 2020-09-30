package dev.kxxcn.maru.util

import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

class PieChartFormatter(private val pieChart: PieChart) : ValueFormatter() {

    private val numberFormat = NumberFormat.getInstance(Locale.KOREA)

    override fun getFormattedValue(value: Float): String {
        return if (value.isNaN()) "0%" else "${value.roundToInt()}%"
    }

    override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
        return if (pieChart.isUsePercentValuesEnabled) {
            getFormattedValue(value)
        } else {
            numberFormat.format(value)
        }
    }
}
