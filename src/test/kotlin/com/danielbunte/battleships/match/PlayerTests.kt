package com.danielbunte.battleships.match

import com.danielbunte.battleships.gameworld.GameBoard
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class PlayerTests{
    @Test
    fun `player has gameBoard after initialization`(){
        // given: a Player
        val classUnderTest = Player("")
        val gameBoard = GameBoard(1,1)
        assertThrows<UninitializedPropertyAccessException> { classUnderTest.gameBoard }

        // when: player is initialized
        classUnderTest.init(gameBoard)

        // then: player has a gameBoard
        assertEquals(gameBoard, classUnderTest.gameBoard)
    }
}