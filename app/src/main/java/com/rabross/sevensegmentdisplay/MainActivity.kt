package com.rabross.sevensegmentdisplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val digit = remember { mutableStateOf(0) }
            val animationFallFill = remember { mutableStateOf(0) }
            val animationRightToLeftFill = remember { mutableStateOf(0) }
            val animationRoundOutsideDoubleSeg = remember { mutableStateOf(0) }

            Surface {
                Column {
                    Row(modifier = Modifier.weight(1f)) {
                        SevenSegmentDisplay(modifier = Modifier.weight(1f),decoder = BinaryDecoder (digit.value))
                        SevenSegmentDisplay(modifier = Modifier.weight(1f),decoder = BinaryDecoder(animationFallFill.value))
                    }
                    Row(modifier = Modifier.weight(1f)) {
                        SevenSegmentDisplay(modifier = Modifier.weight(1f),decoder = BinaryDecoder (animationRightToLeftFill.value))
                        SevenSegmentDisplay(modifier = Modifier.weight(1f),decoder = BinaryDecoder(animationRoundOutsideDoubleSeg.value))
                    }
                }

            }

            singleDigitCountFlow
                .onEach { number -> digit.value = number.mapToDigit() }
                .launchIn(lifecycleScope)

            Animations.fallFill
                .onEach { animationFallFill. value = it }
                .launchIn(lifecycleScope)

            Animations.rightToLeftFill
                .onEach { animationRightToLeftFill. value = it }
                .launchIn(lifecycleScope)

            Animations.roundOutsideDoubleSeg
                .onEach { animationRoundOutsideDoubleSeg. value = it }
                .launchIn(lifecycleScope)
        }
    }

    private val singleDigitCountFlow = flow {
        while (currentCoroutineContext().isActive) {
            (0..10).forEach { number ->
                emit(number)
                delay(Duration.seconds(1))
            }
        }
    }
}