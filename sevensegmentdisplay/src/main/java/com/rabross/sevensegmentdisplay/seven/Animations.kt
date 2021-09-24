package com.rabross.sevensegmentdisplay.seven

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class Animations {
    companion object {

        @ExperimentalTime
        private fun animationFlowGenerator(frames: List<Int>): Flow<Int> {
            return flow {
                while (currentCoroutineContext().isActive) {
                    frames.forEach {
                        emit(it)
                        delay(Duration.milliseconds(100))
                    }
                }
            }
        }

        @ExperimentalTime
        val roundOutsideSingleSeg = animationFlowGenerator(
            listOf(
                0b000000001,
                0b000000010,
                0b000000100,
                0b000001000,
                0b000010000,
                0b000100000
            )
        )

        @ExperimentalTime
        val roundOutsideDoubleSeg = animationFlowGenerator(
            listOf(
                0b000000001,
                0b000000011,
                0b000000010,
                0b000000110,
                0b000000100,
                0b000001100,
                0b000001000,
                0b000011000,
                0b000010000,
                0b000110000,
                0b000100000,
                0b000100001
            )
        )

        @ExperimentalTime
        val fillOutside = animationFlowGenerator(
            listOf(
                0b000000000,
                0b000000001,
                0b000000011,
                0b000000111,
                0b000001111,
                0b000011111,
                0b000111111
            )
        )

        @ExperimentalTime
        val pathEight = animationFlowGenerator(
            listOf(
                0b000000011,
                0b001000010,
                0b001010000,
                0b000011000,
                0b000001100,
                0b001000100,
                0b001100000,
                0b000100001
            )
        )

        @ExperimentalTime
        val opposing = animationFlowGenerator(
            listOf(
                0b000001001,
                0b000010010,
                0b000100100,
            )
        )

        @ExperimentalTime
        val fallSingle = animationFlowGenerator(
            listOf(
                0b000000001,
                0b000100010,
                0b001000000,
                0b000010100,
                0b000001000,
            )
        )

        @ExperimentalTime
        val fallFill = animationFlowGenerator(
            listOf(
                0b000000000,
                0b000000001,
                0b000100011,
                0b001100011,
                0b001110111,
                0b001111111,
                0b001111110,
                0b001011100,
                0b000011100,
                0b000001000,
            )
        )

        @ExperimentalTime
        val rightToLeftFill = animationFlowGenerator(
            listOf(
                0b000000110,
                0b001001111,
                0b001111111,
                0b001111001,
                0b000110000,
                0b000000000,
            )
        )
    }
}