package com.rabross.segmenteddisplay.fourteen

interface Decoder {
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

class BinaryDecoder(data: Int = 0) : Decoder {
    override val a  = data and 0b0000000000000001
    override val b  = data and 0b0000000000000010
    override val c  = data and 0b0000000000000100
    override val d  = data and 0b0000000000001000
    override val e  = data and 0b0000000000010000
    override val f  = data and 0b0000000000100000
    override val g1 = data and 0b0000000001000000
    override val g2 = data and 0b0000000010000000
    override val h  = data and 0b0000000100000000
    override val i  = data and 0b0000001000000000
    override val j  = data and 0b0000010000000000
    override val k  = data and 0b0000100000000000
    override val l  = data and 0b0001000000000000
    override val m  = data and 0b0010000000000000

    companion object {
        fun mapToDisplay(number: Int): Int {
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
        fun mapToDisplay(char: Char): Int {
            return when (char) {
                'A' -> 0xF7
                'B' -> 0x128F
                'C' -> 0x39
                'D' -> 0x120F
                'E' -> 0xF9
                'F' -> 0xF1
                'G' -> 0xBD
                'H' -> 0xF6
                'I' -> 0x1209
                'J' -> 0x1E
                'K' -> 0x2470
                'L' -> 0x38
                'M' -> 0x536
                'N' -> 0x2136
                'O' -> 0x3F
                'P' -> 0xF3
                'Q' -> 0x203F
                'R' -> 0x20F3
                'S' -> 0x18D
                'T' -> 0x1201
                'U' -> 0x3E
                'V' -> 0xC30
                'W' -> 0x2836
                'X' -> 0x2D00
                'Y' -> 0x1500
                'Z' -> 0xC09
                '0' -> mapToDisplay(0)
                '1' -> mapToDisplay(1)
                '2' -> mapToDisplay(2)
                '3' -> mapToDisplay(3)
                '4' -> mapToDisplay(4)
                '5' -> mapToDisplay(5)
                '6' -> mapToDisplay(6)
                '7' -> mapToDisplay(7)
                '8' -> mapToDisplay(8)
                '9' -> mapToDisplay(9)
                else -> 0x0
            }
        }
    }
}