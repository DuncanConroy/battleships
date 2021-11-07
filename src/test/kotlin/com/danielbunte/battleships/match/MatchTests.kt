package com.danielbunte.battleships.match

import com.danielbunte.battleships.gameworld.GameBoard
import com.danielbunte.battleships.gameworld.Ship
import com.danielbunte.battleships.mockkRelaxed
import io.mockk.every
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MatchTests {

    @Test
    fun `adding too many players throws an exception`() {
        // given: two players
        val classUnderTest = Match(mockkRelaxed(), mockkRelaxed())
        classUnderTest.addPlayer(Player(""))
        classUnderTest.addPlayer(Player(""))

        // when: an additional player is added
        // then: an exception is thrown
        assertThrows<IllegalArgumentException> { classUnderTest.addPlayer(Player("")) }
    }

    @Test
    fun `game flow follows correct sequence`() {
        // given: a match
        val hitCalculator = mockkRelaxed<HitCalculator>()
        val turnCoordinator = mockkRelaxed<TurnCoordinator>()
        val classUnderTest = Match(hitCalculator, turnCoordinator)
        val playerA = mockkRelaxed<Player>()
        val playerB = mockkRelaxed<Player>()
        val coordinates = "C4"
        classUnderTest.addPlayer(playerA)
        classUnderTest.addPlayer(playerB)
        classUnderTest.placeShip(playerA, mockkRelaxed(), coordinates, true)
        val hitResult = HitResult.MISS
        every { hitCalculator.attemptAttack(any(), coordinates) } returns hitResult

        // when: attemptAttack is invoked
        val result = classUnderTest.attemptAttack(playerA, playerB, coordinates)

        // then: turnCoordinator is invoked, followed by hitCalculator
        assertEquals(GameResult.ONGOING, result.first)
        verifyOrder {
            turnCoordinator.canMakeTurn(playerA)
            hitCalculator.attemptAttack(playerB.gameBoard, coordinates)
            playerB.gameBoard.updateCellState(coordinates, hitResult)
        }
    }

    @Test
    fun `returns NOT_YOUR_TURN if wrong player attemptsAttack`() {
        // given: a match
        val turnCoordinator = mockkRelaxed<TurnCoordinator>()
        val classUnderTest = Match(mockkRelaxed(), turnCoordinator)
        val playerA = mockkRelaxed<Player>()
        val playerB = mockkRelaxed<Player>()
        classUnderTest.placeShip(playerA, mockkRelaxed(), "", true)

        every { turnCoordinator.canMakeTurn(any()) } returns (TurnResult.NOT_YOUR_TURN to playerA)

        // when: attemptAttack is invoked
        val result = classUnderTest.attemptAttack(playerA, playerB, "")

        // then: result is NOT_YOUR_TURN
        assertEquals(GameResult.NOT_YOUR_TURN, result.first)
        assertEquals(null, result.second)
    }

    @Test
    fun `playerA wins, if all playerB's ships are destroyed`() {
        // given: two players, with one ship each
        val playerA = mockkRelaxed<Player>()
        val playerB = Player("Player B")
        val gameBoardB = GameBoard(10, 10)
        val ship = Ship("", 0) // init with 0 length, so that health is 0 as well.
        val shipyardB = listOf(ship)
        playerB.init(gameBoardB, shipyardB)

        val hitCalculator = mockkRelaxed<HitCalculator>()
        val classUnderTest = Match(hitCalculator, mockkRelaxed())
        classUnderTest.addPlayer(playerA)
        classUnderTest.addPlayer(playerB)
        classUnderTest.placeShip(playerB, ship, "A1", true)

        every { hitCalculator.attemptAttack(any(), any()) } returns HitResult.DESTROYED

        // when: ship is hit
        val result = classUnderTest.attemptAttack(playerA, playerB, "A1")

        // then: playerA has won
        assertEquals(GameResult.WON, result.first)
        assertEquals(playerA, result.second)
    }

    @Test
    fun `game is ongoing after a non-win attack`() {
        // given: a simple match
        val hitCalculator = mockkRelaxed<HitCalculator>()
        every { hitCalculator.attemptAttack(any(), any()) } returns HitResult.MISS
        val classUnderTest = Match(hitCalculator, mockkRelaxed())
        classUnderTest.placeShip(mockkRelaxed(), mockkRelaxed(), "", true)

        // when: a non-winning attack is attempted
        val result = classUnderTest.attemptAttack(mockkRelaxed(), mockkRelaxed(), "A1")

        // then: game is ongoing
        assertEquals(GameResult.ONGOING, result.first)
        // and: there's no winner
        assertEquals(null, result.second)
    }

    @Test
    fun `players must have placed all their ships, before the game can begin`() {
        // given: a simple match
        val classUnderTest = Match(mockkRelaxed(), mockkRelaxed())
        val playerA = Player("Player A")
        val playerB = Player("Player B")
        val ships = listOf(Ship("Carrier", 5), Ship("Testboat", 1))
        classUnderTest.addPlayer(playerA)
        classUnderTest.addPlayer(playerB)
        classUnderTest.initGame(mockkRelaxed(), ships)

        // when: attemptAttack is called before ships are placed
        val result = classUnderTest.attemptAttack(playerA, playerB, "")

        // then: placement is returned
        assertEquals(GameResult.SHIP_PLACEMENT, result.first)
        assertEquals(null, result.second)
    }

    @Test
    fun `game is ongoing, when all ships have been placed`() {
        // given: a simple match
        val classUnderTest = Match(mockkRelaxed(), mockkRelaxed())
        val playerA = Player("Player A")
        val playerB = Player("Player B")
        val ships = listOf(Ship("Carrier", 5), Ship("Testboat", 1))
        classUnderTest.addPlayer(playerA)
        classUnderTest.addPlayer(playerB)
        classUnderTest.initGame(GameBoard(10, 10), ships)
        classUnderTest.placeShip(playerA, playerA.shipyard[0], "A1", true)
        classUnderTest.placeShip(playerA, playerA.shipyard[1], "A2", true)
        classUnderTest.placeShip(playerB, playerB.shipyard[0], "A1", true)
        classUnderTest.placeShip(playerB, playerB.shipyard[1], "A2", true)

        // when: attemptAttack is called after ships are placed
        val result = classUnderTest.attemptAttack(playerA, playerB, "A1")

        // then: placement is returned
        assertEquals(GameResult.ONGOING, result.first)
        assertEquals(null, result.second)
    }

    @Test
    fun `placeShip places ship on gameBoard`() {
        // given: a match
        val classUnderTest = Match(mockkRelaxed(), mockkRelaxed())
        val playerA = Player("Player A")
        val gameBoardA = GameBoard(10, 10)
        val testBoat = Ship("Testboat", 5)
        val cruiser = Ship("Cruiser", 5)
        playerA.init(gameBoardA, listOf(testBoat, cruiser))
        classUnderTest.addPlayer(playerA)

        // when: placeShip is invoked
        classUnderTest.placeShip(playerA, playerA.shipyard[0], "A1", true)

        // then: cells A1..E1 have a link to the ship (meaning the ship is placed onto those cells)
        listOf("A1", "B1", "C1", "D1", "E1").forEach {
            val shipOnCell = playerA.gameBoard.getShipAt(it)
            assertEquals(testBoat, shipOnCell, "Cell $it should be occupied")
        }
        assertEquals(GameResult.SHIP_PLACEMENT, classUnderTest.attemptAttack(playerA, mockkRelaxed(), "").first)

        // when: all ships are placed
        classUnderTest.placeShip(playerA, playerA.shipyard[1], "A2", false)

        // then: GameResult is ONGOING
        assertEquals(GameResult.ONGOING, classUnderTest.attemptAttack(playerA, mockkRelaxed(), "A1").first)
    }

    @Test
    fun `initGame initializes players and TurnCoordinator`() {
        // given: a simple match
        val turnCoordinator = mockkRelaxed<TurnCoordinator>()
        val classUnderTest = Match(mockkRelaxed(), turnCoordinator)
        val playerA = Player("Player A")
        val playerB = Player("Player B")
        classUnderTest.addPlayer(playerA)
        classUnderTest.addPlayer(playerB)
        val carrier = Ship("Carrier", 5)
        val testBoat = Ship("Testboat", 1)
        val gameBoard = GameBoard(10, 10)

        // when: initGame is invoked
        classUnderTest.initGame(gameBoard, listOf(carrier, testBoat))

        // then: players have gameBoard and ships
        assertFalse(carrier === playerA.shipyard[0])  // we don't want the exact same instance, but a copy
        assertFalse(testBoat === playerA.shipyard[1]) // we don't want the exact same instance, but a copy
        assertFalse(carrier === playerB.shipyard[0])  // we don't want the exact same instance, but a copy
        assertFalse(testBoat === playerB.shipyard[1]) // we don't want the exact same instance, but a copy

        assertEquals(2, playerA.shipyard.size)
        assertEquals(2, playerB.shipyard.size)
        assertEquals(carrier.name, playerA.shipyard[0].name)
        assertEquals(carrier.length, playerA.shipyard[0].length)
        assertEquals(testBoat.name, playerA.shipyard[1].name)
        assertEquals(testBoat.length, playerA.shipyard[1].length)
        assertEquals(carrier.name, playerB.shipyard[0].name)
        assertEquals(carrier.length, playerB.shipyard[0].length)
        assertEquals(testBoat.name, playerB.shipyard[1].name)
        assertEquals(testBoat.length, playerB.shipyard[1].length)

        assertFalse(playerA.gameBoard === playerB.gameBoard)
        assertFalse(gameBoard === playerA.gameBoard)
        assertFalse(gameBoard === playerB.gameBoard)
        assertEquals(gameBoard.width, playerA.gameBoard.width)
        assertEquals(gameBoard.height, playerA.gameBoard.height)
        assertEquals(gameBoard.width, playerB.gameBoard.width)
        assertEquals(gameBoard.height, playerB.gameBoard.height)

        // and then: turnCoordinator should have been initialized with players
        verify {
            turnCoordinator.init(listOf(playerA, playerB))
        }
    }

//    @Test
//    fun `attemptAttack informs subscribers of result`(){
//        // given: a match with subscribers
//        val classUnderTest = Match(mockkRelaxed(), mockkRelaxed())
//        classUnderTest.subscribe()
//    }
}