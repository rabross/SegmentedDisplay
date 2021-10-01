package com.rabross.segmenteddisplay.app

import org.junit.Assert.assertEquals
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.security.InvalidParameterException

@FixMethodOrder(MethodSorters.JVM)
internal class ByteBufferSevenSegmentIndexMapperTests {

    private val minBufferWidth = 2
    private val minBufferHeight = 5
    private val minSevenSegmentCountX = 1
    private val minSevenSegmentCountY = 1
    private val minSevenSegmentIndex = 0

    @Test
    internal fun `Minimum - Segment A`() {
        val segmentAIndex = 0
        val bufferIndex = mapSingleSegmentToBufferIndex(
                minBufferWidth,
                minBufferHeight,
                minSevenSegmentCountX,
                minSevenSegmentCountY,
                minSevenSegmentIndex,
                segmentAIndex
            )
        assertEquals(0, bufferIndex)
    }

    @Test
    internal fun `Minimum - Segment B`() {
        val segmentBIndex = 1
        val bufferIndex = mapSingleSegmentToBufferIndex(
                minBufferWidth,
                minBufferHeight,
                minSevenSegmentCountX,
                minSevenSegmentCountY,
                minSevenSegmentIndex,
                segmentBIndex
            )
        assertEquals(3, bufferIndex)
    }

    @Test
    internal fun `Minimum - Segment C`() {
        val segmentCIndex = 2
        val bufferIndex = mapSingleSegmentToBufferIndex(
                minBufferWidth,
                minBufferHeight,
                minSevenSegmentCountX,
                minSevenSegmentCountY,
                minSevenSegmentIndex,
                segmentCIndex
            )
        assertEquals(7, bufferIndex)
    }

    @Test
    internal fun `Minimum - Segment D`() {
        val segmentDIndex = 3
        val bufferIndex = mapSingleSegmentToBufferIndex(
                minBufferWidth,
                minBufferHeight,
                minSevenSegmentCountX,
                minSevenSegmentCountY,
                minSevenSegmentIndex,
                segmentDIndex
            )
        assertEquals(8, bufferIndex)
    }

    @Test
    internal fun `Minimum - Segment E`() {
        val segmentEIndex = 4
        val bufferIndex = mapSingleSegmentToBufferIndex(
                minBufferWidth,
                minBufferHeight,
                minSevenSegmentCountX,
                minSevenSegmentCountY,
                minSevenSegmentIndex,
                segmentEIndex
            )
        assertEquals(6, bufferIndex)
    }

    @Test
    internal fun `Minimum - Segment F`() {
        val segmentFIndex = 5
        val bufferIndex = mapSingleSegmentToBufferIndex(
                minBufferWidth,
                minBufferHeight,
                minSevenSegmentCountX,
                minSevenSegmentCountY,
                minSevenSegmentIndex,
                segmentFIndex
            )
        assertEquals(2, bufferIndex)
    }

    @Test
    internal fun `Minimum - Segment G`() {
        val segmentGIndex = 6
        val bufferIndex = mapSingleSegmentToBufferIndex(
                minBufferWidth,
                minBufferHeight,
                minSevenSegmentCountX,
                minSevenSegmentCountY,
                minSevenSegmentIndex,
                segmentGIndex
            )
        assertEquals(4, bufferIndex)
    }

    @Test
    internal fun `Square Grid - Index 0 Segment A`() {
        val sevenSegmentCountX = 2
        val sevenSegmentCountY = 2
        val bufferWidth = 2 * sevenSegmentCountX
        val bufferHeight = 5 * sevenSegmentCountY
        val sevenSegmentIndex = 0
        val segmentIndex = 0
        val bufferIndex = mapSingleSegmentToBufferIndex(
            bufferWidth,
            bufferHeight,
            sevenSegmentCountX,
            sevenSegmentCountY,
            sevenSegmentIndex,
            segmentIndex
        )
        assertEquals(0, bufferIndex)
    }

    @Test
    internal fun `Square Grid - Index 1 Segment A`() {
        val sevenSegmentCountX = 2
        val sevenSegmentCountY = 2
        val bufferWidth = 2 * sevenSegmentCountX
        val bufferHeight = 5 * sevenSegmentCountY
        val sevenSegmentIndex = 1
        val segmentIndex = 0
        val bufferIndex = mapSingleSegmentToBufferIndex(
            bufferWidth,
            bufferHeight,
            sevenSegmentCountX,
            sevenSegmentCountY,
            sevenSegmentIndex,
            segmentIndex
        )
        assertEquals(2, bufferIndex)
    }

