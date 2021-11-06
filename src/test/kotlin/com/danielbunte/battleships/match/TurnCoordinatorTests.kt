package com.danielbunte.battleships.match

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TurnCoordinatorTests {

    @Test
    fun `players cannot make consecutive turns`() {
        // given: two players
        val playerA = Player("PlayerA")
        val playerB = Player("PlayerB")
        val classUnderTest = TurnCoordinator(listOf(playerA,playerB))

        // when: playerA tries to make consecutive turns
        classUnderTest.makeTurn(playerA, "A1")
        val result = classUnderTest.makeTurn(playerA, "A2")

        // then: NOT_YOUR_TURN is returned
        assertEquals(TurnResult.NOT_YOUR_TURN, result.first)

        // and: the correct Player is returned
        assertEquals(playerB, result.second)
    }

    @Test
    fun `players make alternating turns`() {
        // given: two players
        val playerA = Player("PlayerA")
        val playerB = Player("PlayerB")
        val classUnderTest = TurnCoordinator(listOf(playerA,playerB))

        // when: playerA tries to make consecutive turns
        classUnderTest.makeTurn(playerA, "A1")
        val result = classUnderTest.makeTurn(playerB, "A1")

        // then: next player is playerA
        assertEquals(playerA, result.second)
    }
}