package com.danielbunte.battleships.gameworld

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ShipTests {

    @Test
    fun `ships health should be equal to length on initialization`() {
        // given: a ship of length 2
        val classUnderTest = Ship("", 2)

        // then: should have health = 2
        assertEquals(2, classUnderTest.health)
    }

    @Test
    fun `ships health should be modifiable`() {
        // given: a ship of length 2
        val classUnderTest = Ship("", 2)

        // when: health is diminished
        classUnderTest.health--

        // then: correct health should be set
        assertEquals(1, classUnderTest.health)
    }
}