package com.danielbunte.battleships.match

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TurnCoordinatorTests {
    @Test
    fun `players cannot make consecutive turns`(){
        // given: two players
        val classUnderTest = TurnCoordinator()
        val playerA = Player("PlayerA")
        val playerB = Player("PlayerB")
        classUnderTest.addPlayer(playerA)
        classUnderTest.addPlayer(playerB)

        // when: playerA tries to make consecutive turns
        classUnderTest.makeTurn(playerA, "A1")
        val result = classUnderTest.makeTurn(playerA, "A2")

        // then: NOT_YOUR_TURN is returned
        assertEquals(TurnResult.NOT_YOUR_TURN, result.first)

        // and: the correct Player is returned
        assertEquals(playerB, result.second)
    }

    @Test
    fun `players make alternating turns`(){
        // given: two players
        val classUnderTest = TurnCoordinator()
        val playerA = Player("PlayerA")
        val playerB = Player("PlayerB")
        classUnderTest.addPlayer(playerA)
        classUnderTest.addPlayer(playerB)

        // when: playerA tries to make consecutive turns
        classUnderTest.makeTurn(playerA, "A1")
        val result = classUnderTest.makeTurn(playerB, "A1")

        // then: next player is playerA
        assertEquals(playerA, result.second)
    }

    @Test
    fun `adding too many players throws an exception`(){
        // given: two players
        val classUnderTest = TurnCoordinator()
        classUnderTest.addPlayer(Player(""))
        classUnderTest.addPlayer(Player(""))

        // when: an additional player is added
        // then: an exception is thrown
        assertThrows<IllegalArgumentException> { classUnderTest.addPlayer(Player("")) }
    }
}