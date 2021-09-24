package com.rabross.sevensegmentdisplay

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.pow
import kotlin.math.sqrt

@ExperimentalTime
@Preview
@Composable
fun SevenSegmentDisplayPreview() {
    Surface(Modifier.fillMaxSize()) {
        SevenSegmentDisplay()
    }
}

@Preview
@Composable
fun DigitalClockDisplayPreview() {
    Surface(Modifier.fillMaxSize()) {
        DigitalClockSevenSegmentDisplay()
    }
}

@Preview
@Composable
fun FourteenSegmentDisplayPreview() {
    Surface(Modifier.fillMaxSize()) {
        FourteenSegmentDisplay(decoder = BinaryFourteenSegmentDecoder(0b10000000))
    }
}

@Composable
fun SevenSegmentDisplay(
    modifier: Modifier = Modifier,
    segmentScale: Int = 3,
    hasBorder: Boolean = true,
    led: Led = SingleColorLed(Color.Red, Color.DarkGray.copy(alpha = 0.3f)),
    decoder: SevenSegmentDecoder = BinarySevenSegmentDecoder()
) {
    Canvas(modifier = modifier.fillMaxSize()) {

        val scaleWidth = 1 + segmentScale + 1 + hasBorder.toInt()
        val scaleHeight = 1 + segmentScale + 1 + segmentScale + 1 + hasBorder.toInt()

        val segmentWidthByWidth = size.width / scaleWidth
        val segmentWidthByHeight = size.height / scaleHeight

        val segmentWidth = if (scaleHeight * segmentWidthByWidth < size.height) segmentWidthByWidth else segmentWidthByHeight
        val segmentLength = segmentScale * segmentWidth

        val horizontalSegmentSize = Size(segmentLength, segmentWidth)
        val verticalSegmentSize = Size(segmentWidth, segmentLength)

        val border = if(hasBorder) segmentWidth / 2f else 0f

        val aOffset = Offset(segmentWidth + border, 0f + border)
        val bOffset = Offset(segmentLength + segmentWidth + border, segmentWidth + border)
        val cOffset = Offset(segmentLength + segmentWidth + border, segmentLength + segmentWidth + segmentWidth + border)
        val dOffset = Offset(segmentWidth + border, segmentLength + segmentWidth + segmentLength + segmentWidth + border)
        val eOffset = Offset(0f + border, segmentLength + segmentWidth + segmentWidth + border)
        val fOffset = Offset(0f + border, segmentWidth + border)
        val gOffset = Offset(segmentWidth + border, segmentLength + segmentWidth + border)

        drawHorizontalSegment(led.signal(decoder.a), aOffset, horizontalSegmentSize)
        drawVerticalSegment(led.signal(decoder.b), bOffset, verticalSegmentSize)
        drawVerticalSegment(led.signal(decoder.c), cOffset, verticalSegmentSize)
        drawHorizontalSegment(led.signal(decoder.d), dOffset, horizontalSegmentSize)
        drawVerticalSegment(led.signal(decoder.e), eOffset, verticalSegmentSize)
        drawVerticalSegment(led.signal(decoder.f), fOffset, verticalSegmentSize)
        drawHorizontalSegment(led.signal(decoder.g), gOffset, horizontalSegmentSize)
    }
}

private fun DrawScope.drawHorizontalSegment(color: Color, offset: Offset, size: Size) {
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
    drawPath(hexagonPath, color)
}

private fun DrawScope.drawVerticalSegment(color: Color, offset: Offset, size: Size) {
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
    drawPath(hexagonPath, color)
}

@Composable
fun Delimiter(
    modifier: Modifier = Modifier,
    segmentScale: Int = 3,
    hasBorder: Boolean = true,
    led: Led = SingleColorLed(Color.Red, Color.DarkGray.copy(alpha = 0.3f)),
    decoder: DelimiterDecoder = BinaryDelimiterDecoder()
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val scaleWidth = 1 + segmentScale + 1 + hasBorder.toInt()
        val scaleHeight = 1 + segmentScale + 1 + segmentScale + 1 + hasBorder.toInt()

        val segmentWidthByWidth = size.width * 2 / scaleWidth
        val segmentWidthByHeight = size.height / scaleHeight

        val segmentWidth = if (scaleHeight * segmentWidthByWidth < size.height) segmentWidthByWidth else segmentWidthByHeight

        val border = if (hasBorder) segmentWidth / 2f else 0f

        val totalWidth = segmentWidth * (scaleWidth) / 2
        val totalHeight = segmentWidth * scaleHeight

        drawDelimiter(
            led.signal(decoder.a),
            led.signal(decoder.b),
            segmentWidth / 2,
            Offset(border / 2, segmentWidth / 2 + border / 2),
            Size(totalWidth - border, totalHeight - segmentWidth - border)
        )
    }
}

