package com.danielbunte.battleships.ai

import com.danielbunte.battleships.gameworld.GameBoard
import com.danielbunte.battleships.gameworld.Ship
import com.danielbunte.battleships.match.Match
import com.danielbunte.battleships.match.Player
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

internal class ExplorationStateTests {

    @Test
    fun `fires at random coordinates, until it hits a ship`() {
        // given: a match
        val match = spyk(Match())
        val playerA = Player("Player A")
        val playerB = Player("Player B")
        match.addPlayer(playerA)
        match.addPlayer(playerB)
        match.initGame(GameBoard(2, 1), listOf(Ship("Testboat", 2)))
        match.placeShip(playerA, playerA.shipyard[0], "A1", true)
        match.placeShip(playerB, playerB.shipyard[0], "A1", true)
        val aiClient = AIClient(playerA)
        val classUnderTest = ExplorationState(aiClient, playerA, playerB, match)

        // when: run is invoked
        val nextState = classUnderTest.run()

        // then: attemptAttack should have been invoked
        verify {
            match.attemptAttack(playerA, playerB, any())
        }

        // and: since it was a hit, next state is not classUnderTest
        assertNotEquals(classUnderTest, nextState)
    }
}