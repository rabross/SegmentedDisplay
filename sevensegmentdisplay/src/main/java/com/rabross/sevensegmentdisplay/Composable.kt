package com.rabross.sevensegmentdisplay

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.time.ExperimentalTime

@ExperimentalTime
@Preview
@Composable
fun SevenSegmentDisplayPreview() {
    Surface(Modifier.fillMaxSize()) {
        SevenSegmentDisplay()
    }
}

@Composable
fun SevenSegmentDisplay(
    modifier: Modifier = Modifier,
    segmentScale: Int = 3,
    led: Led = SingleColorLed(Color.Red, Color.DarkGray),
    decoder: Decoder = BinaryDecoder()
) {
    Canvas(modifier = modifier.fillMaxSize()) {

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

        drawHorizontalSegment(led, aOffset, horizontalSegmentSize, decoder.a)
        drawVerticalSegment(led, bOffset, verticalSegmentSize, decoder.b)
        drawVerticalSegment(led, cOffset, verticalSegmentSize, decoder.c)
        drawHorizontalSegment(led, dOffset, horizontalSegmentSize, decoder.d)
        drawVerticalSegment(led, eOffset, verticalSegmentSize, decoder.e)
        drawVerticalSegment(led, fOffset, verticalSegmentSize, decoder.f)
        drawHorizontalSegment(led, gOffset, horizontalSegmentSize, decoder.g)
    }
}

private fun DrawScope.drawHorizontalSegment(led: Led, offset: Offset, size: Size, signal: Int) {
    val radius = size.minDimension / 2
    val centerX: Float = offset.x + size.width / 2
    val centerY: Float = offset.y + size.height / 2
    val spacing = radius / 10
    val hexagonPath = Path()
    hexagonPath.moveTo(centerX, centerY + radius - spacing)
    hexagonPath.lineTo(centerX - size.width/2, centerY + radius - spacing)
    hexagonPath.lineTo(centerX - size.width/2 - radius + spacing, centerY)
    hexagonPath.lineTo(centerX - size.width/2, centerY - radius + spacing)
    hexagonPath.lineTo(centerX + size.width/2, centerY - radius + spacing)
    hexagonPath.lineTo(centerX + size.width/2 + radius - spacing, centerY)
    hexagonPath.lineTo(centerX + size.width/2, centerY + radius - spacing)
    hexagonPath.moveTo(centerX, centerY + radius - spacing)
    drawPath(hexagonPath, led.signal(signal))
}

private fun DrawScope.drawVerticalSegment(led: Led, offset: Offset, size: Size, signal: Int) {
    val radius = size.minDimension / 2
    val centerX: Float = offset.x + size.width / 2
    val centerY: Float = offset.y + size.height / 2
    val spacing = radius / 10
    val hexagonPath = Path()
    hexagonPath.moveTo(centerX, centerY + radius + size.height/2 - spacing)
    hexagonPath.lineTo(centerX - radius + spacing, centerY + size.height/2)
    hexagonPath.lineTo(centerX - radius + spacing, centerY - size.height/2)
    hexagonPath.lineTo(centerX, centerY - radius - size.height/2 + spacing)
    hexagonPath.lineTo(centerX + radius - spacing, centerY - size.height/2)
    hexagonPath.lineTo(centerX + radius - spacing, centerY + size.height/2)
    hexagonPath.moveTo(centerX, centerY + radius + size.height/2 - spacing)
    drawPath(hexagonPath, led.signal(signal))
}


@Composable
fun DigitalClockDisplay(
    modifier: Modifier = Modifier,
    hourFirst: Int = 0,
    hourSecond: Int = 0,
    minuteFirst: Int = 0,
    minuteSecond: Int = 0,
    secondFirst: Int = 0,
    secondSecond: Int = 0,
) {
    Row(modifier = modifier) {
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f),
            decoder = BinaryDecoder(hourFirst)
        )
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f),
            decoder = BinaryDecoder(hourSecond)
        )
        Spacer(modifier = Modifier.width(24.dp))
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f),
            decoder = BinaryDecoder(minuteFirst)
        )
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f),
            decoder = BinaryDecoder(minuteSecond)
        )
        Spacer(modifier = Modifier.width(24.dp))
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f),
            decoder = BinaryDecoder(secondFirst)
        )
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f),
            decoder = BinaryDecoder(secondSecond)
        )
    }
}