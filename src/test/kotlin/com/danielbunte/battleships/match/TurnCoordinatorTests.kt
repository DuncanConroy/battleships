package com.danielbunte.battleships.match

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class TurnCoordinatorTests {

    @Test
    fun `players cannot make consecutive turns`() {
        // given: two players
        val playerA = Player("PlayerA")
        val playerB = Player("PlayerB")
        val classUnderTest = TurnCoordinator()
        classUnderTest.init(listOf(playerA, playerB))

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
        val classUnderTest = TurnCoordinator()
        classUnderTest.init(listOf(playerA, playerB))

        // when: playerA tries to make consecutive turns
        classUnderTest.canMakeTurn(playerA)
        val result = classUnderTest.canMakeTurn(playerB)

        // then: next player is playerA
        assertEquals(playerA, result.second)

        // and: PROCEED is returned
        assertEquals(TurnResult.PROCEED, result.first)
    }

    @Test
    fun `init sets the players correctly`() {
        // given: two players
        val playerA = Player("PlayerA")
        val playerB = Player("PlayerB")
        val classUnderTest = TurnCoordinator()
        assertThrows<UninitializedPropertyAccessException> { classUnderTest.canMakeTurn(playerA) }

        // when: class is properly initialized
        classUnderTest.init(listOf(playerA, playerB))

        // then: no errors are thrown
        assertDoesNotThrow { classUnderTest.canMakeTurn(playerA) }
    }
}