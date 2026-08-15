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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rabross.segmenteddisplay.delimiter.BinaryDecoder as DelimiterBinaryDecoder
import com.rabross.segmenteddisplay.Led
import com.rabross.segmenteddisplay.SingleColorLed
import com.rabross.segmenteddisplay.delimiter.Delimiter
import com.rabross.segmenteddisplay.fourteen.BinaryDecoder.Companion.mapToDisplay
import com.rabross.segmenteddisplay.seven.SegmentStyle
import kotlin.math.pow
import kotlin.math.sqrt

@Preview
@Composable
fun FourteenFlatSegmentDisplayPreview() {
    Surface(color = Color.Black) {
        SegmentDisplay(
            decoder = BinaryDecoder(mapToDisplay('A')),
            segmentStyle = SegmentStyle.FLAT
        )
    }
}

@Preview
@Composable
fun FourteenDiffuserSegmentDisplayPreview() {
    Surface(color = Color.Black) {
        SegmentDisplay(
            decoder = BinaryDecoder(mapToDisplay('A')),
            segmentStyle = SegmentStyle.DIFFUSER
        )
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
                        decoder = BinaryDecoder(mapToDisplay(char)),
                        segmentStyle = SegmentStyle.DIFFUSER
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
    delimiterSignal: Int = 0,
    segmentStyle: SegmentStyle = SegmentStyle.DIFFUSER
) {
    Row(modifier = modifier) {
        val digitModifier = Modifier
            .weight(1f)
            .padding(4.dp)

        SegmentDisplay(
            modifier = digitModifier,
            decoder = BinaryDecoder(hourFirst),
            segmentStyle = segmentStyle
        )
        SegmentDisplay(
            modifier = digitModifier,
            decoder = BinaryDecoder(hourSecond),
            segmentStyle = segmentStyle
        )
        Delimiter(
            modifier = digitModifier,
            decoder = DelimiterBinaryDecoder(delimiterSignal)
        )
        SegmentDisplay(
            modifier = digitModifier,
            decoder = BinaryDecoder(minuteFirst),
            segmentStyle = segmentStyle
        )
        SegmentDisplay(
            modifier = digitModifier,
            decoder = BinaryDecoder(minuteSecond),
            segmentStyle = segmentStyle
        )
        Delimiter(
            modifier = digitModifier,
            decoder = DelimiterBinaryDecoder(delimiterSignal)
        )
        SegmentDisplay(
            modifier = digitModifier,
            decoder = BinaryDecoder(secondFirst),
            segmentStyle = segmentStyle
        )
        SegmentDisplay(
            modifier = digitModifier,
            decoder = BinaryDecoder(secondSecond),
            segmentStyle = segmentStyle
        )
    }
}

@Composable
fun SegmentDisplay(
    modifier: Modifier = Modifier,
    segmentScale: Int = 4,
    spacingRatio: Float = 0.2f,
    led: Led = defaultLed,
    segmentStyle: SegmentStyle = SegmentStyle.FLAT,
    decoder: Decoder = BinaryDecoder()
) {
    val spacingRatioLimited = remember(spacingRatio) { spacingRatio.coerceIn(0f..0.9f) }

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

            val aPath = createHorizontalSegmentPath(aOffset, hSize, spacingRatioLimited)
            val bPath = createVerticalSegmentPath(bOffset, vSize, spacingRatioLimited)
            val cPath = createVerticalSegmentPath(cOffset, vSize, spacingRatioLimited)
            val dPath = createHorizontalSegmentPath(dOffset, hSize, spacingRatioLimited)
            val ePath = createVerticalSegmentPath(eOffset, vSize, spacingRatioLimited)
            val fPath = createVerticalSegmentPath(fOffset, vSize, spacingRatioLimited)
            val g1Path = createSmallHorizontalSegmentPath(g1Offset, shSize, spacingRatioLimited)
            val g2Path = createSmallHorizontalSegmentPath(g2Offset, shSize, spacingRatioLimited)
            val hPath = createBackSlashSegmentPath(segmentWidth, hOffset, dSize, spacingRatioLimited)
            val iPath = createSmallVerticalSegmentPath(iOffset, svSize, spacingRatioLimited)
            val jPath = createForwardSlashSegmentPath(segmentWidth, jOffset, dSize, spacingRatioLimited)
            val kPath = createForwardSlashSegmentPath(segmentWidth, kOffset, dSize, spacingRatioLimited)
            val lPath = createSmallVerticalSegmentPath(lOffset, svSize, spacingRatioLimited)
            val mPath = createBackSlashSegmentPath(segmentWidth, mOffset, dSize, spacingRatioLimited)

            onDrawBehind {
                drawSegment(led.signal(decoder.a), segmentStyle, aPath)
                drawSegment(led.signal(decoder.b), segmentStyle, bPath)
                drawSegment(led.signal(decoder.c), segmentStyle, cPath)
                drawSegment(led.signal(decoder.d), segmentStyle, dPath)
                drawSegment(led.signal(decoder.e), segmentStyle, ePath)
                drawSegment(led.signal(decoder.f), segmentStyle, fPath)
                drawSegment(led.signal(decoder.g1), segmentStyle, g1Path)
                drawSegment(led.signal(decoder.g2), segmentStyle, g2Path)
                drawSegment(led.signal(decoder.h), segmentStyle, hPath)
                drawSegment(led.signal(decoder.i), segmentStyle, iPath)
                drawSegment(led.signal(decoder.j), segmentStyle, jPath)
                drawSegment(led.signal(decoder.k), segmentStyle, kPath)
                drawSegment(led.signal(decoder.l), segmentStyle, lPath)
                drawSegment(led.signal(decoder.m), segmentStyle, mPath)
            }
        }
    ) {}
}

