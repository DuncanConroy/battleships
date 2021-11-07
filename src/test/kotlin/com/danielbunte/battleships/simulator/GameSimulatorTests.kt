package com.danielbunte.battleships.simulator

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class GameSimulatorTests {

    @Test
    fun `run simulation`() {
        val simulator = GameSimulator()
        simulator.start()
        assertTrue(true)
    }
}