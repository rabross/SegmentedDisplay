package com.rabross.sevensegmentdisplay

import androidx.compose.ui.graphics.Color

fun interface Led {
    fun signal(value: Int): Color
}

class SingleColorLed(private val on: Color, private val off: Color) : Led {
    override fun signal(value: Int) = if (value > 0) on else off
}