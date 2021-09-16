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
}