package com.danielbunte.battleships.gameworld

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
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
        "b \t   2, 1, 1",
        "j 10    , 9, 9",
        "a.3     , 0, 2",
        "a#3     , 0, 2",
    )
    fun `human readable coordinates are translated into x,y coordinates`(input: String, expectedX: Int, expectedY: Int) {
        // given: a game board instance
        val classUnderTest = GameBoard(10, 10)

        // when: input is requested
        val result = classUnderTest.convertCoordinates(input)

        // then: expectedX, expectedY is returned
        assertEquals(expectedX, result.first)
        assertEquals(expectedY, result.second)
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
        val classUnderTest = GameBoard(10, 10)

        // when: inputX, inputY is requested
        val result = classUnderTest.convertCoordinates(inputX, inputY)

        // then: expected is returned
        assertEquals(expected, result)
    }

    @Test
    fun `placing a ship horizontally correctly updates affected cells`() {
        // given: an empty GameBoard of 10x10
        val classUnderTest = GameBoard(10, 10)

        // when: ship is placed on C3 horizontally
        classUnderTest.placeShip("C3", Ship("Carrier", 5), true)

        // then: C3-C7 should be occupied
        assertEquals("Carrier", classUnderTest.getShipAt("C3")!!.name)
        assertEquals("Carrier", classUnderTest.getShipAt("D3")!!.name)
        assertEquals("Carrier", classUnderTest.getShipAt("E3")!!.name)
        assertEquals("Carrier", classUnderTest.getShipAt("F3")!!.name)
        assertEquals("Carrier", classUnderTest.getShipAt("G3")!!.name)
    }

    @Test
    fun `placing a ship vertically correctly updates affected cells`() {
        // given: an empty GameBoard of 10x10
        val classUnderTest = GameBoard(10, 10)

        // when: ship is placed on C3 vertically
        classUnderTest.placeShip("C3", Ship("Carrier", 5), false)

        // then: C3-C7 should be occupied
        assertEquals("Carrier", classUnderTest.getShipAt("C3")!!.name)
        assertEquals("Carrier", classUnderTest.getShipAt("C4")!!.name)
        assertEquals("Carrier", classUnderTest.getShipAt("C5")!!.name)
        assertEquals("Carrier", classUnderTest.getShipAt("C6")!!.name)
        assertEquals("Carrier", classUnderTest.getShipAt("C7")!!.name)
    }

    @Test
    fun `placing ships results in success or failure, if occupied`() {
        // given: an empty GameBoard of 10x10
        val classUnderTest = GameBoard(10, 10)

        // when: ship is placed on A1 horizontally
        val result = classUnderTest.placeShip("A1", Ship("Carrier", 5), true)

        // then: success
        assertEquals(true, result)
    }

    @Test
    fun `placing ships on top of each other results in failure`() {
        // given: a GameBoard of 10x10 with a placed ship
        val classUnderTest = GameBoard(10, 10)
        classUnderTest.placeShip("B2", Ship("Carrier", 5), true)

        // when: another ship is placed so that it crosses the first ship
        val result = classUnderTest.placeShip("C1", Ship("Cruiser", 3), false)

        // then: failure
        assertEquals(false, result)
    }

    @Test
    fun `placement outside grid results in failure`(){
        // given: a GameBoard
        val classUnderTest = GameBoard(10,10)

        // when: a ship is placed completely outside the grid
        val result = classUnderTest.placeShip("K11", Ship("",1), true)

        // then: failure
        assertEquals(false, result)
    }

    @Test
    fun `placement partially outside grid results in failure`(){
        // given: a GameBoard
        val classUnderTest = GameBoard(10,10)

        // when: a ship is placed partially vertically  outside the grid
        val resultV = classUnderTest.placeShip("A9", Ship("",5), false)

        // then: failure
        assertEquals(false, resultV)

        // and when: a ship is placed partially horizontally outside the grid
        val resultH = classUnderTest.placeShip("H1", Ship("",5), true)

        // then: failure
        assertEquals(false, resultH)

        // and when: a ship is placed partially horizontally outside the grid row 0
        val result0 = classUnderTest.placeShip("H0", Ship("",5), true)

        // then: failure
        assertEquals(false, result0)
    }
}