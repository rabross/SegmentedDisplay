package com.rabross.segmenteddisplay.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rabross.segmenteddisplay.seven.BinaryDecoder
import com.rabross.segmenteddisplay.seven.SevenSegmentDisplay
import kotlin.time.ExperimentalTime

@ExperimentalTime
class SevenSegmentHeartActivity : ComponentActivity() {

    private val rows = 6
    private val columns = 13

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {

            Surface(color = Color.Black) {
                Column(modifier = Modifier.padding(24.dp)) {
                    for (row in 0 until rows) {
                        Row {
                            for (column in 0 until columns) {
                                SevenSegmentDisplay(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(2.dp),
                                    decoder = BinaryDecoder(buffer[(row * columns) + column])
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private val buffer = listOf(
        0b0000000, 0b0000100, 0b1011110, 0b1111111, 0b1111111, 0b1111110, 0b0010000, 0b0000100, 0b1111110, 0b1111111, 0b1111110, 0b1011100, 0b0000000,
        0b0000100, 0b1111111, 0b1111111, 0b1111111, 0b1111111, 0b1111111, 0b1111101, 0b1111111, 0b1111111, 0b1111111, 0b1111111, 0b1111111, 0b1111101,
        0b0000010, 0b1111111, 0b1111111, 0b1111111, 0b1111111, 0b1111111, 0b1111111, 0b1111111, 0b1111111, 0b1111111, 0b1111111, 0b1111111, 0b1111011,
        0b0000000, 0b0000010, 0b1100111, 0b1111111, 0b1111111, 0b1111111, 0b1111111, 0b1111111, 0b1111111, 0b1111111, 0b1111111, 0b1100011, 0b0000000,
        0b0000000, 0b0000000, 0b0000000, 0b0000000, 0b0100011, 0b1110111, 0b1111111, 0b1111111, 0b1110011, 0b0100001, 0b0000000, 0b0000000, 0b0000000,
        0b0000000, 0b0000000, 0b0000000, 0b0000000, 0b0000000, 0b0000000, 0b1100111, 0b0100001, 0b0000000, 0b0000000, 0b0000000, 0b0000000, 0b0000000
    )
}