private fun createHorizontalSegmentPath(offset: Offset, size: Size, spacingRatio: Float): Path {
    val radius = size.minDimension / 2
    val centerX = offset.x + size.width / 2
    val centerY = offset.y + size.height / 2
    val spacing = radius * spacingRatio
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

private fun createVerticalSegmentPath(offset: Offset, size: Size, spacingRatio: Float): Path {
    val radius = size.minDimension / 2
    val centerX = offset.x + size.width / 2
    val centerY = offset.y + size.height / 2
    val spacing = radius * spacingRatio
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

private fun createSmallHorizontalSegmentPath(offset: Offset, size: Size, spacingRatio: Float): Path {
    val radius = size.minDimension / 2
    val centerX = offset.x + size.width / 2
    val centerY = offset.y + size.height / 2
    val spacing = radius * spacingRatio
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

private fun createSmallVerticalSegmentPath(offset: Offset, size: Size, spacingRatio: Float): Path {
    val radius = size.minDimension / 2
    val centerX = offset.x + size.width / 2
    val centerY = offset.y + size.height / 2
    val spacing = radius * spacingRatio
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

private fun createBackSlashSegmentPath(width: Float, offset: Offset, size: Size, spacingRatio: Float): Path {
    val diagonalWidth = sqrt(width.pow(2) / 2) * 2
    val spacing = (diagonalWidth / 4) * spacingRatio
    return Path().apply {
        moveTo(offset.x + spacing, offset.y + spacing)
        lineTo(offset.x + size.width - spacing, offset.y + size.height - diagonalWidth)
        lineTo(offset.x + size.width - spacing, offset.y + size.height - spacing)
        lineTo(offset.x + spacing, offset.y + diagonalWidth)
        close()
    }
}

private fun createForwardSlashSegmentPath(width: Float, offset: Offset, size: Size, spacingRatio: Float): Path {
    val diagonalWidth = sqrt(width.pow(2) / 2) * 2
    val spacing = (diagonalWidth / 4) * spacingRatio
    return Path().apply {
        moveTo(offset.x + spacing, offset.y + size.height - spacing)
        lineTo(offset.x + spacing, offset.y + size.height - diagonalWidth)
        lineTo(offset.x + size.width - spacing, offset.y + spacing)
        lineTo(offset.x + size.width - spacing, offset.y + diagonalWidth)
        close()
    }
}

private fun DrawScope.drawSegment(
    color: Color,
    segmentStyle: SegmentStyle,
    path: Path
) {
    val finalBrush = if (segmentStyle == SegmentStyle.FLAT) {
        null
    } else {
        val bounds = path.getBounds()
        color.toLedBrush(bounds.center)
    }

    if (finalBrush != null) {
        drawPath(path, finalBrush)
    } else {
        drawPath(path, color)
    }
}

private val defaultLed = SingleColorLed(Color.Red, Color.DarkGray.copy(alpha = 0.4f))

private const val diffuserFactor = 0.4f

private fun Color.toLedBrush(center: Offset) = Brush.radialGradient(
    colors = listOf(this, darker),
    center = center
)

private val Color.darker
    get() = copy(
        red = red * diffuserFactor,
        green = green * diffuserFactor,
        blue = blue * diffuserFactor
    )

