package com.rabross.segmenteddisplay.fourteen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rabross.segmenteddisplay.delimiter.BinaryDecoder as DelimiterBinaryDecoder
import com.rabross.segmenteddisplay.Led
import com.rabross.segmenteddisplay.SingleColorLed
import com.rabross.segmenteddisplay.delimiter.Delimiter
import com.rabross.segmenteddisplay.fourteen.BinaryDecoder.Companion.mapToDisplay
import kotlin.math.pow
import kotlin.math.sqrt

@Preview
@Composable
fun FourteenSegmentDisplayPreview() {
    Surface {
        SegmentDisplay()
    }
}

@Preview
@Composable
fun DigitalClockPreview() {
    Surface(color = Color.Black) {
        DigitalClock(
            hourFirst = mapToDisplay(1),
            hourSecond = mapToDisplay(2),
            minuteFirst = mapToDisplay(3),
            minuteSecond = mapToDisplay(4),
            secondFirst = mapToDisplay(5),
            secondSecond = mapToDisplay(6),
            delimiterSignal = 3
        )
    }
}

@Preview(widthDp = 300, heightDp = 900)
@Composable
fun AlphabetPreview() {
    val alphabet = ('A'..'Z').toList() + ('0'..'9').toList()
    Surface(color = Color.Black, modifier = Modifier.size(400.dp)) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(50.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(alphabet) { char ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    SegmentDisplay(
                        modifier = Modifier.padding(2.dp),
                        decoder = BinaryDecoder(BinaryDecoder.mapToDisplay(char))
                    )
                }
            }
        }
    }
}

@Composable
fun DigitalClock(
    modifier: Modifier = Modifier,
    hourFirst: Int = 0,
    hourSecond: Int = 0,
    minuteFirst: Int = 0,
    minuteSecond: Int = 0,
    secondFirst: Int = 0,
    secondSecond: Int = 0,
    delimiterSignal: Int = 0
) {
    Row(modifier = modifier) {
        val digitModifier = Modifier
            .weight(1f)
            .padding(4.dp)

        SegmentDisplay(
            modifier = digitModifier,
            decoder = BinaryDecoder(hourFirst)
        )
        SegmentDisplay(
            modifier = digitModifier,
            decoder = BinaryDecoder(hourSecond)
        )
        Delimiter(
            modifier = digitModifier,
            decoder = DelimiterBinaryDecoder(delimiterSignal)
        )
        SegmentDisplay(
            modifier = digitModifier,
            decoder = BinaryDecoder(minuteFirst)
        )
        SegmentDisplay(
            modifier = digitModifier,
            decoder = BinaryDecoder(minuteSecond)
        )
        Delimiter(
            modifier = digitModifier,
            decoder = DelimiterBinaryDecoder(delimiterSignal)
        )
        SegmentDisplay(
            modifier = digitModifier,
            decoder = BinaryDecoder(secondFirst)
        )
        SegmentDisplay(
            modifier = digitModifier,
            decoder = BinaryDecoder(secondSecond)
        )
    }
}

