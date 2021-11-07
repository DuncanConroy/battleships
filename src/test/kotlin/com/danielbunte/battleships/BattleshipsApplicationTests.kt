package com.danielbunte.battleships

import com.danielbunte.battleships.gameworld.GameBoard
import com.danielbunte.battleships.gameworld.Ship
import com.danielbunte.battleships.io.PrintResultSubscriber
import com.danielbunte.battleships.match.GameResult
import com.danielbunte.battleships.match.Match
import com.danielbunte.battleships.match.Player
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BattleshipsApplicationTests {

    @Test
    fun contextLoads() {
    }

    @Test
    fun `fully integrated test with two players, where player A wins`() {
        // given: a full match
        val fleet = listOf(
            Ship("TestBoat", 2),
            Ship("TestBoat", 2),
        )
        val gameBoard = GameBoard(10, 10)
        val playerA = Player("Jane Doe")
        val playerB = Player("Captain Picard")
        val match = Match()
        match.addPlayer(playerA)
        match.addPlayer(playerB)
        match.initGame(gameBoard, fleet)
        match.placeShip(playerA, playerA.shipyard[0], "c.4", false)
        assertEquals(2 to 3, playerA.shipyard[0].cells[0])
        assertEquals(0, playerB.shipyard[0].cells.size, "playerB's ship shouldn't have been modified")
        match.placeShip(playerA, playerA.shipyard[1], "e,9", true)
        match.placeShip(playerB, playerB.shipyard[0], "a1", true)
        match.placeShip(playerB, playerB.shipyard[1], "a2", true)
        val subscriber = PrintResultSubscriber()
        subscriber.subscribeToMatch(match)

        // when: players attempt attacks
        match.attemptAttack(playerA, playerB, "a1")
        match.attemptAttack(playerB, playerA, "j10")
        match.attemptAttack(playerA, playerB, "b1")
        match.attemptAttack(playerB, playerA, "j9")
        match.attemptAttack(playerA, playerB, "a2")
        match.attemptAttack(playerB, playerA, "j8")
        // and: final hit is done
        val result = match.attemptAttack(playerA, playerB, "b2")

        // then: playerA should have won
        assertEquals(GameResult.WON, result.first)
        assertEquals("Jane Doe", result.second?.name)
    }

}
