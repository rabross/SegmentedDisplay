package com.rabross.sevensegmentdisplay

fun Int.mapToDigit(): Int {
    return when (this) {
        0 -> 0b000111111
        1 -> 0b000000110
        2 -> 0b001011011
        3 -> 0b001001111
        4 -> 0b001100110
        5 -> 0b001101101
        6 -> 0b001111101
        7 -> 0b000000111
        8 -> 0b001111111
        9 -> 0b001100111
        else -> 0b000000000
    }
}