@Composable
fun SegmentDisplay(
    modifier: Modifier = Modifier,
    segmentScale: Int = 4,
    led: Led = SingleColorLed(Color.Red, Color.DarkGray.copy(alpha = 0.4f)),
    decoder: Decoder = BinaryDecoder()
) {
    Canvas(modifier = modifier
        .aspectRatio((1f + segmentScale + 1f) / (1f + segmentScale + 1f + segmentScale + 1f), true)
        .size(100.dp)
        .drawWithCache {
            val scaleWidth = 1 + segmentScale + 1
            val scaleHeight = 1 + segmentScale + 1 + segmentScale + 1

            val segmentWidthByWidth = size.width / scaleWidth
            val segmentWidthByHeight = size.height / scaleHeight

            val segmentWidth = if (scaleHeight * segmentWidthByWidth < size.height) segmentWidthByWidth else segmentWidthByHeight
            val segmentLength = segmentScale * segmentWidth

            val fullWidth = segmentWidth * scaleWidth
            val quarterGapWidth = (fullWidth - segmentWidth * 3) / 2

            val hSize = Size(segmentLength, segmentWidth)
            val shSize = Size(segmentLength / 2, segmentWidth)
            val vSize = Size(segmentWidth, segmentLength)
            val svSize = Size(segmentWidth, segmentLength - segmentWidth / 2)
            val dSize = Size(quarterGapWidth, segmentLength)

            val aOffset = Offset(segmentWidth, 0f)
            val bOffset = Offset(segmentLength + segmentWidth, segmentWidth)
            val cOffset = Offset(segmentLength + segmentWidth, segmentLength + segmentWidth * 2)
            val dOffset = Offset(segmentWidth, segmentLength + segmentWidth + segmentLength + segmentWidth)
            val eOffset = Offset(0f, segmentLength + segmentWidth * 2)
            val fOffset = Offset(0f, segmentWidth)
            val g1Offset = Offset(segmentWidth, segmentLength + segmentWidth)
            val g2Offset = Offset(quarterGapWidth + segmentWidth * 2, segmentLength + segmentWidth)
            val hOffset = Offset(segmentWidth, segmentWidth)
            val iOffset = Offset(quarterGapWidth + segmentWidth, segmentWidth + segmentWidth / 2)
            val jOffset = Offset(quarterGapWidth + segmentWidth * 2, segmentWidth)
            val kOffset = Offset(segmentWidth, segmentLength + segmentWidth * 2)
            val lOffset = Offset(quarterGapWidth + segmentWidth, segmentLength + segmentWidth * 2)
            val mOffset = Offset(quarterGapWidth + segmentWidth * 2, segmentLength + segmentWidth * 2)

            val aPath = createHorizontalSegmentPath(aOffset, hSize)
            val bPath = createVerticalSegmentPath(bOffset, vSize)
            val cPath = createVerticalSegmentPath(cOffset, vSize)
            val dPath = createHorizontalSegmentPath(dOffset, hSize)
            val ePath = createVerticalSegmentPath(eOffset, vSize)
            val fPath = createVerticalSegmentPath(fOffset, vSize)
            val g1Path = createSmallHorizontalSegmentPath(g1Offset, shSize)
            val g2Path = createSmallHorizontalSegmentPath(g2Offset, shSize)
            val hPath = createBackSlashSegmentPath(segmentWidth, hOffset, dSize)
            val iPath = createSmallVerticalSegmentPath(iOffset, svSize)
            val jPath = createForwardSlashSegmentPath(segmentWidth, jOffset, dSize)
            val kPath = createForwardSlashSegmentPath(segmentWidth, kOffset, dSize)
            val lPath = createSmallVerticalSegmentPath(lOffset, svSize)
            val mPath = createBackSlashSegmentPath(segmentWidth, mOffset, dSize)

            onDrawBehind {
                drawPath(aPath, led.signal(decoder.a))
                drawPath(bPath, led.signal(decoder.b))
                drawPath(cPath, led.signal(decoder.c))
                drawPath(dPath, led.signal(decoder.d))
                drawPath(ePath, led.signal(decoder.e))
                drawPath(fPath, led.signal(decoder.f))
                drawPath(g1Path, led.signal(decoder.g1))
                drawPath(g2Path, led.signal(decoder.g2))
                drawPath(hPath, led.signal(decoder.h))
                drawPath(iPath, led.signal(decoder.i))
                drawPath(jPath, led.signal(decoder.j))
                drawPath(kPath, led.signal(decoder.k))
                drawPath(lPath, led.signal(decoder.l))
                drawPath(mPath, led.signal(decoder.m))
            }
        }
    ) {}
}

