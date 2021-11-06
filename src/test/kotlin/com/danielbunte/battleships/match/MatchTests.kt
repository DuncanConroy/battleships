package com.danielbunte.battleships.match

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MatchTests {
    @Test
    fun `adding too many players throws an exception`() {
        // given: two players
        val classUnderTest = Match()
        classUnderTest.addPlayer(Player(""))
        classUnderTest.addPlayer(Player(""))

        // when: an additional player is added
        // then: an exception is thrown
        assertThrows<IllegalArgumentException> { classUnderTest.addPlayer(Player("")) }
    }
}