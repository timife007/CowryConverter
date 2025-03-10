package com.timife.cowryconverter.presentation.screens

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun Graph(modifier: Modifier = Modifier) {
    val tickColor = MaterialTheme.colorScheme.onPrimary

    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    val calendar = Calendar.getInstance().apply {
        set(2024, Calendar.JUNE, 1) // Start date: 01 June 2024
    }

    val xTickInterval = 4 // Every 4 days
    val xLabelInterval = 8 // Labels appear every 8 days, skipping one interval
    val numberOfXTicks = 10 // Number of tick marks
    val numberOfYTicks = 6  // Number of horizontal ticks

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val baselineY = height * 0.9f // Align everything to a base line
        val xGap = width / (numberOfXTicks - 1)
        val yGap = height / (numberOfYTicks - 1)

        val paint = Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        // Draw X-axis ticks and labels at points, omitting labels every other interval
        for (i in 0 until numberOfXTicks) {
            val x = i * xGap
            drawLine(
                color = tickColor,
                start = Offset(x, baselineY - 5f),
                end = Offset(x, baselineY),
                strokeWidth = 2f
            )

            if (i % 2 == 0) { // Label every other tick
                val date = calendar.time
                drawContext.canvas.nativeCanvas.drawText(
                    dateFormat.format(date),
                    x, // Label positioned directly on the tick
                    baselineY + 30f, // Move below the baseline
                    paint
                )
                calendar.add(Calendar.DAY_OF_MONTH, xLabelInterval)
            }
        }

        // Draw Y-axis ticks (skip tick at the origin)
        for (i in 1 until numberOfYTicks) { // Start from 1 to skip (0,0)
            val y = baselineY - (i * yGap)

            drawLine(
                color = tickColor,
                start = Offset(0f, y),
                end = Offset(5f, y), // Very short tick
                strokeWidth = 2f
            )
        }

        // Draw smooth sinusoidal curve with curved peaks and troughs
        val path = Path().apply {
            moveTo(0f, baselineY - (yGap * 2)) // Start with a trough
            cubicTo(
                width * 0.25f, baselineY - (yGap * 4),
                width * 0.25f, baselineY - (yGap * 1),
                width * 0.5f, baselineY - (yGap * 3) // Peak
            )
            cubicTo(
                width * 0.75f, baselineY - (yGap * 5),
                width * 0.75f, baselineY - (yGap * 2),
                width, baselineY - (yGap * 4) // Second trough
            )
        }
        drawPath(
            path = path,
            color = Color.White,
            style = Stroke(width = 4f)
        )
    }
}








