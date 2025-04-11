package com.example.smartflowassessment.ui.components

import android.graphics.Color
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.smartflowassessment.utils.ChartType
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

@Composable
fun ChartComponent(
    entries: List<Float>,
    labels: List<String>,
    chartType: ChartType = ChartType.BAR,
    modifier: Modifier = Modifier,
) {
    when (chartType) {
        ChartType.BAR -> {
            AndroidView(
                modifier = modifier.fillMaxWidth().height(500.dp),
                factory = { context ->
                    val barEntries =
                        entries.mapIndexed { index, value ->
                            BarEntry(index.toFloat(), value)
                        }

                    BarChart(context).apply {
                        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 500)

                        val dataSet =
                            BarDataSet(barEntries, "Stock Levels").apply {
                                color = Color.rgb(33, 150, 243)
                                valueTextColor = Color.BLACK
                                valueTextSize = 14f
                            }

                        val barData = BarData(dataSet)
                        data = barData

                        description.apply {
                            text = "Stock per item"
                            textColor = Color.DKGRAY
                        }

                        xAxis.apply {
                            granularity = 1f
                            setDrawGridLines(false)
                            setDrawLabels(true)
                            setDrawAxisLine(true)
                            position = XAxis.XAxisPosition.BOTTOM
                            valueFormatter = IndexAxisValueFormatter(labels)
                        }

                        axisLeft.axisMinimum = 0f
                        axisRight.isEnabled = false
                        legend.isEnabled = false

                        animateY(1000)
                    }
                },
                update = { it.animateY(1000) },
            )
        }

        ChartType.PIE -> {
            AndroidView(
                modifier = modifier.fillMaxWidth().height(500.dp),
                factory = { context ->
                    PieChart(context).apply {
                        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 500)

                        val pieEntries =
                            entries.mapIndexed { index, value ->
                                PieEntry(value, labels.getOrElse(index) { "Category $index" })
                            }
                        setUsePercentValues(true)
                        description.isEnabled = false
                        centerText = "Category Distribution"
                        setCenterTextSize(16f)
                        setDrawEntryLabels(false)

                        legend.apply {
                            verticalAlignment = Legend.LegendVerticalAlignment.TOP
                            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                            orientation = Legend.LegendOrientation.HORIZONTAL
                            setDrawInside(false)
                            xEntrySpace = 7f
                            yEntrySpace = 0f
                            isEnabled = true
                        }

                        val dataSet =
                            PieDataSet(pieEntries, "Categories").apply {
                                val colors =
                                    arrayOf(
                                        Color.rgb(33, 150, 243),
                                        Color.rgb(242, 153, 74),
                                        Color.rgb(76, 175, 80),
                                        Color.rgb(233, 30, 99),
                                        Color.rgb(156, 39, 176),
                                        Color.rgb(255, 193, 7),
                                        Color.rgb(0, 188, 212),
                                    )

                                this.colors = pieEntries.indices.map { colors[it % colors.size] }
                                valueTextColor = Color.WHITE
                                valueTextSize = 14f
                                sliceSpace = 3f

                                valueFormatter =
                                    object : ValueFormatter() {
                                        override fun getFormattedValue(value: Float): String = String.format("%.1f%%", value)
                                    }
                            }

                        val pieData = PieData(dataSet)
                        data = pieData

                        animateY(1000, Easing.EaseInOutQuad)
                    }
                },
                update = { chart ->
                    chart.animateY(1000, Easing.EaseInOutQuad)
                },
            )
        }
    }
}
