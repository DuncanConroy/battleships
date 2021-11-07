package com.danielbunte.battleships.ai

import com.danielbunte.battleships.gameworld.GameBoard
import com.danielbunte.battleships.gameworld.Ship
import com.danielbunte.battleships.match.Match
import com.danielbunte.battleships.match.Player
import com.danielbunte.battleships.mockkRelaxed
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class PlaceShipsStateTests {

    @Test
    fun `run() places a ship randomly`() {
        // given: a match
        val match = mockkRelaxed<Match>()
        val player = Player("Player A")
        player.init(GameBoard(10, 10), listOf(Ship("Testboat", 2), Ship("Carrier", 5)))
        every { match.placeShip(any(), any(), any(), any()) } answers {
            player.gameBoard.placeShip(arg(2), arg(1), arg(3))
        }
        val classUnderTest = PlaceShipsState(player, match)

        // when: run is invoked
        val nextState = classUnderTest.run()

        // then: the longest ship is placed at a random place, first
        assertEquals(0, player.shipyard[0].cells.size) // the shorter ship "Testboat" is not touched, yet
        assertEquals(5, player.shipyard[1].cells.size)
        // and: next state is still PlaceShipsState
        assertEquals(classUnderTest, nextState)

        // and when: run is invoked again
        val finalState = classUnderTest.run()

        // then: the next longest ship is placed
        assertEquals(2, player.shipyard[0].cells.size)
        // and: next state is null (nothing is set)
        assertNull(finalState)
    }
}