private fun DrawScope.drawDelimiter(upDotColor: Color, downDotColor: Color, radius: Float, offset: Offset, size: Size) {
    val upDotCenterOffset = Offset(size.width / 2 + offset.x, size.height / 4 + offset.y)
    val downDotCenterOffset = Offset(size.width / 2 + offset.x, size.height / 4 * 3 + offset.y)
    drawCircle(upDotColor, radius, upDotCenterOffset)
    drawCircle(downDotColor, radius, downDotCenterOffset)
}

@Composable
fun DigitalClockSevenSegmentDisplay(
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
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f),
            decoder = BinarySevenSegmentDecoder(hourFirst)
        )
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f),
            decoder = BinarySevenSegmentDecoder(hourSecond)
        )
        Delimiter(modifier = Modifier.weight(0.5f), decoder = BinaryDelimiterDecoder(delimiterSignal))
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f),
            decoder = BinarySevenSegmentDecoder(minuteFirst)
        )
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f),
            decoder = BinarySevenSegmentDecoder(minuteSecond)
        )
        Delimiter(modifier = Modifier.weight(0.5f), decoder = BinaryDelimiterDecoder(delimiterSignal))
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f),
            decoder = BinarySevenSegmentDecoder(secondFirst)
        )
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f),
            decoder = BinarySevenSegmentDecoder(secondSecond)
        )
    }
}
@Composable
fun DigitalClockFourteenSegmentDisplay(
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
        FourteenSegmentDisplay(
            modifier = Modifier.weight(1f),
            decoder = BinaryFourteenSegmentDecoder(hourFirst)
        )
        FourteenSegmentDisplay(
            modifier = Modifier.weight(1f),
            decoder = BinaryFourteenSegmentDecoder(hourSecond)
        )
        Delimiter(modifier = Modifier.weight(0.5f), decoder = BinaryDelimiterDecoder(delimiterSignal))
        FourteenSegmentDisplay(
            modifier = Modifier.weight(1f),
            decoder = BinaryFourteenSegmentDecoder(minuteFirst)
        )
        FourteenSegmentDisplay(
            modifier = Modifier.weight(1f),
            decoder = BinaryFourteenSegmentDecoder(minuteSecond)
        )
        Delimiter(modifier = Modifier.weight(0.5f), decoder = BinaryDelimiterDecoder(delimiterSignal))
        FourteenSegmentDisplay(
            modifier = Modifier.weight(1f),
            decoder = BinaryFourteenSegmentDecoder(secondFirst)
        )
        FourteenSegmentDisplay(
            modifier = Modifier.weight(1f),
            decoder = BinaryFourteenSegmentDecoder(secondSecond)
        )
    }
}

private fun Boolean.toInt() = this.compareTo(false)

