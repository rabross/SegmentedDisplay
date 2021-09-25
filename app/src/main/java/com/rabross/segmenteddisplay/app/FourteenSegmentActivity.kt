package com.rabross.segmenteddisplay.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.rabross.segmenteddisplay.fourteen.BinaryFourteenSegmentDecoder
import com.rabross.segmenteddisplay.fourteen.DigitalClockFourteenSegmentDisplay
import com.rabross.segmenteddisplay.fourteen.FourteenSegmentDisplay
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import java.util.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
class FourteenSegmentActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val hourFirst = remember { mutableStateOf(0) }
            val hourSecond = remember { mutableStateOf(0) }
            val minuteFirst = remember { mutableStateOf(0) }
            val minuteSecond = remember { mutableStateOf(0) }
            val secondFirst = remember { mutableStateOf(0) }
            val secondSecond = remember { mutableStateOf(0) }

            Surface(color = Color.Black) {
                Column(modifier = Modifier.padding(24.dp)) {
                    DigitalClockFourteenSegmentDisplay(
                        Modifier.weight(1f),
                        BinaryFourteenSegmentDecoder.mapToDisplay(hourFirst.value),
                        BinaryFourteenSegmentDecoder.mapToDisplay(hourSecond.value),
                        BinaryFourteenSegmentDecoder.mapToDisplay(minuteFirst.value),
                        BinaryFourteenSegmentDecoder.mapToDisplay(minuteSecond.value),
                        BinaryFourteenSegmentDecoder.mapToDisplay(secondFirst.value),
                        BinaryFourteenSegmentDecoder.mapToDisplay(secondSecond.value),
                        3
                    )
                    Row(Modifier.weight(1f)) {
                        FourteenSegmentDisplay(modifier = Modifier.weight(1f), decoder = BinaryFourteenSegmentDecoder(
                            BinaryFourteenSegmentDecoder.mapToDisplay('H'))
                        )
                        FourteenSegmentDisplay(modifier = Modifier.weight(1f), decoder = BinaryFourteenSegmentDecoder(
                            BinaryFourteenSegmentDecoder.mapToDisplay('E'))
                        )
                        FourteenSegmentDisplay(modifier = Modifier.weight(1f), decoder = BinaryFourteenSegmentDecoder(
                            BinaryFourteenSegmentDecoder.mapToDisplay('L'))
                        )
                        FourteenSegmentDisplay(modifier = Modifier.weight(1f), decoder = BinaryFourteenSegmentDecoder(
                            BinaryFourteenSegmentDecoder.mapToDisplay('L'))
                        )
                        FourteenSegmentDisplay(modifier = Modifier.weight(1f), decoder = BinaryFourteenSegmentDecoder(
                            BinaryFourteenSegmentDecoder.mapToDisplay('O'))
                        )
                    }
                    Row(Modifier.weight(1f)) {
                        FourteenSegmentDisplay(modifier = Modifier.weight(1f), decoder = BinaryFourteenSegmentDecoder(
                            BinaryFourteenSegmentDecoder.mapToDisplay('W'))
                        )
                        FourteenSegmentDisplay(modifier = Modifier.weight(1f), decoder = BinaryFourteenSegmentDecoder(
                            BinaryFourteenSegmentDecoder.mapToDisplay('O'))
                        )
                        FourteenSegmentDisplay(modifier = Modifier.weight(1f), decoder = BinaryFourteenSegmentDecoder(
                            BinaryFourteenSegmentDecoder.mapToDisplay('R'))
                        )
                        FourteenSegmentDisplay(modifier = Modifier.weight(1f), decoder = BinaryFourteenSegmentDecoder(
                            BinaryFourteenSegmentDecoder.mapToDisplay('L'))
                        )
                        FourteenSegmentDisplay(modifier = Modifier.weight(1f), decoder = BinaryFourteenSegmentDecoder(
                            BinaryFourteenSegmentDecoder.mapToDisplay('D'))
                        )
                    }
                }
            }

            tickerFlow(Duration.seconds(1))
                .map { Calendar.getInstance() }
                .distinctUntilChanged { old, new ->
                    old.get(Calendar.SECOND) == new.get(Calendar.SECOND)
                }
                .onEach { calendar ->
                    val hour = calendar.get(Calendar.HOUR_OF_DAY)
                    val hourDigits = hour.splitDigits()
                    hourFirst.value = hourDigits.first
                    hourSecond.value = hourDigits.second

                    val minute = calendar.get(Calendar.MINUTE)
                    val minuteDigits = minute.splitDigits()
                    minuteFirst.value = minuteDigits.first
                    minuteSecond.value = minuteDigits.second

                    val second = calendar.get(Calendar.SECOND)
                    val secondDigits = second.splitDigits()
                    secondFirst.value = secondDigits.first
                    secondSecond.value = secondDigits.second
                }
                .launchIn(lifecycleScope)
        }
    }

    private fun tickerFlow(period: Duration) = flow {
        while (currentCoroutineContext().isActive) {
            emit(Unit)
            delay(period)
        }
    }

    private fun Int.splitDigits(): Pair<Int, Int> {
        return when {
            this <= 0 -> 0 to 0
            else -> {
                val secondDigit: Int = this % 10
                val firstDigit: Int = if (this < 10) 0 else this / 10 % 10
                firstDigit to secondDigit
            }
        }
    }
}