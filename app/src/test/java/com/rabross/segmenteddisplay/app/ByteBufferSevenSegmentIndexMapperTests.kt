package com.rabross.segmenteddisplay.app

import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.security.InvalidParameterException

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
internal class ByteBufferSevenSegmentIndexMapperTests {

    private val minBufferWidth = 2
    private val minBufferHeight = 5
    private val minSevenSegmentCountX = 1
    private val minSevenSegmentCountY = 1
    private val minSevenSegmentIndex = 0

    @Test
    internal fun `Minimum buffer - Segment A`() {
        val segmentAIndex = 0
        val bufferIndex = mapSingleSegmentToBufferIndex(
                minBufferWidth,
                minBufferHeight,
                minSevenSegmentCountX,
                minSevenSegmentCountY,
                minSevenSegmentIndex,
                segmentAIndex
            )
        assert(bufferIndex == 0)
    }

    @Test
    internal fun `Minimum buffer - Segment B`() {
        val segmentBIndex = 1
        val bufferIndex = mapSingleSegmentToBufferIndex(
                minBufferWidth,
                minBufferHeight,
                minSevenSegmentCountX,
                minSevenSegmentCountY,
                minSevenSegmentIndex,
                segmentBIndex
            )
        assert(bufferIndex == 3)
    }

    @Test
    internal fun `Minimum buffer - Segment C`() {
        val segmentCIndex = 2
        val bufferIndex = mapSingleSegmentToBufferIndex(
                minBufferWidth,
                minBufferHeight,
                minSevenSegmentCountX,
                minSevenSegmentCountY,
                minSevenSegmentIndex,
                segmentCIndex
            )
        assert(bufferIndex == 7)
    }

    @Test
    internal fun `Minimum buffer - Segment D`() {
        val segmentDIndex = 3
        val bufferIndex = mapSingleSegmentToBufferIndex(
                minBufferWidth,
                minBufferHeight,
                minSevenSegmentCountX,
                minSevenSegmentCountY,
                minSevenSegmentIndex,
                segmentDIndex
            )
        assert(bufferIndex == 8)
    }

    @Test
    internal fun `Minimum buffer - Segment E`() {
        val segmentEIndex = 4
        val bufferIndex = mapSingleSegmentToBufferIndex(
                minBufferWidth,
                minBufferHeight,
                minSevenSegmentCountX,
                minSevenSegmentCountY,
                minSevenSegmentIndex,
                segmentEIndex
            )
        assert(bufferIndex == 6)
    }

    @Test
    internal fun `Minimum buffer - Segment F`() {
        val segmentFIndex = 5
        val bufferIndex = mapSingleSegmentToBufferIndex(
                minBufferWidth,
                minBufferHeight,
                minSevenSegmentCountX,
                minSevenSegmentCountY,
                minSevenSegmentIndex,
                segmentFIndex
            )
        assert(bufferIndex == 2)
    }

    @Test
    internal fun `Minimum buffer - Segment G`() {
        val segmentGIndex = 6
        val bufferIndex = mapSingleSegmentToBufferIndex(
                minBufferWidth,
                minBufferHeight,
                minSevenSegmentCountX,
                minSevenSegmentCountY,
                minSevenSegmentIndex,
                segmentGIndex
            )
        assert(bufferIndex == 4)
    }

    @Test(expected = InvalidParameterException::class)
    internal fun `Invalid buffer format throws error`() {
        mapSingleSegmentToBufferIndex(3,5,1,1,0,0)
    }

    @Test(expected = InvalidParameterException::class)
    internal fun `Invalid buffer width throws error`() {
        mapSingleSegmentToBufferIndex(2,5,2,1,0,0)
    }

    @Test(expected = InvalidParameterException::class)
    internal fun `Invalid buffer height throws error`() {
        mapSingleSegmentToBufferIndex(2,5,1,2,0,0)
    }
}