@Composable
fun FourteenSegmentDisplay(
    modifier: Modifier = Modifier,
    segmentScale: Int = 4,
    led: Led = SingleColorLed(Color.Red, Color.DarkGray.copy(alpha = 0.3f)),
    decoder: FourteenSegmentDecoder = BinaryFourteenSegmentDecoder()
) {
    Canvas(modifier = modifier.fillMaxSize()) {

        val scaleWidth = 1 + segmentScale + 1
        val scaleHeight = 1 + segmentScale + 1 + segmentScale + 1

        val segmentWidthByWidth = size.width / scaleWidth
        val segmentWidthByHeight = size.height / scaleHeight

        val segmentWidth = if (scaleHeight * segmentWidthByWidth < size.height) segmentWidthByWidth else segmentWidthByHeight
        val segmentLength = segmentScale * segmentWidth

        val horizontalSegmentSize = Size(segmentLength, segmentWidth)
        val smallHorizontalSegmentSize = Size(segmentLength / 2, segmentWidth)
        val verticalSegmentSize = Size(segmentWidth, segmentLength)
        val smallVerticalSegmentSize = Size(segmentWidth, segmentLength - segmentWidth/2)
        val diagonalSegmentSize = Size(segmentWidth * 1.5f, segmentLength)

        val aOffset = Offset(segmentWidth, 0f)
        val bOffset = Offset(segmentLength + segmentWidth, segmentWidth)
        val cOffset = Offset(segmentLength + segmentWidth, segmentLength + segmentWidth + segmentWidth)
        val dOffset = Offset(segmentWidth, segmentLength + segmentWidth + segmentLength + segmentWidth)
        val eOffset = Offset(0f, segmentLength + segmentWidth + segmentWidth)
        val fOffset = Offset(0f, segmentWidth)
        val g1Offset = Offset(segmentWidth, segmentLength + segmentWidth)
        val g2Offset = Offset(center.x + segmentWidth/2, segmentLength + segmentWidth)
        val hOffset = Offset(segmentWidth, segmentWidth)
        val iOffset = Offset(center.x - segmentWidth/2, segmentWidth + segmentWidth/2)
        val jOffset = Offset(segmentLength - segmentWidth/2, segmentWidth)
        val kOffset = Offset(segmentWidth, segmentLength + segmentWidth * 2)
        val lOffset = Offset(center.x - segmentWidth/2, segmentLength + segmentWidth + segmentWidth)
        val mOffset = Offset(segmentLength - segmentWidth/2, segmentLength + segmentWidth + segmentWidth)

        drawHorizontalSegment(led.signal(decoder.a), aOffset, horizontalSegmentSize)
        drawVerticalSegment(led.signal(decoder.b), bOffset, verticalSegmentSize)
        drawVerticalSegment(led.signal(decoder.c), cOffset, verticalSegmentSize)
        drawHorizontalSegment(led.signal(decoder.d), dOffset, horizontalSegmentSize)
        drawVerticalSegment(led.signal(decoder.e), eOffset, verticalSegmentSize)
        drawVerticalSegment(led.signal(decoder.f), fOffset, verticalSegmentSize)
        drawSmallHorizontalSegment(led.signal(decoder.g1), g1Offset, smallHorizontalSegmentSize)
        drawSmallHorizontalSegment(led.signal(decoder.g2), g2Offset, smallHorizontalSegmentSize)
        drawBackSlashSegment(led.signal(decoder.h), segmentWidth, hOffset, diagonalSegmentSize)
        drawSmallVerticalSegment(led.signal(decoder.i), iOffset, smallVerticalSegmentSize)
        drawForwardSlashSegment(led.signal(decoder.j), segmentWidth, jOffset, diagonalSegmentSize)
        drawForwardSlashSegment(led.signal(decoder.k), segmentWidth, kOffset, diagonalSegmentSize)
        drawSmallVerticalSegment(led.signal(decoder.l), lOffset, smallVerticalSegmentSize)
        drawBackSlashSegment(led.signal(decoder.m), segmentWidth, mOffset, diagonalSegmentSize)
    }
}

private fun DrawScope.drawSmallHorizontalSegment(color: Color, offset: Offset, size: Size) {
    val radius = size.minDimension / 2
    val centerX: Float = offset.x + size.width / 2
    val centerY: Float = offset.y + size.height / 2
    val spacing = radius / 10
    val hexagonPath = Path()
    hexagonPath.moveTo(centerX, centerY + radius - spacing)
    hexagonPath.lineTo(centerX - size.width/2, centerY + radius - spacing)
    hexagonPath.lineTo(centerX - size.width/2 - radius + spacing, centerY)
    hexagonPath.lineTo(centerX - size.width/2, centerY - radius + spacing)
    hexagonPath.lineTo(centerX + size.width/2 - radius, centerY - radius + spacing)
    hexagonPath.lineTo(centerX + size.width/2 - spacing, centerY)
    hexagonPath.lineTo(centerX + size.width/2 - radius, centerY + radius - spacing)
    hexagonPath.moveTo(centerX, centerY + radius - spacing)
    drawPath(hexagonPath, color)
}

private fun DrawScope.drawSmallVerticalSegment(color: Color, offset: Offset, size: Size) {
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
    drawPath(hexagonPath, color)
}

private fun DrawScope.drawBackSlashSegment(color: Color, width: Float, offset: Offset, size: Size) {
    val calcWidth = sqrt(width.pow(2)/2) * 2
    val spacing = calcWidth / 40

    val parallelogram = Path()
    parallelogram.moveTo(offset.x + spacing, offset.y + spacing)
    parallelogram.lineTo(offset.x + size.width - spacing, offset.y + size.height - calcWidth)
    parallelogram.lineTo(offset.x + size.width - spacing, offset.y + size.height - spacing)
    parallelogram.lineTo(offset.x + spacing, offset.y + calcWidth)
    parallelogram.lineTo(offset.x + spacing, offset.y + spacing)

    drawPath(parallelogram, color)
}

private fun DrawScope.drawForwardSlashSegment(color: Color, width: Float, offset: Offset, size: Size) {
    val calcWidth = sqrt(width.pow(2)/2) * 2
    val spacing = calcWidth / 40

    val parallelogram = Path()
    parallelogram.moveTo(offset.x + spacing, offset.y + size.height - spacing)
    parallelogram.lineTo(offset.x + spacing, offset.y + size.height - calcWidth)
    parallelogram.lineTo(offset.x + size.width - spacing, offset.y + spacing)
    parallelogram.lineTo(offset.x + size.width - spacing, offset.y + calcWidth)
    parallelogram.lineTo(offset.x + spacing, offset.y + size.height - spacing)
    drawPath(parallelogram, color)
}