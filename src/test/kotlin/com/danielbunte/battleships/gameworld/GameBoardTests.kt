package com.danielbunte.battleships.gameworld

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GameBoardTests {

    @ParameterizedTest(name = "{0} translates to {1},{2}")
    @CsvSource(
        // human readable, x, y
        "a1, 0, 0",
        "A2, 0, 1",
        "b1, 1, 0",
        "B2, 1, 1",
        "A 1, 0, 0",
        "a   2, 0, 1",
        "B  1, 1, 0",
        "b      2, 1, 1",
    )
    fun `human readable coordinates are translated into x,y coordinates`(input: String, expectedX: Int, expectedY: Int) {
        // given: a game board instance
        //      1    2
        // A    #    #
        // B    #    #
        val classUnderTest = GameBoard(2, 2)

        // when: A1 is requested
        val result = classUnderTest.getCoordinates(input)

        // then: expectedX, expectedY is returned
        assertEquals(result.first, expectedX)
        assertEquals(result.second, expectedY)
    }
}