package com.rabross.segmenteddisplay.app

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.graphics.scale
import com.rabross.segmenteddisplay.seven.Decoder
import com.rabross.segmenteddisplay.seven.SegmentDisplay
import java.nio.ByteBuffer
import java.security.InvalidParameterException
import androidx.compose.ui.graphics.Color as ComposeColor

class SevenSegmentScreenActivity : ComponentActivity() {

    private val columns = 20
    private val rows = 6
    private val bufferWidth = 40
    private val bufferHeight = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bitmap = Bitmap.createBitmap(bufferWidth, bufferHeight, Bitmap.Config.ALPHA_8)
        val screenBuffer = ByteBuffer.allocate(bitmap.allocationByteCount)
        Canvas(bitmap).drawBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.bitmap)
                .scale(bufferWidth, bufferHeight), 0f, 0f, null
        )
        bitmap.copyPixelsToBuffer(screenBuffer)

        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = ComposeColor.Black) {
                Column(modifier = Modifier.padding(24.dp)) {
                    for (row in 0 until rows) {
                        Row {
                            for (column in 0 until columns) {
                                val sevenSegmentIndex = (row * columns) + column
                                SegmentDisplay(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(2.dp),
                                    decoder = BufferDecoder(screenBuffer, bufferWidth, bufferHeight, columns, rows, sevenSegmentIndex)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

class BufferDecoder(
    buffer: ByteBuffer,
    bufferWidth: Int,
    bufferHeight: Int,
    sevenSegmentCountX: Int,
    sevenSegmentCountY: Int,
    sevenSegmentIndex: Int
) : Decoder {

    override val a = buffer.get(mapSingleSegmentToBufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex, 0)).toInt() and 0b00000001
    override val b = buffer.get(mapSingleSegmentToBufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex, 1)).toInt() and 0b00000010
    override val c = buffer.get(mapSingleSegmentToBufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex, 2)).toInt() and 0b00000100
    override val d = buffer.get(mapSingleSegmentToBufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex, 3)).toInt() and 0b00001000
    override val e = buffer.get(mapSingleSegmentToBufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex, 4)).toInt() and 0b00010000
    override val f = buffer.get(mapSingleSegmentToBufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex, 5)).toInt() and 0b00100000
    override val g = buffer.get(mapSingleSegmentToBufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex, 6)).toInt() and 0b01000000

}

/*
Each segment is 2x5 pixels
0,0 = A
0,1 = F
1,1 = B
0,2 = G
0,3 = E
1,3 = C
0,4 = D

Segment index:
A = 0
B = 1
C = 2
D = 3
E = 4
F = 5
G = 6
 */
internal fun mapSingleSegmentToBufferIndex(
    imageBufferWidth: Int,
    imageBufferHeight: Int,
    sevenSegmentCountX: Int,
    sevenSegmentCountY: Int,
    sevenSegmentIndex: Int,
    segmentIndex: Int
): Int {
    val segmentWidth = 2
    val segmentHeight = 5

    if(imageBufferWidth % segmentWidth > 0 || imageBufferHeight % segmentHeight > 0) throw InvalidParameterException("Image buffer must be divisible by 7-segment size 2x5")
    if(imageBufferWidth < sevenSegmentCountX * segmentWidth) throw InvalidParameterException("Image buffer width must be large enough to support given 7-segment grid width")
    if(imageBufferHeight < sevenSegmentCountY * segmentHeight) throw InvalidParameterException("Image buffer height must be large enough to support given 7-segment grid height")

    val segmentOffsetX = sevenSegmentIndex % sevenSegmentCountX
    val segmentOffsetY = sevenSegmentIndex / sevenSegmentCountX

    val imageOffsetX = segmentOffsetX * segmentWidth
    val imageOffsetY = segmentOffsetY * segmentHeight

    val imageCoordinate = when(segmentIndex) {
        /*A*/ 0 -> imageOffsetX to imageOffsetY
        /*B*/ 1 -> imageOffsetX + 1 to imageOffsetY + 1
        /*C*/ 2 -> imageOffsetX + 1 to imageOffsetY + 3
        /*D*/ 3 -> imageOffsetX to imageOffsetY + 4
        /*E*/ 4 -> imageOffsetX to imageOffsetY + 3
        /*F*/ 5 -> imageOffsetX to imageOffsetY + 1
        /*G*/ 6 -> imageOffsetX to imageOffsetY + 2
        else -> 0 to 0
    }

    return imageCoordinate.second * imageBufferWidth + imageCoordinate.first
}