private fun createHorizontalSegmentPath(offset: Offset, size: Size): Path {
    val radius = size.minDimension / 2
    val centerX = offset.x + size.width / 2
    val centerY = offset.y + size.height / 2
    val spacing = radius / 10
    return Path().apply {
        moveTo(centerX - size.width / 2, centerY + radius - spacing)
        lineTo(centerX - size.width / 2 - radius + spacing, centerY)
        lineTo(centerX - size.width / 2, centerY - radius + spacing)
        lineTo(centerX + size.width / 2, centerY - radius + spacing)
        lineTo(centerX + size.width / 2 + radius - spacing, centerY)
        lineTo(centerX + size.width / 2, centerY + radius - spacing)
        close()
    }
}

private fun createVerticalSegmentPath(offset: Offset, size: Size): Path {
    val radius = size.minDimension / 2
    val centerX = offset.x + size.width / 2
    val centerY = offset.y + size.height / 2
    val spacing = radius / 10
    return Path().apply {
        moveTo(centerX, centerY + radius + size.height / 2 - spacing)
        lineTo(centerX - radius + spacing, centerY + size.height / 2)
        lineTo(centerX - radius + spacing, centerY - size.height / 2)
        lineTo(centerX, centerY - radius - size.height / 2 + spacing)
        lineTo(centerX + radius - spacing, centerY - size.height / 2)
        lineTo(centerX + radius - spacing, centerY + size.height / 2)
        close()
    }
}

private fun createSmallHorizontalSegmentPath(offset: Offset, size: Size): Path {
    val radius = size.minDimension / 2
    val centerX = offset.x + size.width / 2
    val centerY = offset.y + size.height / 2
    val spacing = radius / 10
    return Path().apply {
        moveTo(centerX - size.width / 2, centerY + radius - spacing)
        lineTo(centerX - size.width / 2 - radius + spacing, centerY)
        lineTo(centerX - size.width / 2, centerY - radius + spacing)
        lineTo(centerX + size.width / 2 - radius, centerY - radius + spacing)
        lineTo(centerX + size.width / 2 - spacing, centerY)
        lineTo(centerX + size.width / 2 - radius, centerY + radius - spacing)
        close()
    }
}

private fun createSmallVerticalSegmentPath(offset: Offset, size: Size): Path {
    val radius = size.minDimension / 2
    val centerX = offset.x + size.width / 2
    val centerY = offset.y + size.height / 2
    val spacing = radius / 10
    return Path().apply {
        moveTo(centerX, centerY + radius + size.height / 2 - spacing)
        lineTo(centerX - radius + spacing, centerY + size.height / 2)
        lineTo(centerX - radius + spacing, centerY - size.height / 2)
        lineTo(centerX, centerY - radius - size.height / 2 + spacing)
        lineTo(centerX + radius - spacing, centerY - size.height / 2)
        lineTo(centerX + radius - spacing, centerY + size.height / 2)
        close()
    }
}

private fun createBackSlashSegmentPath(width: Float, offset: Offset, size: Size): Path {
    val diagonalWidth = sqrt(width.pow(2) / 2) * 2
    val spacing = diagonalWidth / 40
    return Path().apply {
        moveTo(offset.x + spacing, offset.y + spacing)
        lineTo(offset.x + size.width - spacing, offset.y + size.height - diagonalWidth)
        lineTo(offset.x + size.width - spacing, offset.y + size.height - spacing)
        lineTo(offset.x + spacing, offset.y + diagonalWidth)
        close()
    }
}

private fun createForwardSlashSegmentPath(width: Float, offset: Offset, size: Size): Path {
    val diagonalWidth = sqrt(width.pow(2) / 2) * 2
    val spacing = diagonalWidth / 40
    return Path().apply {
        moveTo(offset.x + spacing, offset.y + size.height - spacing)
        lineTo(offset.x + spacing, offset.y + size.height - diagonalWidth)
        lineTo(offset.x + size.width - spacing, offset.y + spacing)
        lineTo(offset.x + size.width - spacing, offset.y + diagonalWidth)
        close()
    }
}

