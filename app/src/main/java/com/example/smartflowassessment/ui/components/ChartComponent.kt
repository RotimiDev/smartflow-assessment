package com.example.smartflowassessment.ui.components

import android.graphics.Color
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet

@Composable
fun ChartComponent(
    entries: List<BarEntry>,
    labels: List<String>,
) {
    AndroidView(factory = { context ->
        BarChart(context).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 500)

            val dataSet =
                BarDataSet(entries, "Stock Levels").apply {
                    color = Color.rgb(33, 150, 243)
                    valueTextColor = Color.BLACK
                    valueTextSize = 14f
                }

            val barData = BarData(dataSet as IBarDataSet)
            this.data = barData

            description =
                Description().apply {
                    text = "Stock per item"
                    textColor = Color.DKGRAY
                }

            xAxis.apply {
                granularity = 1f
                setDrawGridLines(false)
                setDrawLabels(true)
                setDrawAxisLine(true)
                position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
            }

            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = false

            legend.isEnabled = false

            animateY(1000)
        }
    }, update = {
        it.animateY(1000)
    })
}