    @Test
    internal fun `Square Grid - Index 2 Segment A`() {
        val sevenSegmentCountX = 2
        val sevenSegmentCountY = 2
        val bufferWidth = 2 * sevenSegmentCountX
        val bufferHeight = 5 * sevenSegmentCountY
        val sevenSegmentIndex = 2
        val segmentIndex = 0
        val bufferIndex = mapSingleSegmentToBufferIndex(
            bufferWidth,
            bufferHeight,
            sevenSegmentCountX,
            sevenSegmentCountY,
            sevenSegmentIndex,
            segmentIndex
        )
        assertEquals(20, bufferIndex)
    }

    @Test
    internal fun `Square Grid - Index 3 Segment A`() {
        val sevenSegmentCountX = 2
        val sevenSegmentCountY = 2
        val bufferWidth = 2 * sevenSegmentCountX
        val bufferHeight = 5 * sevenSegmentCountY
        val sevenSegmentIndex = 3
        val segmentIndex = 0
        val bufferIndex = mapSingleSegmentToBufferIndex(
            bufferWidth,
            bufferHeight,
            sevenSegmentCountX,
            sevenSegmentCountY,
            sevenSegmentIndex,
            segmentIndex
        )
        assertEquals(22, bufferIndex)
    }

    @Test
    internal fun `Square Grid - Index 0 Segment B`() {
        val sevenSegmentCountX = 2
        val sevenSegmentCountY = 2
        val bufferWidth = 2 * sevenSegmentCountX
        val bufferHeight = 5 * sevenSegmentCountY
        val sevenSegmentIndex = 0
        val segmentIndex = 1
        val bufferIndex = mapSingleSegmentToBufferIndex(
            bufferWidth,
            bufferHeight,
            sevenSegmentCountX,
            sevenSegmentCountY,
            sevenSegmentIndex,
            segmentIndex
        )
        assertEquals(5, bufferIndex)
    }

    @Test
    internal fun `Square Grid - Index 1 Segment B`() {
        val sevenSegmentCountX = 2
        val sevenSegmentCountY = 2
        val bufferWidth = 2 * sevenSegmentCountX
        val bufferHeight = 5 * sevenSegmentCountY
        val sevenSegmentIndex = 1
        val segmentIndex = 1
        val bufferIndex = mapSingleSegmentToBufferIndex(
            bufferWidth,
            bufferHeight,
            sevenSegmentCountX,
            sevenSegmentCountY,
            sevenSegmentIndex,
            segmentIndex
        )
        assertEquals(7, bufferIndex)
    }

    @Test
    internal fun `Square Grid - Index 2 Segment B`() {
        val sevenSegmentCountX = 2
        val sevenSegmentCountY = 2
        val bufferWidth = 2 * sevenSegmentCountX
        val bufferHeight = 5 * sevenSegmentCountY
        val sevenSegmentIndex = 2
        val segmentIndex = 1
        val bufferIndex = mapSingleSegmentToBufferIndex(
            bufferWidth,
            bufferHeight,
            sevenSegmentCountX,
            sevenSegmentCountY,
            sevenSegmentIndex,
            segmentIndex
        )
        assertEquals(25, bufferIndex)
    }

    @Test
    internal fun `Square Grid - Index 3 Segment B`() {
        val sevenSegmentCountX = 2
        val sevenSegmentCountY = 2
        val bufferWidth = 2 * sevenSegmentCountX
        val bufferHeight = 5 * sevenSegmentCountY
        val sevenSegmentIndex = 3
        val segmentIndex = 1
        val bufferIndex = mapSingleSegmentToBufferIndex(
            bufferWidth,
            bufferHeight,
            sevenSegmentCountX,
            sevenSegmentCountY,
            sevenSegmentIndex,
            segmentIndex
        )
        assertEquals(27, bufferIndex)
    }

    @Test
    internal fun `Rectangle Grid - Index 3 Segment 0`() {
        val sevenSegmentCountX = 5
        val sevenSegmentCountY = 3
        val bufferWidth = 2 * sevenSegmentCountX
        val bufferHeight = 5 * sevenSegmentCountY
        val sevenSegmentIndex = 3
        val segmentIndex = 0
        val bufferIndex = mapSingleSegmentToBufferIndex(
            bufferWidth,
            bufferHeight,
            sevenSegmentCountX,
            sevenSegmentCountY,
            sevenSegmentIndex,
            segmentIndex
        )
        assertEquals(6, bufferIndex)
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