package com.rabross.segmenteddisplay

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
import com.rabross.segmenteddisplay.seven.Animations
import com.rabross.segmenteddisplay.seven.BinarySevenSegmentDecoder
import com.rabross.segmenteddisplay.seven.DigitalClockSevenSegmentDisplay
import com.rabross.segmenteddisplay.seven.SevenSegmentDisplay
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import java.util.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
class SevenSegmentActivity : ComponentActivity() {

    private val redLed = SingleColorLed(Color.Red, Color.Black)
    private val greenLed = SingleColorLed(Color.Green, Color.Black)
    private val blueLed = SingleColorLed(Color.Blue, Color.Black)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val animationRightToLeftFill = remember { mutableStateOf(0) }
            val animationRoundOutsideDoubleSeg = remember { mutableStateOf(0) }
            val animationFallFill = remember { mutableStateOf(0) }

            val hourFirst = remember { mutableStateOf(0) }
            val hourSecond = remember { mutableStateOf(0) }
            val minuteFirst = remember { mutableStateOf(0) }
            val minuteSecond = remember { mutableStateOf(0) }
            val secondFirst = remember { mutableStateOf(0) }
            val secondSecond = remember { mutableStateOf(0) }

            Surface(color = Color.Black) {
                Column(modifier = Modifier.padding(24.dp)) {
                    DigitalClockSevenSegmentDisplay(
                        Modifier.weight(1f),
                        BinarySevenSegmentDecoder.mapToDisplay(hourFirst.value),
                        BinarySevenSegmentDecoder.mapToDisplay(hourSecond.value),
                        BinarySevenSegmentDecoder.mapToDisplay(minuteFirst.value),
                        BinarySevenSegmentDecoder.mapToDisplay(minuteSecond.value),
                        BinarySevenSegmentDecoder.mapToDisplay(secondFirst.value),
                        BinarySevenSegmentDecoder.mapToDisplay(secondSecond.value),
                        3
                    )
                    Row(
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        SevenSegmentDisplay(
                            modifier = Modifier.weight(1f),
                            decoder = BinarySevenSegmentDecoder(animationFallFill.value),
                            led = redLed
                        )
                        SevenSegmentDisplay(
                            modifier = Modifier.weight(1f),
                            decoder = BinarySevenSegmentDecoder(animationRightToLeftFill.value),
                            led = greenLed
                        )
                        SevenSegmentDisplay(
                            modifier = Modifier.weight(1f),
                            decoder = BinarySevenSegmentDecoder(animationRoundOutsideDoubleSeg.value),
                            led = blueLed
                        )
                    }
                }
            }

            Animations.rightToLeftFill
                .onEach { animationRightToLeftFill.value = it }
                .launchIn(lifecycleScope)

            Animations.roundOutsideDoubleSeg
                .onEach { animationRoundOutsideDoubleSeg.value = it }
                .launchIn(lifecycleScope)

            Animations.fallFill
                .onEach { animationFallFill.value = it }
                .launchIn(lifecycleScope)

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