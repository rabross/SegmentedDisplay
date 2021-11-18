package com.rabross.segmenteddisplay.delimiter

interface Decoder {
    val a: Int
    val b: Int
}

class BinaryDecoder(data: Int = 0) : Decoder {
    override val a = data and 0b01
    override val b = data and 0b10
}