package com.rabross.segmenteddisplay.delimiter

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rabross.segmenteddisplay.Led
import com.rabross.segmenteddisplay.SingleColorLed
import com.rabross.segmenteddisplay.seven.BinaryDecoder as SevenSegmentBinaryDecoder
import com.rabross.segmenteddisplay.seven.SevenSegmentDisplay

@Preview
@Composable
fun DelimiterPreview() {
    Surface {
        Delimiter()
    }
}

@Preview
@Composable
fun DigitalClockDisplayPreview() {
    Surface {
        DigitalClockSevenSegmentDisplay()
    }
}

@Composable
fun Delimiter(
    modifier: Modifier = Modifier,
    segmentScale: Int = 3,
    led: Led = SingleColorLed(Color.Red, Color.DarkGray.copy(alpha = 0.3f)),
    decoder: Decoder = BinaryDecoder()
) {
    Canvas(
        modifier = modifier
            .aspectRatio(((1f + segmentScale + 1f)) / (1f + segmentScale + 1f + segmentScale + 1f), true)
            .size(100.dp)
    ) {
        val scaleWidth = 1 + segmentScale + 1
        val scaleHeight = 1 + segmentScale + 1 + segmentScale + 1

        val segmentWidthByWidth = size.width / scaleWidth
        val segmentWidthByHeight = size.height / scaleHeight

        val segmentWidth = if (scaleHeight * segmentWidthByWidth < size.height) segmentWidthByWidth else segmentWidthByHeight

        val totalWidth = segmentWidth * scaleWidth
        val totalHeight = segmentWidth * scaleHeight

        drawDelimiter(
            led.signal(decoder.a),
            led.signal(decoder.b),
            segmentWidth / 2,
            Offset(0f, segmentWidth / 2),
            Size(totalWidth, totalHeight - segmentWidth)
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
            modifier = Modifier.weight(1f).padding(4.dp),
            decoder = SevenSegmentBinaryDecoder(hourFirst)
        )
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f).padding(4.dp),
            decoder = SevenSegmentBinaryDecoder(hourSecond)
        )
        Delimiter(modifier = Modifier.weight(1f).padding(4.dp),
            decoder = BinaryDecoder(delimiterSignal)
        )
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f).padding(4.dp),
            decoder = SevenSegmentBinaryDecoder(minuteFirst)
        )
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f).padding(4.dp),
            decoder = SevenSegmentBinaryDecoder(minuteSecond)
        )
        Delimiter(modifier = Modifier.weight(1f).padding(4.dp),
            decoder = BinaryDecoder(delimiterSignal)
        )
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f).padding(4.dp),
            decoder = SevenSegmentBinaryDecoder(secondFirst)
        )
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f).padding(4.dp),
            decoder = SevenSegmentBinaryDecoder(secondSecond)
        )
    }
}