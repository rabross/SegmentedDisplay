package com.rabross.segmenteddisplay.app

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.scale
import com.rabross.segmenteddisplay.seven.Decoder
import com.rabross.segmenteddisplay.seven.SegmentDisplay
import java.nio.ByteBuffer
import java.security.InvalidParameterException
import androidx.compose.ui.graphics.Color as ComposeColor

class SevenSegmentScreenActivity : ComponentActivity() {

    private val columns = 25
    private val rows = 10
    private val bufferWidth = columns * 2
    private val bufferHeight = rows * 5
    private val imageResource = R.drawable.color_bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bitmap = Bitmap.createBitmap(bufferWidth, bufferHeight, Bitmap.Config.ARGB_8888)
            .also { Canvas(it).drawBitmap(
                    BitmapFactory.decodeResource(resources, imageResource)
                        .scale(bufferWidth, bufferHeight), 0f, 0f, null
                )
            }

        val screenBuffer = ByteBuffer.allocate(bitmap.allocationByteCount)
            .also { bitmap.copyPixelsToBuffer(it) }

        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = ComposeColor.Black) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Image(
                        modifier = Modifier
                            .background(color = ComposeColor.White)
                            .fillMaxWidth(),
                        painter = painterResource(id = imageResource),
                        contentDescription = "preview")
                    Spacer(modifier = Modifier.height(24.dp))
                    for (row in 0 until rows) {
                        Row {
                            for (column in 0 until columns) {
                                val sevenSegmentIndex = (row * columns) + column
                                SegmentDisplay(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(2.dp),
                                    decoder = ARGB8888BufferDecoder(screenBuffer, bufferWidth, bufferHeight, columns, rows, sevenSegmentIndex),
                                    led = { value -> ComposeColor(value) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

class Alpha8BufferDecoder(
    imageBuffer: ByteBuffer,
    bufferWidth: Int,
    bufferHeight: Int,
    sevenSegmentCountX: Int,
    sevenSegmentCountY: Int,
    sevenSegmentIndex: Int
) : Decoder {
    override val a = imageBuffer.get(0.toALPHA8BufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex)).toInt()
    override val b = imageBuffer.get(1.toALPHA8BufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex)).toInt()
    override val c = imageBuffer.get(2.toALPHA8BufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex)).toInt()
    override val d = imageBuffer.get(3.toALPHA8BufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex)).toInt()
    override val e = imageBuffer.get(4.toALPHA8BufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex)).toInt()
    override val f = imageBuffer.get(5.toALPHA8BufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex)).toInt()
    override val g = imageBuffer.get(6.toALPHA8BufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex)).toInt()
}

class ARGB8888BufferDecoder(
    imageBuffer: ByteBuffer,
    bufferWidth: Int,
    bufferHeight: Int,
    sevenSegmentCountX: Int,
    sevenSegmentCountY: Int,
    sevenSegmentIndex: Int
) : Decoder {
    override val a = imageBuffer.getInt(0.toARGB8888BufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex)).toARGB()
    override val b = imageBuffer.getInt(1.toARGB8888BufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex)).toARGB()
    override val c = imageBuffer.getInt(2.toARGB8888BufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex)).toARGB()
    override val d = imageBuffer.getInt(3.toARGB8888BufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex)).toARGB()
    override val e = imageBuffer.getInt(4.toARGB8888BufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex)).toARGB()
    override val f = imageBuffer.getInt(5.toARGB8888BufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex)).toARGB()
    override val g = imageBuffer.getInt(6.toARGB8888BufferIndex(bufferWidth, bufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex)).toARGB()

    //Pixel data read from the image buffer us stored at RGBA and must be converted to ARGB
    private fun Int.toARGB(): Int {
        return (this shl 24) or (this shr 8)
    }
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

internal fun Int.toALPHA8BufferIndex(
    imageBufferWidth: Int,
    imageBufferHeight: Int,
    sevenSegmentCountX: Int,
    sevenSegmentCountY: Int,
    sevenSegmentIndex: Int
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

    val imageCoordinate = when(this) {
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

internal fun Int.toARGB8888BufferIndex(
    imageBufferWidth: Int,
    imageBufferHeight: Int,
    sevenSegmentCountX: Int,
    sevenSegmentCountY: Int,
    sevenSegmentIndex: Int
): Int {
    return toALPHA8BufferIndex(imageBufferWidth, imageBufferHeight, sevenSegmentCountX, sevenSegmentCountY, sevenSegmentIndex) * 4 //bytes
}