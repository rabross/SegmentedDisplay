package com.rabross.segmenteddisplay.seven

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.rabross.segmenteddisplay.Led
import com.rabross.segmenteddisplay.SingleColorLed
import com.rabross.segmenteddisplay.delimiter.Delimiter
import com.rabross.segmenteddisplay.seven.BinaryDecoder.Companion.mapToDisplay
import com.rabross.segmenteddisplay.delimiter.BinaryDecoder as DelimiterBinaryDecoder

@Preview
@Composable
fun FlatSegmentDisplayPreview() {
    Surface(color = Color.Black) {
        SegmentDisplay(
            decoder = BinaryDecoder(BinaryDecoder.mapToDisplay(5)),
            segmentStyle = SegmentStyle.FLAT
        )
    }
}

@Preview
@Composable
fun DiffuserSegmentDisplayPreview() {
    Surface(color = Color.Black) {
        SegmentDisplay(
            decoder = BinaryDecoder(BinaryDecoder.mapToDisplay(5)),
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

@Composable
fun SegmentDisplay(
    modifier: Modifier = Modifier,
    segmentScale: Int = 3,
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

            val horizontalSegmentSize = Size(segmentLength, segmentWidth)
            val verticalSegmentSize = Size(segmentWidth, segmentLength)

            val aOffset = Offset(segmentWidth, 0f)
            val bOffset = Offset(segmentLength + segmentWidth, segmentWidth)
            val cOffset = Offset(segmentLength + segmentWidth, segmentLength + segmentWidth + segmentWidth)
            val dOffset = Offset(segmentWidth, segmentLength + segmentWidth + segmentLength + segmentWidth)
            val eOffset = Offset(0f, segmentLength + segmentWidth + segmentWidth)
            val fOffset = Offset(0f, segmentWidth)
            val gOffset = Offset(segmentWidth, segmentLength + segmentWidth)

            val aPath = createHorizontalSegmentPath(aOffset, horizontalSegmentSize, spacingRatioLimited)
            val bPath = createVerticalSegmentPath(bOffset, verticalSegmentSize, spacingRatioLimited)
            val cPath = createVerticalSegmentPath(cOffset, verticalSegmentSize, spacingRatioLimited)
            val dPath = createHorizontalSegmentPath(dOffset, horizontalSegmentSize, spacingRatioLimited)
            val ePath = createVerticalSegmentPath(eOffset, verticalSegmentSize, spacingRatioLimited)
            val fPath = createVerticalSegmentPath(fOffset, verticalSegmentSize, spacingRatioLimited)
            val gPath = createHorizontalSegmentPath(gOffset, horizontalSegmentSize, spacingRatioLimited)

            onDrawBehind {
                drawSegment(led.signal(decoder.a), segmentStyle, aPath)
                drawSegment(led.signal(decoder.b), segmentStyle, bPath)
                drawSegment(led.signal(decoder.c), segmentStyle, cPath)
                drawSegment(led.signal(decoder.d), segmentStyle, dPath)
                drawSegment(led.signal(decoder.e), segmentStyle, ePath)
                drawSegment(led.signal(decoder.f), segmentStyle, fPath)
                drawSegment(led.signal(decoder.g), segmentStyle, gPath)
            }
        }
    ) {}
}

private fun createHorizontalSegmentPath(offset: Offset, size: Size, spacingRatio: Float): Path {
    val radius = size.minDimension / 2
    val centerX: Float = offset.x + size.width / 2
    val centerY: Float = offset.y + size.height / 2
    val spacing = radius * spacingRatio
    return Path().apply {
        moveTo(centerX, centerY + radius - spacing)
        lineTo(centerX - size.width / 2, centerY + radius - spacing)
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
    val centerX: Float = offset.x + size.width / 2
    val centerY: Float = offset.y + size.height / 2
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
            decoder = BinaryDecoder(hourFirst),
            segmentStyle = SegmentStyle.DIFFUSER
        )
        SegmentDisplay(
            modifier = digitModifier,
            decoder = BinaryDecoder(hourSecond),
            segmentStyle = SegmentStyle.DIFFUSER
        )
        Delimiter(
            modifier = digitModifier,
            decoder = DelimiterBinaryDecoder(delimiterSignal)
        )
        SegmentDisplay(
            modifier = digitModifier,
            decoder = BinaryDecoder(minuteFirst),
            segmentStyle = SegmentStyle.DIFFUSER
        )
        SegmentDisplay(
            modifier = digitModifier,
            decoder = BinaryDecoder(minuteSecond),
            segmentStyle = SegmentStyle.DIFFUSER
        )
        Delimiter(
            modifier = digitModifier,
            decoder = DelimiterBinaryDecoder(delimiterSignal)
        )
        SegmentDisplay(
            modifier = digitModifier,
            decoder = BinaryDecoder(secondFirst),
            segmentStyle = SegmentStyle.DIFFUSER
        )
        SegmentDisplay(
            modifier = digitModifier,
            decoder = BinaryDecoder(secondSecond),
            segmentStyle = SegmentStyle.DIFFUSER
        )
    }
}


private val defaultLed = SingleColorLed(Color.Red, Color.DarkGray.copy(alpha = 0.3f))

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
