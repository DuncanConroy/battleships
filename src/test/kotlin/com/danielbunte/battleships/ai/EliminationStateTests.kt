package com.danielbunte.battleships.ai

import com.danielbunte.battleships.gameworld.GameBoard
import com.danielbunte.battleships.gameworld.Ship
import com.danielbunte.battleships.io.AttackResultDto
import com.danielbunte.battleships.io.Dto
import com.danielbunte.battleships.io.Subscriber
import com.danielbunte.battleships.match.Match
import com.danielbunte.battleships.match.Player
import com.danielbunte.battleships.match.TurnCoordinator
import com.danielbunte.battleships.match.TurnResult
import com.danielbunte.battleships.mockkRelaxed
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class EliminationStateTests {

    @Test
    fun `target on the right border is correctly eliminated, when first hit is in the middle cell`() {
        // given: a GameBoard with an enemy ship on right the border
        val boardSelf = GameBoard(10, 10)
        val self = mockkRelaxed<Player>()
        val aiClient = AIClient(self)
        every { self.gameBoard } returns boardSelf
        val opponent = Player("Player B")
        val turnCoordinator = mockkRelaxed<TurnCoordinator>()
        every { turnCoordinator.makeTurn(any()) } returns (TurnResult.PROCEED to self)
        val match = Match(turnCoordinator = turnCoordinator)
        val previousState = mockkRelaxed<ExplorationState>()
        val classUnderTest = EliminationState(aiClient, previousState, self, opponent, match)
        val subscriber = mockkRelaxed<Subscriber>()
        val attackCapture = slot<Dto>()
        every { subscriber.receive(capture(attackCapture)) } just Runs
        match.subscribe(subscriber)
        match.addPlayer(self)
        match.addPlayer(opponent)
        val ship = Ship("Testboat", 3)
        opponent.init(boardSelf.copy(), listOf(ship))
        match.placeShip(opponent, ship, "H2", true)

        // and: initial hit was in the middle of the ship
        every { previousState.lastAttempt } returns "I2"

        // when: EliminationState runs
        classUnderTest.run()

        // then: cells are hit up to border
        assertEquals("J2", (attackCapture.captured as AttackResultDto).coordinates)

        // and then: direction changes to LEFT and first cell left of initial hit is attacked
        every { subscriber.receive(capture(attackCapture)) } just Runs
        classUnderTest.run()
        assertEquals("H2", (attackCapture.captured as AttackResultDto).coordinates)
    }
}