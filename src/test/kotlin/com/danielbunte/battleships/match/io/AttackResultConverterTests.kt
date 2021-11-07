package com.danielbunte.battleships.match.io

import com.danielbunte.battleships.gameworld.GameBoard
import com.danielbunte.battleships.match.GameResult
import com.danielbunte.battleships.match.HitResult
import com.danielbunte.battleships.match.Player
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class AttackResultConverterTests {

    @Test
    fun `converts an attackResult into an appropriate dto`() {
        // given: a class instance
        val classUnderTest = AttackResultConverter()
        val playerB = Player("Player B")
        playerB.init(GameBoard(2, 2), emptyList())
        playerB.gameBoard.updateCellState("B1", HitResult.DESTROYED)
        playerB.gameBoard.updateCellState("A2", HitResult.HIT)
        playerB.gameBoard.updateCellState("B2", HitResult.MISS)
        // when: convert is invoked
        val result = classUnderTest.convert(
            attackingPlayer = Player("Player A"),
            targetPlayer = playerB,
            coordinates = "C4",
            gameResult = GameResult.ONGOING,
            nextPlayer = null,
            hitResult = HitResult.MISS,
            winner = null,
        )

        // then: an appropriate dto is created
        assertEquals("Player A", result.attackingPlayerName)
        assertEquals("Player B", result.targetPlayerName)
        assertEquals("C4", result.coordinates)
        assertEquals("MISS", result.hitResult)
        assertEquals("ONGOING", result.gameResult)
        assertEquals(listOf(listOf("WATER", "HIT"), listOf("DESTROYED", "MISS")), result.targetPlayerBoardView)
    }
}