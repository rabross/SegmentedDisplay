package com.rabross.segmenteddisplay.app

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.unit.dp
import com.rabross.segmenteddisplay.seven.SevenSegmentDecoder
import com.rabross.segmenteddisplay.seven.SevenSegmentDisplay
import java.nio.ByteBuffer
import java.security.InvalidParameterException
import kotlin.time.ExperimentalTime

@ExperimentalTime
class SevenSegmentScreenActivity : ComponentActivity() {

    private val rows = 1
    private val columns = 1

    private val bitmap = Bitmap.createBitmap(2, 5, Bitmap.Config.ALPHA_8)
    private val canvas = Canvas(bitmap)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        canvas.drawColor(Color.TRANSPARENT)
        canvas.drawLine(0f, 2f, 2f, 2f, Paint().apply{
            color = Color.WHITE
        })

        val screenBuffer = ByteBuffer.allocate(bitmap.allocationByteCount)
        bitmap.copyPixelsToBuffer(screenBuffer)

        setContent {

            Surface(color = ComposeColor.Black) {
                Column(modifier = Modifier.padding(24.dp)) {
                    for (row in 0 until rows) {
                        Row {
                            for (column in 0 until columns) {
                                val sevenSegmentIndex = (row * columns) + column
                                SevenSegmentDisplay(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(2.dp),
                                    decoder = BufferDecoder(screenBuffer, 2, 5, 1, 1, sevenSegmentIndex)
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
                    ) : SevenSegmentDecoder {
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
internal fun mapSingleSegmentToBufferIndex(imageBufferWidth: Int,
                                                      imageBufferHeight: Int,
                                                      sevenSegmentCountX: Int,
                                                      sevenSegmentCountY: Int,
                                                      sevenSegmentIndex: Int,
                                                      segmentIndex: Int): Int {
    val segmentWidth = 2
    val segmentHeight = 5

    if(imageBufferWidth % segmentWidth > 0 || imageBufferHeight % segmentHeight > 0) throw InvalidParameterException("Image buffer must be divisible by 7-segment size 2x5")
    if(imageBufferWidth < sevenSegmentCountX * segmentWidth) throw InvalidParameterException("Image buffer width must be large enough to support given 7-segment grid width")
    if(imageBufferHeight < sevenSegmentCountY * segmentHeight) throw InvalidParameterException("Image buffer height must be large enough to support given 7-segment grid height")

    val segmentOffsetX = sevenSegmentIndex % sevenSegmentCountX
    val segmentOffsetY = sevenSegmentIndex / sevenSegmentCountY

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