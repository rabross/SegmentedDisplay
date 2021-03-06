package com.rabross.segmenteddisplay.fourteen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
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
import com.rabross.segmenteddisplay.delimiter.BinaryDecoder as DelimiterBinaryDecoder
import com.rabross.segmenteddisplay.Led
import com.rabross.segmenteddisplay.SingleColorLed
import com.rabross.segmenteddisplay.delimiter.Delimiter
import kotlin.math.pow
import kotlin.math.sqrt

@Preview
@Composable
fun FourteenSegmentDisplayPreview() {
    Surface {
        SegmentDisplay()
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
        SegmentDisplay(modifier = modifier.padding(4.dp),
            decoder = BinaryDecoder(hourFirst)
        )
        SegmentDisplay(modifier = modifier.padding(4.dp),
            decoder = BinaryDecoder(hourSecond)
        )
        Delimiter(modifier = modifier.padding(4.dp),
            decoder = DelimiterBinaryDecoder(delimiterSignal)
        )
        SegmentDisplay(modifier = modifier.padding(4.dp),
            decoder = BinaryDecoder(minuteFirst)
        )
        SegmentDisplay(modifier = modifier.padding(4.dp),
            decoder = BinaryDecoder(minuteSecond)
        )
        Delimiter(modifier = modifier.padding(4.dp),
            decoder = DelimiterBinaryDecoder(delimiterSignal)
        )
        SegmentDisplay(modifier = modifier.padding(4.dp),
            decoder = BinaryDecoder(secondFirst)
        )
        SegmentDisplay(modifier = modifier.padding(4.dp),
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
        .size(100.dp)) {

        val scaleWidth = 1 + segmentScale + 1
        val scaleHeight = 1 + segmentScale + 1 + segmentScale + 1

        val segmentWidthByWidth = size.width / scaleWidth
        val segmentWidthByHeight = size.height / scaleHeight

        val segmentWidth = if (scaleHeight * segmentWidthByWidth < size.height) segmentWidthByWidth else segmentWidthByHeight
        val segmentLength = segmentScale * segmentWidth

        val fullWidth = segmentWidth * scaleWidth
        val quarterGapWidth = (fullWidth - segmentWidth * 3)/2

        val horizontalSegmentSize = Size(segmentLength, segmentWidth)
        val smallHorizontalSegmentSize = Size(segmentLength / 2, segmentWidth)
        val verticalSegmentSize = Size(segmentWidth, segmentLength)
        val smallVerticalSegmentSize = Size(segmentWidth, segmentLength - segmentWidth/2)
        val diagonalSegmentSize = Size(quarterGapWidth, segmentLength)

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

        drawSegmentA(led.signal(decoder.a), aOffset, horizontalSegmentSize)
        drawSegmentB(led.signal(decoder.b), bOffset, verticalSegmentSize)
        drawSegmentC(led.signal(decoder.c), cOffset, verticalSegmentSize)
        drawSegmentD(led.signal(decoder.d), dOffset, horizontalSegmentSize)
        drawSegmentE(led.signal(decoder.e), eOffset, verticalSegmentSize)
        drawSegmentF(led.signal(decoder.f), fOffset, verticalSegmentSize)
        drawSegmentG1(led.signal(decoder.g1), g1Offset, smallHorizontalSegmentSize)
        drawSegmentG2(led.signal(decoder.g2), g2Offset, smallHorizontalSegmentSize)
        drawSegmentH(led.signal(decoder.h), segmentWidth, hOffset, diagonalSegmentSize)
        drawSegmentI(led.signal(decoder.i), iOffset, smallVerticalSegmentSize)
        drawSegmentJ(led.signal(decoder.j), segmentWidth, jOffset, diagonalSegmentSize)
        drawSegmentK(led.signal(decoder.k), segmentWidth, kOffset, diagonalSegmentSize)
        drawSegmentL(led.signal(decoder.l), lOffset, smallVerticalSegmentSize)
        drawSegmentM(led.signal(decoder.m), segmentWidth, mOffset, diagonalSegmentSize)
    }
}

private fun DrawScope.drawSegmentA(c: Color, o: Offset, s: Size) = drawHorizontalSegment(c, o, s)
private fun DrawScope.drawSegmentB(c: Color, o: Offset, s: Size) = drawVerticalSegment(c, o, s)
private fun DrawScope.drawSegmentC(c: Color, o: Offset, s: Size) = drawVerticalSegment(c, o, s)
private fun DrawScope.drawSegmentD(c: Color, o: Offset, s: Size) = drawHorizontalSegment(c, o, s)
private fun DrawScope.drawSegmentE(c: Color, o: Offset, s: Size) = drawVerticalSegment(c, o, s)
private fun DrawScope.drawSegmentF(c: Color, o: Offset, s: Size) = drawVerticalSegment(c, o, s)
private fun DrawScope.drawSegmentG1(c: Color, o: Offset, s: Size) = drawSmallHorizontalSegment(c, o, s)
private fun DrawScope.drawSegmentG2(c: Color, o: Offset, s: Size) = drawSmallHorizontalSegment(c, o, s)
private fun DrawScope.drawSegmentH(c: Color, w: Float, o: Offset, s: Size) = drawBackSlashSegment(c, w, o, s)
private fun DrawScope.drawSegmentI(c: Color, o: Offset, s: Size) = drawSmallVerticalSegment(c, o, s)
private fun DrawScope.drawSegmentJ(c: Color, w: Float, o: Offset, s: Size) = drawForwardSlashSegment(c, w, o, s)
private fun DrawScope.drawSegmentK(c: Color, w: Float, o: Offset, s: Size) = drawForwardSlashSegment(c, w, o, s)
private fun DrawScope.drawSegmentL(c: Color, o: Offset, s: Size) = drawSmallVerticalSegment(c, o, s)
private fun DrawScope.drawSegmentM(c: Color, w: Float, o: Offset, s: Size) = drawBackSlashSegment(c, w, o, s)

private fun DrawScope.drawHorizontalSegment(color: Color, offset: Offset, size: Size) {
    val radius = size.minDimension / 2
    val centerX = offset.x + size.width / 2
    val centerY = offset.y + size.height / 2
    val spacing = radius / 10

    val hexagonPath = Path().apply {
        moveTo(centerX - size.width/2, centerY + radius - spacing)
        lineTo(centerX - size.width/2 - radius + spacing, centerY)
        lineTo(centerX - size.width/2, centerY - radius + spacing)
        lineTo(centerX + size.width/2, centerY - radius + spacing)
        lineTo(centerX + size.width/2 + radius - spacing, centerY)
        lineTo(centerX + size.width/2, centerY + radius - spacing)
    }

    drawPath(hexagonPath, color)
}

private fun DrawScope.drawVerticalSegment(color: Color, offset: Offset, size: Size) {
    val radius = size.minDimension / 2
    val centerX = offset.x + size.width / 2
    val centerY = offset.y + size.height / 2
    val spacing = radius / 10

    val hexagonPath = Path().apply {
        moveTo(centerX, centerY + radius + size.height / 2 - spacing)
        lineTo(centerX - radius + spacing, centerY + size.height / 2)
        lineTo(centerX - radius + spacing, centerY - size.height / 2)
        lineTo(centerX, centerY - radius - size.height / 2 + spacing)
        lineTo(centerX + radius - spacing, centerY - size.height / 2)
        lineTo(centerX + radius - spacing, centerY + size.height / 2)
    }

    drawPath(hexagonPath, color)
}

private fun DrawScope.drawSmallHorizontalSegment(color: Color, offset: Offset, size: Size) {
    val radius = size.minDimension / 2
    val centerX = offset.x + size.width / 2
    val centerY = offset.y + size.height / 2
    val spacing = radius / 10

    val hexagonPath = Path().apply {
        moveTo(centerX - size.width / 2, centerY + radius - spacing)
        lineTo(centerX - size.width / 2 - radius + spacing, centerY)
        lineTo(centerX - size.width / 2, centerY - radius + spacing)
        lineTo(centerX + size.width / 2 - radius, centerY - radius + spacing)
        lineTo(centerX + size.width / 2 - spacing, centerY)
        lineTo(centerX + size.width / 2 - radius, centerY + radius - spacing)
    }

    drawPath(hexagonPath, color)
}

private fun DrawScope.drawSmallVerticalSegment(color: Color, offset: Offset, size: Size) {
    val radius = size.minDimension / 2
    val centerX = offset.x + size.width / 2
    val centerY = offset.y + size.height / 2
    val spacing = radius / 10

    val hexagonPath = Path().apply {
        moveTo(centerX, centerY + radius + size.height / 2 - spacing)
        lineTo(centerX - radius + spacing, centerY + size.height / 2)
        lineTo(centerX - radius + spacing, centerY - size.height / 2)
        lineTo(centerX, centerY - radius - size.height / 2 + spacing)
        lineTo(centerX + radius - spacing, centerY - size.height / 2)
        lineTo(centerX + radius - spacing, centerY + size.height / 2)
    }

    drawPath(hexagonPath, color)
}

private fun DrawScope.drawBackSlashSegment(color: Color, width: Float, offset: Offset, size: Size) {
    val diagonalWidth = sqrt(width.pow(2)/2) * 2
    val spacing = diagonalWidth / 40

    val parallelogram = Path().apply {
        moveTo(offset.x + spacing, offset.y + spacing)
        lineTo(offset.x + size.width - spacing, offset.y + size.height - diagonalWidth)
        lineTo(offset.x + size.width - spacing, offset.y + size.height - spacing)
        lineTo(offset.x + spacing, offset.y + diagonalWidth)
    }

    drawPath(parallelogram, color)
}

private fun DrawScope.drawForwardSlashSegment(color: Color, width: Float, offset: Offset, size: Size) {
    val diagonalWidth = sqrt(width.pow(2)/2) * 2
    val spacing = diagonalWidth / 40

    val parallelogram = Path().apply {
        moveTo(offset.x + spacing, offset.y + size.height - spacing)
        lineTo(offset.x + spacing, offset.y + size.height - diagonalWidth)
        lineTo(offset.x + size.width - spacing, offset.y + spacing)
        lineTo(offset.x + size.width - spacing, offset.y + diagonalWidth)
    }

    drawPath(parallelogram, color)
}
