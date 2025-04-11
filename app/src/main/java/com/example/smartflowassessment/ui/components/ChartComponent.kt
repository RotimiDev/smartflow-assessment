package com.example.smartflowassessment.ui.components

import android.graphics.Color
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.smartflowassessment.utils.ChartType
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
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
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        when (chartType) {
            ChartType.BAR -> {
                AndroidView(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    factory = { context ->
                        val barEntries =
                            entries.mapIndexed { index, value ->
                                BarEntry(index.toFloat(), value)
                            }

                        BarChart(context).apply {
                            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)

                            val dataSet =
                                BarDataSet(barEntries, "Stock Levels").apply {
                                    color = Color.rgb(33, 150, 243)
                                    valueTextColor = Color.BLACK
                                    valueTextSize = 12f
                                }

                            val barData = BarData(dataSet)
                            barData.barWidth = 0.6f
                            data = barData

                            description.apply {
                                text = ""
                                textColor = Color.DKGRAY
                            }

                            xAxis.apply {
                                granularity = 1f
                                setDrawGridLines(false)
                                setDrawLabels(true)
                                setDrawAxisLine(true)
                                position = XAxis.XAxisPosition.BOTTOM
                                valueFormatter = IndexAxisValueFormatter(labels)
                                labelRotationAngle =
                                    if (labels.size > 6) 45f else 0f
                            }

                            axisLeft.apply {
                                axisMinimum = 0f
                                granularity = 5f
                                setDrawGridLines(true)
                                setDrawAxisLine(true)
                            }

                            axisRight.isEnabled = false
                            legend.isEnabled = true

                            animateY(1000)
                            invalidate()
                        }
                    },
                    update = { chart ->
                        val barEntries =
                            entries.mapIndexed { index, value ->
                                BarEntry(index.toFloat(), value)
                            }

                        val dataSet =
                            BarDataSet(barEntries, "Stock Levels").apply {
                                color = Color.rgb(33, 150, 243)
                                valueTextColor = Color.BLACK
                                valueTextSize = 12f
                            }

                        val barData = BarData(dataSet)
                        barData.barWidth = 0.6f
                        chart.data = barData
                        chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                        chart.xAxis.labelRotationAngle = if (labels.size > 6) 45f else 0f
                        chart.animateY(1000)
                        chart.invalidate()
                    },
                )
            }

            ChartType.PIE -> {
                AndroidView(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    factory = { context ->
                        PieChart(context).apply {
                            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)

                            val pieEntries =
                                entries.mapIndexed { index, value ->
                                    PieEntry(value, labels.getOrElse(index) { "Category $index" })
                                }
                            setUsePercentValues(true)
                            description.isEnabled = false
                            centerText = "Distribution"
                            setCenterTextSize(16f)
                            setDrawEntryLabels(false)

                            legend.apply {
                                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                                orientation = Legend.LegendOrientation.HORIZONTAL
                                setDrawInside(false)
                                xEntrySpace = 7f
                                yEntrySpace = 0f
                                isEnabled = true
                                textSize = 10f
                            }

                            val dataSet =
                                PieDataSet(pieEntries, "").apply {
                                    val colors =
                                        arrayOf(
                                            Color.rgb(33, 150, 243),
                                            Color.rgb(242, 153, 74),
                                            Color.rgb(76, 175, 80),
                                            Color.rgb(233, 30, 99),
                                            Color.rgb(156, 39, 176),
                                            Color.rgb(255, 193, 7),
                                            Color.rgb(0, 188, 212),
                                            Color.rgb(255, 87, 34),
                                            Color.rgb(3, 169, 244),
                                            Color.rgb(139, 195, 74),
                                        )

                                    this.colors =
                                        pieEntries.indices.map { colors[it % colors.size] }
                                    valueTextColor = Color.WHITE
                                    valueTextSize = 12f
                                    sliceSpace = 3f

                                    valueFormatter =
                                        object : ValueFormatter() {
                                            override fun getFormattedValue(value: Float): String = String.format("%.1f%%", value)
                                        }
                                }

                            val pieData = PieData(dataSet)
                            data = pieData

                            animateY(1000, Easing.EaseInOutQuad)
                            invalidate()
                        }
                    },
                    update = { chart ->
                        val pieEntries =
                            entries.mapIndexed { index, value ->
                                PieEntry(value, labels.getOrElse(index) { "Category $index" })
                            }

                        val dataSet =
                            PieDataSet(pieEntries, "").apply {
                                val colors =
                                    arrayOf(
                                        Color.rgb(33, 150, 243),
                                        Color.rgb(242, 153, 74),
                                        Color.rgb(76, 175, 80),
                                        Color.rgb(233, 30, 99),
                                        Color.rgb(156, 39, 176),
                                        Color.rgb(255, 193, 7),
                                        Color.rgb(0, 188, 212),
                                        Color.rgb(255, 87, 34),
                                        Color.rgb(3, 169, 244),
                                        Color.rgb(139, 195, 74),
                                    )

                                this.colors = pieEntries.indices.map { colors[it % colors.size] }
                                valueTextColor = Color.WHITE
                                valueTextSize = 12f
                                sliceSpace = 3f

                                valueFormatter =
                                    object : ValueFormatter() {
                                        override fun getFormattedValue(value: Float): String = String.format("%.1f%%", value)
                                    }
                            }

                        val pieData = PieData(dataSet)
                        chart.data = pieData
                        chart.animateY(1000, Easing.EaseInOutQuad)
                        chart.invalidate()
                    },
                )
            }

            ChartType.LINE -> {
                AndroidView(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    factory = { context ->
                        LineChart(context).apply {
                            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)

                            val lineEntries =
                                entries.mapIndexed { index, value ->
                                    Entry(index.toFloat(), value)
                                }

                            val dataSet =
                                LineDataSet(lineEntries, "Trend Data").apply {
                                    color = Color.rgb(33, 150, 243)
                                    lineWidth = 2f
                                    setDrawValues(true)
                                    valueTextSize = 10f
                                    setDrawCircles(true)
                                    setCircleColor(Color.rgb(33, 150, 243))
                                    circleRadius = 4f
                                    setDrawCircleHole(true)
                                    circleHoleRadius = 2f
                                    mode = LineDataSet.Mode.CUBIC_BEZIER
                                    setDrawFilled(true)
                                    fillColor = Color.rgb(33, 150, 243)
                                    fillAlpha = 50
                                }

                            val lineData = LineData(dataSet)
                            data = lineData

                            description.apply {
                                text = ""
                                textColor = Color.DKGRAY
                            }

                            xAxis.apply {
                                granularity = 1f
                                setDrawGridLines(false)
                                position = XAxis.XAxisPosition.BOTTOM
                                valueFormatter = IndexAxisValueFormatter(labels)
                                labelRotationAngle = if (labels.size > 6) 45f else 0f
                            }

                            axisLeft.apply {
                                axisMinimum = 0f
                                granularity = entries.maxOrNull()?.div(5) ?: 10f
                                setDrawGridLines(true)
                            }

                            axisRight.isEnabled = false
                            legend.isEnabled = true

                            animateXY(1000, 1000)
                            invalidate()
                        }
                    },
                    update = { chart ->
                        val lineEntries =
                            entries.mapIndexed { index, value ->
                                Entry(index.toFloat(), value)
                            }

                        val dataSet =
                            LineDataSet(lineEntries, "Trend Data").apply {
                                color = Color.rgb(33, 150, 243)
                                lineWidth = 2f
                                setDrawValues(true)
                                valueTextSize = 10f
                                setDrawCircles(true)
                                setCircleColor(Color.rgb(33, 150, 243))
                                circleRadius = 4f
                                setDrawCircleHole(true)
                                circleHoleRadius = 2f
                                mode = LineDataSet.Mode.CUBIC_BEZIER
                                setDrawFilled(true)
                                fillColor = Color.rgb(33, 150, 243)
                                fillAlpha = 50
                            }

                        val lineData = LineData(dataSet)
                        chart.data = lineData
                        chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                        chart.xAxis.labelRotationAngle = if (labels.size > 6) 45f else 0f
                        chart.animateXY(1000, 1000)
                        chart.invalidate()
                    },
                )
            }

            else -> {}
        }
    }
}
