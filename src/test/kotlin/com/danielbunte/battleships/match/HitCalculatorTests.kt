package com.danielbunte.battleships.match

import com.danielbunte.battleships.gameworld.GameBoard
import com.danielbunte.battleships.gameworld.Ship
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class HitCalculatorTests {

    @RelaxedMockK
    private lateinit var gameBoard: GameBoard

    @Test
    fun `hit on ship diminishes ship's health`() {
        // given: a game board with a placed ship
        val ship = Ship("Testboat", 3)
        every { gameBoard.getShipAt(any()) } returns ship
        val hitCalculator = HitCalculator()

        // when: HitCalculator is invoked with any coordinate
        val result = hitCalculator.attemptAttack(gameBoard, "A1")

        // then: a hit should be issued
        assertEquals(HitResult.HIT, result)

        // and: the ship's health should be diminished
        assertEquals(2, ship.health)
    }

    @Test
    fun `miss returns MISS`() {
        // given: a game board without ships
        every { gameBoard.getShipAt(any()) } returns null
        val hitCalculator = HitCalculator()

        // when: HitCalculator is invoked with any coordinate
        val result = hitCalculator.attemptAttack(gameBoard, "A1")

        // then: a MISS should be issued
        assertEquals(HitResult.MISS, result)
    }

    @Test
    fun `last hit on a ship returns DESTROYED`() {
        // given: a game board with a placed ship
        val ship = Ship("Testboat", 1)
        every { gameBoard.getShipAt(any()) } returns ship
        val hitCalculator = HitCalculator()

        // when: HitCalculator is invoked with any coordinate
        val result = hitCalculator.attemptAttack(gameBoard, "A1")

        // then: a hit should be issued
        assertEquals(HitResult.DESTROYED, result)

        // and: the ship's health should be diminished
        assertEquals(0, ship.health)
    }

    @Test
    fun `successive hits on a destroyed ship returns DESTROYED`() {
        // given: a game board with a placed ship
        val ship = Ship("Testboat", 1)
        ship.health = 0
        every { gameBoard.getShipAt(any()) } returns ship
        val hitCalculator = HitCalculator()

        // when: HitCalculator is invoked with any coordinate
        val result = hitCalculator.attemptAttack(gameBoard, "A1")

        // then: a hit should be issued
        assertEquals(HitResult.DESTROYED, result)

        // and: the ship's health should be unmodified
        assertEquals(0, ship.health)
    }
}