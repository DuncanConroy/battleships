package com.danielbunte.battleships.match

import com.danielbunte.battleships.gameworld.GameBoard
import com.danielbunte.battleships.gameworld.Ship
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MatchTests {

    @Test
    fun `adding too many players throws an exception`() {
        // given: two players
        val classUnderTest = Match(mockk(relaxed = true))
        classUnderTest.addPlayer(Player(""))
        classUnderTest.addPlayer(Player(""))

        // when: an additional player is added
        // then: an exception is thrown
        assertThrows<IllegalArgumentException> { classUnderTest.addPlayer(Player("")) }
    }

    @Test
    fun `game flow`() {
        // then: turnCoordinator is invoked
        // and then: hitCalculator is invoked
        assertEquals(true, false)
    }

    @Test
    fun `playerA wins, if all playerB's ships are destroyed`() {
        // given: two players, with one ship each
        val playerA = mockk<Player>(relaxed = true)
        val playerB = Player("Player B")
        val gameBoardB = mockk<GameBoard>(relaxed = true)
        playerB.init(gameBoardB)

        val hitCalculator = mockk<HitCalculator>(relaxed = true)
        val classUnderTest = Match(hitCalculator)
        classUnderTest.addPlayer(playerA)
        classUnderTest.addPlayer(playerB)

        val ship = Ship("", 0) // init with 0 length, so that health is 0 as well.
        every { hitCalculator.attemptAttack(any(), any()) } returns HitResult.DESTROYED
        every { gameBoardB.allShips } returns listOf(ship)
        every { gameBoardB.getShipAt(any()) } returns ship

        // when: ship is hit
        val result = classUnderTest.attemptAttack(playerA, playerB, "A1")

        // then: playerA has won
        assertEquals(GameResult.WON, result.first)
        assertEquals(playerA, result.second)
    }

    @Test
    fun `game is ongoing after a non-win attack`() {
        // given: a simple match
        val hitCalculator = mockk<HitCalculator>(relaxed = true)
        every { hitCalculator.attemptAttack(any(), any()) } returns HitResult.MISS
        val classUnderTest = Match(hitCalculator)

        // when: a non-winning attack is attempted
        val result = classUnderTest.attemptAttack(mockk(relaxed = true), mockk(relaxed = true), "A1")

        // then: game is ongoing
        assertEquals(GameResult.ONGOING, result.first)
        // and: there's no winner
        assertEquals(null, result.second)
    }
}