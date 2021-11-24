package com.rabross.segmenteddisplay.seven

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        DigitalClock()
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
    Canvas(modifier = modifier
        .aspectRatio((1f + segmentScale + 1f) / (1f + segmentScale + 1f + segmentScale + 1f), true)
        .size(100.dp)) {

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

        val spacingRatioLimited = spacingRatio.coerceIn(0f..0.9f)

        drawHorizontalSegment(led.signal(decoder.a), segmentStyle, aOffset, horizontalSegmentSize, spacingRatioLimited)
        drawVerticalSegment(led.signal(decoder.b), segmentStyle, bOffset, verticalSegmentSize, spacingRatioLimited)
        drawVerticalSegment(led.signal(decoder.c), segmentStyle, cOffset, verticalSegmentSize, spacingRatioLimited)
        drawHorizontalSegment(led.signal(decoder.d), segmentStyle, dOffset, horizontalSegmentSize, spacingRatioLimited)
        drawVerticalSegment(led.signal(decoder.e), segmentStyle, eOffset, verticalSegmentSize, spacingRatioLimited)
        drawVerticalSegment(led.signal(decoder.f), segmentStyle, fOffset, verticalSegmentSize, spacingRatioLimited)
        drawHorizontalSegment(led.signal(decoder.g), segmentStyle, gOffset, horizontalSegmentSize, spacingRatioLimited)
    }
}

private fun DrawScope.drawHorizontalSegment(color: Color, segmentStyle: SegmentStyle, offset: Offset, size: Size, spacingRatio: Float) {
    val radius = size.minDimension / 2
    val centerX: Float = offset.x + size.width / 2
    val centerY: Float = offset.y + size.height / 2
    val spacing = radius * spacingRatio
    val hexagonPath = Path().apply {
        moveTo(centerX, centerY + radius - spacing)
        lineTo(centerX - size.width/2, centerY + radius - spacing)
        lineTo(centerX - size.width/2 - radius + spacing, centerY)
        lineTo(centerX - size.width/2, centerY - radius + spacing)
        lineTo(centerX + size.width/2, centerY - radius + spacing)
        lineTo(centerX + size.width/2 + radius - spacing, centerY)
        lineTo(centerX + size.width/2, centerY + radius - spacing)
    }
    when(segmentStyle) {
        SegmentStyle.FLAT -> drawPath(hexagonPath, color)
        SegmentStyle.DIFFUSER -> drawPath(hexagonPath, color.toLedBrush(Offset(centerX, centerY)))
    }
}

private fun DrawScope.drawVerticalSegment(color: Color, segmentStyle: SegmentStyle, offset: Offset, size: Size, spacingRatio: Float) {
    val radius = size.minDimension / 2
    val centerX: Float = offset.x + size.width / 2
    val centerY: Float = offset.y + size.height / 2
    val spacing = radius * spacingRatio
    val hexagonPath = Path().apply {
        moveTo(centerX, centerY + radius + size.height/2 - spacing)
        lineTo(centerX - radius + spacing, centerY + size.height/2)
        lineTo(centerX - radius + spacing, centerY - size.height/2)
        lineTo(centerX, centerY - radius - size.height/2 + spacing)
        lineTo(centerX + radius - spacing, centerY - size.height/2)
        lineTo(centerX + radius - spacing, centerY + size.height/2)
    }
    when(segmentStyle) {
        SegmentStyle.FLAT -> drawPath(hexagonPath, color)
        SegmentStyle.DIFFUSER -> drawPath(hexagonPath, color.toLedBrush(Offset(centerX, centerY)))
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
        SegmentDisplay(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            decoder = BinaryDecoder(hourFirst),
            segmentStyle = SegmentStyle.DIFFUSER
        )
        SegmentDisplay(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            decoder = BinaryDecoder(hourSecond),
            segmentStyle = SegmentStyle.DIFFUSER
        )
        Delimiter(modifier = Modifier
            .weight(1f)
            .padding(4.dp),
            decoder = DelimiterBinaryDecoder(delimiterSignal)
        )
        SegmentDisplay(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            decoder = BinaryDecoder(minuteFirst),
            segmentStyle = SegmentStyle.DIFFUSER
        )
        SegmentDisplay(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            decoder = BinaryDecoder(minuteSecond),
            segmentStyle = SegmentStyle.DIFFUSER
        )
        Delimiter(modifier = Modifier
            .weight(1f)
            .padding(4.dp),
            decoder = DelimiterBinaryDecoder(delimiterSignal)
        )
        SegmentDisplay(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            decoder = BinaryDecoder(secondFirst),
            segmentStyle = SegmentStyle.DIFFUSER
        )
        SegmentDisplay(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            decoder = BinaryDecoder(secondSecond),
            segmentStyle = SegmentStyle.DIFFUSER
        )
    }
}

private val defaultLed = SingleColorLed(Color.Red, Color.DarkGray.copy(alpha = 0.3f))

private const val diffuserFactor = 0.4f

private fun Color.toLedBrush(offset: Offset) = Brush.radialGradient(
    colors = listOf(
        this, copy(
            red = red * diffuserFactor,
            green = green * diffuserFactor,
            blue = blue * diffuserFactor
        )
    ),
    center = offset
)