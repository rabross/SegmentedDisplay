package com.rabross.sevensegmentdisplay

interface FourteenSegmentDecoder {
    val a: Int
    val b: Int
    val c: Int
    val d: Int
    val e: Int
    val f: Int
    val g1: Int
    val g2: Int
    val h: Int
    val i: Int
    val j: Int
    val k: Int
    val l: Int
    val m: Int
}

class BinaryFourteenSegmentDecoder(data: Int = 0) : FourteenSegmentDecoder {
    override val a  = data and 0b00000001
    override val b  = data and 0b00000010
    override val c  = data and 0b00000100
    override val d  = data and 0b00001000
    override val e  = data and 0b00010000
    override val f  = data and 0b00100000
    override val g1 = data and 0b01000000
    override val g2 = data and 0b10000000
    override val h  = data and 0b0000000100000000
    override val i  = data and 0b0000001000000000
    override val j  = data and 0b0000010000000000
    override val k  = data and 0b0000100000000000
    override val l  = data and 0b0001000000000000
    override val m  = data and 0b0010000000000000

    companion object {
        fun mapToDigit(number: Int): Int {
            return when (number) {
                0 -> 0xC3F
                1 -> 0x406
                2 -> 0xDB
                3 -> 0x8F
                4 -> 0xE6
                5 -> 0xED
                6 -> 0xFD
                7 -> 0x1401
                8 -> 0xFF
                9 -> 0xE7
                else -> 0x0
            }
        }
    }
}