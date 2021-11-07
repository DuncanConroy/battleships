package com.danielbunte.battleships.io

import com.danielbunte.battleships.gameworld.GameBoard
import com.danielbunte.battleships.match.GameResult
import com.danielbunte.battleships.match.HitResult
import com.danielbunte.battleships.match.Player
import com.danielbunte.battleships.match.io.AttackResultConverter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PrintResultSubscriberTests {

    @Test
    fun `createScreen returns proper user representation of the game board`() {
        // given: a class instance
        val subscriber = PrintResultSubscriber()
        val converter = AttackResultConverter()
        val playerA = Player("Player A")
        val playerB = Player("Player B")
        playerB.init(GameBoard(10, 10), emptyList())
        playerB.gameBoard.updateCellState("A4", HitResult.MISS)
        playerB.gameBoard.updateCellState("C4", HitResult.HIT)
        playerB.gameBoard.updateCellState("D3", HitResult.DESTROYED)
        playerB.gameBoard.updateCellState("E3", HitResult.DESTROYED)
        playerB.gameBoard.updateCellState("F3", HitResult.DESTROYED)
        val dto = converter.convert(playerA, playerB, "C4", GameResult.ONGOING, playerB, HitResult.HIT, null)

        // when: createScreen is invoked
        val result = subscriber.createPrintableBoard(dto)

        // then: a proper ui representation is returned
        assertEquals(
            //  A B C D E F G H I J
            """
            ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
            ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
            ~ ~ ~ # # # ~ ~ ~ ~
            o ~ x ~ ~ ~ ~ ~ ~ ~
            ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
            ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
            ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
            ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
            ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
            ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
            """.trimIndent(), result
        )
    }
}