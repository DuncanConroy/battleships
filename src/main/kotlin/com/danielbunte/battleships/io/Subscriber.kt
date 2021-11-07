package com.danielbunte.battleships.io

import com.danielbunte.battleships.match.Match

interface Subscriber {
    fun receiveAttackResult(attackResult: AttackResultDto)
    fun subscribeToMatch(match: Match)
}