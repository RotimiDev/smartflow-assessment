package com.example.smartflowassessment.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartflowassessment.data.model.SalesDataPoint

@Composable
fun SalesBarChart(
    salesData: List<SalesDataPoint>,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val chartWidth = size.width * 0.9f
        val chartHeight = size.height * 0.8f
        val startX = (size.width - chartWidth) / 2
        val bottomY = size.height * 0.9f

        val maxValue = salesData.maxOfOrNull { it.amount } ?: 1f

        val barWidth = chartWidth / salesData.size * 0.7f
        val spacing = chartWidth / salesData.size * 0.3f

        drawLine(
            color = Color.Gray,
            start = Offset(startX, bottomY),
            end = Offset(startX + chartWidth, bottomY),
            strokeWidth = 2f,
        )

        drawLine(
            color = Color.Gray,
            start = Offset(startX, bottomY),
            end = Offset(startX, bottomY - chartHeight),
            strokeWidth = 2f,
        )

        salesData.forEachIndexed { index, dataPoint ->
            val barHeight = (dataPoint.amount / maxValue) * chartHeight
            val barX = startX + index * (barWidth + spacing) + spacing / 2

            drawRect(
                color = Color.Blue,
                topLeft = Offset(barX, bottomY - barHeight),
                size = Size(barWidth, barHeight),
            )

            drawContext.canvas.nativeCanvas.apply {
                val textPaint =
                    Paint().apply {
                        color = android.graphics.Color.GRAY
                        textAlign = Paint.Align.CENTER
                        textSize = 12.sp.toPx()
                    }
                drawText(
                    dataPoint.month,
                    barX + barWidth / 2,
                    bottomY + 15.dp.toPx(),
                    textPaint,
                )

                val formattedAmount = "$${dataPoint.amount.toInt()}"
                drawText(
                    formattedAmount,
                    barX + barWidth / 2,
                    bottomY - barHeight - 5.dp.toPx(),
                    textPaint,
                )
            }
        }
    }
}
