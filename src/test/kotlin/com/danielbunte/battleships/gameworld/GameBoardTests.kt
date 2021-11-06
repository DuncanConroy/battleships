package com.danielbunte.battleships.gameworld

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GameBoardTests {

    @ParameterizedTest(name = "{0} translates to {1},{2}")
    @CsvSource(
        // human readable, x, y
        "a1      , 0, 0",
        "A2      , 0, 1",
        "b1      , 1, 0",
        "B2      , 1, 1",
        "A 1     , 0, 0",
        "a   2   , 0, 1",
        "B  1    , 1, 0",
        "b      2, 1, 1",
        "j   10  , 9, 9",
    )
    fun `human readable coordinates are translated into x,y coordinates`(input: String, expectedX: Int, expectedY: Int) {
        // given: a game board instance
        //      1    2
        // A    #    #
        // B    #    #
        val classUnderTest = GameBoard(2, 2)

        // when: input is requested
        val result = classUnderTest.getCoordinates(input)

        // then: expectedX, expectedY is returned
        assertEquals(result.first, expectedX)
        assertEquals(result.second, expectedY)
    }

    @ParameterizedTest(name = "{0}, {1} translates to {2}")
    @CsvSource(
        // human readable, x, y
        "0, 0, A1",
        "0, 1, A2",
        "1, 0, B1",
        "1, 1, B2",
        "9, 6, J7",
    )
    fun `x,y coordinates are translated into human readable coordinates`(inputX: Int, inputY: Int, expected: String) {
        // given: a game board instance
        //      1    2
        // A    #    #
        // B    #    #
        val classUnderTest = GameBoard(2, 2)

        // when: inputX, inputY is requested
        val result = classUnderTest.getCoordinates(inputX, inputY)

        // then: expected is returned
        assertEquals(result, expected)
    }
}