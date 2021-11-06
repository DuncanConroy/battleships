package com.danielbunte.battleships.io

import com.danielbunte.battleships.match.GameResult
import com.danielbunte.battleships.match.HitResult

data class AttackResultDto(
    val attackingPlayerName: String,
    val targetPlayerName: String,
    val coordinates: String,
    val result: HitResult,
    val gameState: GameResult

) {
    init{
        TODO("Include whole gameBoard as 0,1,2")
    }
}
