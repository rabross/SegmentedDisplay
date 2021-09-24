package com.rabross.segmenteddisplay

interface DelimiterDecoder {
    val a: Int
    val b: Int
}

class BinaryDelimiterDecoder(data: Int = 0) : DelimiterDecoder {
    override val a = data and 0b000000001
    override val b = data and 0b000000010
}