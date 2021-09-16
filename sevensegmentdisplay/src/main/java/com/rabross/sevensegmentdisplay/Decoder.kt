package com.rabross.sevensegmentdisplay

interface Decoder {
    val a: Int
    val b: Int
    val c: Int
    val d: Int
    val e: Int
    val f: Int
    val g: Int
}

class BinaryDecoder(data: Int = 0) : Decoder {
    override val a = data and 0b000000001
    override val b = data and 0b000000010
    override val c = data and 0b000000100
    override val d = data and 0b000001000
    override val e = data and 0b000010000
    override val f = data and 0b000100000
    override val g = data and 0b001000000

    companion object {
        fun mapToDigit(number: Int): Int {
            return when (number) {
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
    }
}