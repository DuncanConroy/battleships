package com.danielbunte.battleships.match

import com.danielbunte.battleships.gameworld.GameBoard
import com.danielbunte.battleships.gameworld.Ship
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class PlayerTests {

    @Test
    fun `player has gameBoard and shipyard after initialization`() {
        // given: a Player
        val classUnderTest = Player("")
        val gameBoard = GameBoard(1, 1)
        val shipyard = listOf(Ship("Cruiser", 3))
        assertThrows<UninitializedPropertyAccessException> { classUnderTest.gameBoard }
        assertThrows<UninitializedPropertyAccessException> { classUnderTest.shipyard }

        // when: player is initialized
        classUnderTest.init(gameBoard, shipyard)

        // then: player has a gameBoard and shipyard
        assertEquals(gameBoard, classUnderTest.gameBoard)
        assertEquals(shipyard, classUnderTest.shipyard)
    }
}