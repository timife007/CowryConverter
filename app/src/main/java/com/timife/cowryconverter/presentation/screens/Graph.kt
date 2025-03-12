package com.timife.cowryconverter.presentation.screens

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import com.timife.cowryconverter.presentation.common.components.AppBackground
import com.timife.cowryconverter.presentation.ui.theme.CowryConverterTheme
import com.timife.cowryconverter.presentation.ui.theme.Green
import com.timife.cowryconverter.presentation.ui.theme.LightBlue
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun Graph(modifier: Modifier = Modifier) {
    val tickColor = MaterialTheme.colorScheme.onPrimary
    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    val calendar = Calendar.getInstance().apply {
        set(2024, Calendar.JUNE, 1)
    }

    val xTickInterval = 4
    val xLabelInterval = 8
    val numberOfXTicks = 10
    val numberOfYTicks = 6


    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val baselineY = height * 0.9f
        val xGap = width / (numberOfXTicks - 1)
        val yGap = height / (numberOfYTicks - 1)

        val paint = Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            textSize = 28f
        }

        // X-axis ticks and labels
        for (i in 0 until numberOfXTicks) {
            val x = i * xGap
            drawLine(
                color = tickColor,
                start = Offset(x, baselineY - 5f),
                end = Offset(x, baselineY),
                strokeWidth = 2f
            )
            if (i % 2 == 0) {
                val date = calendar.time
                drawContext.canvas.nativeCanvas.drawText(
                    dateFormat.format(date),
                    x,
                    baselineY + 30f,
                    paint
                )
                calendar.add(Calendar.DAY_OF_MONTH, xLabelInterval)
            }
        }

        // Y-axis ticks
        for (i in 1 until numberOfYTicks) {
            val y = baselineY - (i * yGap)
            drawLine(
                color = tickColor,
                start = Offset(0f, y),
                end = Offset(5f, y),
                strokeWidth = 2f
            )
        }

        // Curve path
        val curvePath = Path().apply {
            moveTo(0f, baselineY - yGap * 2)
            cubicTo(
                width * 0.3f, baselineY - yGap * 6,
                width * 0.6f, baselineY + yGap * 2,
                width, baselineY - yGap * 5
            )
        }

        // Shaded area
        val shadedPath = Path().apply {
            addPath(curvePath)
            lineTo(width, baselineY)
            lineTo(0f, baselineY)
            close()
        }

        val gradientBrush = Brush.verticalGradient(
            colors = listOf(Color.White.copy(alpha = 0.3f), LightBlue),
            startY = 0f,
            endY = baselineY
        )

        drawPath(
            path = shadedPath,
            brush = gradientBrush
        )

        // Cubic Bézier evaluator
        fun cubicBezierPoint(t: Float, p0: Offset, p1: Offset, p2: Offset, p3: Offset): Offset {
            val oneMinusT = 1 - t
            val x = oneMinusT * oneMinusT * oneMinusT * p0.x +
                    3 * oneMinusT * oneMinusT * t * p1.x +
                    3 * oneMinusT * t * t * p2.x +
                    t * t * t * p3.x
            val y = oneMinusT * oneMinusT * oneMinusT * p0.y +
                    3 * oneMinusT * oneMinusT * t * p1.y +
                    3 * oneMinusT * t * t * p2.y +
                    t * t * t * p3.y
            return Offset(x, y)
        }

        // Actual peak position (on curve)
        val peakPoint = cubicBezierPoint(
            t = 0.33f,
            p0 = Offset(0f, baselineY - yGap * 2),
            p1 = Offset(width * 0.3f, baselineY - yGap * 6),
            p2 = Offset(width * 0.6f, baselineY + yGap * 2),
            p3 = Offset(width, baselineY - yGap * 5)
        )

        // Circle marker
        drawCircle(
            color = Color.White,
            radius = 12f,
            center = peakPoint
        )
        drawCircle(
            color = Green,
            radius = 8f,
            center = peakPoint
        )

        // Draw the card just above the circle
        val cardWidth = 200f
        val cardHeight = 120f
        val cardTopLeftX = peakPoint.x
        val cardTopLeftY = peakPoint.y - cardHeight - 50f // More spacing above the curve

        // Draw main card with rounded corners
        drawRoundRect(
            color = Green,
            topLeft = Offset(cardTopLeftX, cardTopLeftY),
            size = Size(cardWidth, cardHeight),
            cornerRadius = CornerRadius(16f, 16f)
        )

        // Patch bottom-left corner to appear sharp
        drawRect(
            color = Green,
            topLeft = Offset(cardTopLeftX, cardTopLeftY + cardHeight - 16f),
            size = Size(16f, 16f)
        )

        val textPaint = Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = 28f
            isAntiAlias = true
        }


        drawContext.canvas.nativeCanvas.drawText(
            "15 Jun",
            cardTopLeftX + cardWidth / 2,
            cardTopLeftY + cardHeight / 2 - 10f,
            textPaint
        )

        drawContext.canvas.nativeCanvas.drawText(
            "1 EUR = 4,242",
            cardTopLeftX + cardWidth / 2,
            cardTopLeftY + cardHeight / 2 + 30f,
            textPaint
        )
    }
}


@Composable
@Preview
fun GraphPreview() {
    CowryConverterTheme {
        AppBackground {
            Graph()
        }
    }
}







