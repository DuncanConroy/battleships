package com.danielbunte.battleships.match

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TurnCoordinatorTests {

    @Test
    fun `players cannot make consecutive turns`() {
        // given: two players
        val playerA = Player("PlayerA")
        val playerB = Player("PlayerB")
        val classUnderTest = TurnCoordinator(listOf(playerA,playerB))

        // when: playerA tries to make consecutive turns
        classUnderTest.canMakeTurn(playerA)
        val result = classUnderTest.canMakeTurn(playerA)

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
        classUnderTest.canMakeTurn(playerA)
        val result = classUnderTest.canMakeTurn(playerB)

        // then: next player is playerA
        assertEquals(playerA, result.second)
    }
}