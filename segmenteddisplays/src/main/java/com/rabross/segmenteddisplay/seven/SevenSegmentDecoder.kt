package com.rabross.segmenteddisplay.seven

interface SevenSegmentDecoder {
    val a: Int
    val b: Int
    val c: Int
    val d: Int
    val e: Int
    val f: Int
    val g: Int
}

class BinarySevenSegmentDecoder(data: Int = 0) : SevenSegmentDecoder {
    override val a = data and 0b00000001
    override val b = data and 0b00000010
    override val c = data and 0b00000100
    override val d = data and 0b00001000
    override val e = data and 0b00010000
    override val f = data and 0b00100000
    override val g = data and 0b01000000

    companion object {
        fun mapToDisplay(number: Int): Int {
            return when (number) {
                0 -> 0b00111111
                1 -> 0b00000110
                2 -> 0b01011011
                3 -> 0b01001111
                4 -> 0b01100110
                5 -> 0b01101101
                6 -> 0b01111101
                7 -> 0b00000111
                8 -> 0b01111111
                9 -> 0b01100111
                else -> 0b00000000
            }
        }
    }
}
