package com.rabross.segmenteddisplay.delimiter

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rabross.segmenteddisplay.Led
import com.rabross.segmenteddisplay.SingleColorLed

@Preview
@Composable
fun DelimiterPreview() {
    Surface {
        Delimiter()
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
            .drawWithCache {
                val scaleWidth = 1 + segmentScale + 1
                val scaleHeight = 1 + segmentScale + 1 + segmentScale + 1

                val segmentWidthByWidth = size.width / scaleWidth
                val segmentWidthByHeight = size.height / scaleHeight

                val segmentWidth = if (scaleHeight * segmentWidthByWidth < size.height) segmentWidthByWidth else segmentWidthByHeight

                val totalWidth = segmentWidth * scaleWidth
                val totalHeight = segmentWidth * scaleHeight

                val radius = segmentWidth / 2
                val offset = Offset(0f, segmentWidth / 2)
                val drawingSize = Size(totalWidth, totalHeight - segmentWidth)

                val upDotCenterOffset = Offset(drawingSize.width / 2 + offset.x, drawingSize.height / 4 + offset.y)
                val downDotCenterOffset = Offset(drawingSize.width / 2 + offset.x, drawingSize.height / 4 * 3 + offset.y)

                onDrawBehind {
                    drawCircle(led.signal(decoder.a), radius, upDotCenterOffset)
                    drawCircle(led.signal(decoder.b), radius, downDotCenterOffset)
                }
            }
    ) {}
}
